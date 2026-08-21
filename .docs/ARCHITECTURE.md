# Somiq — application architecture

This document describes how the **Somiq** Kotlin Multiplatform (KMP) client is structured: modules, presentation patterns, navigation, and cross-cutting UI concerns. It complements the [README](../README.md) and feature-specific notes (e.g. [login & signup flow](./LOGIN_FLOW_PLAN.md)).

---

## 1. High-level shape

| Layer | Role | Typical location |
|-------|------|------------------|
| **App shell** | DI, `App()`, root navigation, splash | `:shared` |
| **Feature UI** | Screens, feature ViewModels, feature contracts | `:feature-*` (e.g. `feature-auth`, `feature-chat`) |
| **Core** | Infrastructure only: HTTP client, token/auth storage, `BaseViewModel`, `AppError`, `UiText`, Coil, global effects | `:core` |
| **Domain / data** | Use cases, repositories | Per feature or `:shared` (app-shell screens) |

All **Compose UI** that ships in the app is wired through **`:shared`** for Android, iOS, and Desktop (JVM), with platform entry points in **`androidApp`**, **`desktopApp`**, and **`iosApp`**.

### 1.1 `:feature-auth` layout

| Path | Contents |
|------|----------|
| `data/model` | Auth API DTOs (`AuthRequest`, `AuthResponse`, `User`, errors, OTP payloads). |
| `data/repository` | `AuthRepositoryImpl` (implements `domain.repository.AuthRepository`). |
| `domain/model` | Domain results (`VerifyOtpResult`, `AuthResult`). |
| `domain/repository` | `AuthRepository` interface. |
| `domain/usecase` | OTP, signup completion, check username, `UserLoginStatus`, `LogoutUseCase`. |
| `ui/phone`, `ui/otp`, `ui/completeprofile` | Phone → OTP → complete-profile flows (`*Screen`, `*ScreenHost`, `*ViewModel`, `*Contract`). |
| `ui/components` | Shared composables: `LoginHeader`, `CompleteProfileHeader`, `OptionalProfileAvatar`. |
| `di` | `AuthModule`, `AuthViewModelModule`. |

**Profile** loading and `GetProfileUseCase` live in **`:feature-profile`** only; they are not duplicated in `feature-auth`.

---

## 2. Presentation pattern (MVI-style)

Feature screens follow a small **unidirectional** pattern:

| Piece | Purpose |
|-------|---------|
| **`UiState`** (`data class`, implements `BaseUiState`) | What the UI renders; immutable snapshot. |
| **`Intent`** (`sealed interface`, implements `BaseUiIntent`) | User actions and one-off events from the UI. |
| **`Effect`** (`sealed interface`, implements `BaseUiEffect`) | Side effects that are **not** part of state: navigation, one-shot dialogs, snackbars handled in the host. |

Flow:

1. UI calls `viewModel.handleIntent(intent)`.
2. ViewModel updates state with `setState { copy(...) }` or emits `emitEffect(...)`.
3. UI collects `state` (Compose) and `effect` (typically in a `ScreenHost` that maps effects to navigation or global UI).

Contracts are usually grouped in a `*Contract` object (`UiState` / `Intent` / `Effect` nested types) per feature.

### 2.1 Lifecycle-aware Composable Collection
All UI states are collected in the Composable hosts using `collectAsStateWithLifecycle()` to prevent unnecessary flow collection when the app is in the background, conserving system resources.
 
---

## 3. `BaseViewModel`

**Location:** `core/.../presentation/viewmodel/BaseViewModel.kt`

```text
BaseViewModel<I : BaseUiIntent, E : BaseUiEffect>
```

| API | Responsibility |
|-----|----------------|
| `effect: Flow<E>` | Flow of screen-specific UI effects. |
| `commonEffect: Flow<BaseUiEffect.Common>` | Flow of shared/common UI effects (like snackbars) collected automatically. |
| `handleIntent(intent)` | Public entry; delegates to `onIntent`. |
| `emitEffect(effect)` | Queue a screen-specific effect (buffered channel). |
| `showSnackbar(message)` | Queue a common snackbar effect. |

### 3.1 State Management
`BaseViewModel` is stateless. Concrete ViewModels manage state locally using standard `MutableStateFlow`:

- Expose state as: `val uiState: StateFlow<UiState> = _uiState.asStateFlow()`
- Update state using: `_uiState.update { ... }` or `_uiState.value = ...`

### 3.2 UI Effect Collection (`collectEffects`)
To avoid reference boilerplate and ensure lifecycle safety, screen hosts use the `viewModel.collectEffects { ... }` extension function. This concurrently collects:
- Screen-specific effects (emitted via `emitEffect`) passed to the lambda.
- App-wide common effects (like `showSnackbar`) which are intercepted and automatically shown using the `LocalSnackbar.current` host state.

---

## 4. Common UI Effects & Snackbars

App-wide transient actions like showing a snackbar do not require local scaffold boilerplate or passing viewmodel references. They are routed via a second flow inside `BaseViewModel`:

- ViewModels trigger: `showSnackbar("Message")` (which maps to `BaseUiEffect.Common.ShowSnackbar`).
- The `collectEffects` extension function automatically intercepts `commonEffect` emissions and displays them using Compose's `LocalSnackbar.current` provider.

---

## 5. Navigation

- **Keys:** `shared/.../navigation/NavDestinations.kt` — serializable `NavKey` types (`Splash`, `OnBoarding`, `HomeGraph`, `Conversation`, etc.).
- **Graph:** `shared/.../navigation/AppNavigation.kt` — `NavDisplay` + back stack; feature hosts receive lambdas for navigation.
- **Saved state:** `shared/.../navigation/RememberNavBackStack.kt` — polymorphic serialization for `NavKey` (required for KMP / non-reflection targets).

Optional UX notes: [NAVIGATION_ANIMATIONS.md](../core/src/commonMain/kotlin/com/ranjan/somiq/core/presentation/navigation/NAVIGATION_ANIMATIONS.md).

---

## 6. Authentication flow (summary)

The client uses a **single** phone → OTP path; the server returns either session tokens or a signup token. New users complete **name + username** on `CompleteProfile` before entering the main graph.

### 6.1 Device-bound auth context

- A stable per-installation `deviceId` is persisted in `core` DataStore (`TokenStorage`).
- `feature-auth` includes this `deviceId` in OTP verification request.
- Backend includes `deviceId` in JWT claims; refresh token rotation preserves the same device binding.
- If refresh token/device mapping is missing or mismatched in backend DB, refresh is rejected with **401**.

Details, API table, and navigation diagram: [LOGIN_FLOW_PLAN.md](./LOGIN_FLOW_PLAN.md).

---

## 7. Dependency injection

**Koin** is used across modules. Feature ViewModels are registered in feature modules (e.g. `authViewModelModule`); `shared` aggregates modules for the running target.
- **Global Context:** A global, application-wide `CoroutineScope` is registered in `networkModule` and injected into background managers (such as `PostUploadManager`) to avoid hardcoding coroutine dispatchers and scopes.
 
---
 
## 8. Network Configuration & Routing
 
- **Base URL:** Defined globally via `BASE_URL` in `core`.
- **Relative Paths:** The Ktor `HttpClient` is configured with `defaultRequest { url(BASE_URL) }` in `HttpClientFactory.kt`. Repositories make requests using clean relative paths (e.g., `httpClient.get("v1/posts")`), eliminating absolute URL path interpolation boilerplates.

---

## 9. Related documents

| Document | Topic |
|----------|--------|
| [LOGIN_FLOW_PLAN.md](./LOGIN_FLOW_PLAN.md) | Phone / OTP / signup / home navigation |
| [NAVIGATION_ANIMATIONS.md](../core/src/commonMain/kotlin/com/ranjan/somiq/core/presentation/navigation/NAVIGATION_ANIMATIONS.md) | Transition notes |
| [README.md](../README.md) | Targets, build commands |

---

*Last updated to reflect stateless BaseViewModel, dual-channel common effects (collectEffects), injected coroutine scopes, relative network paths, and lifecycle-aware state collection.*
