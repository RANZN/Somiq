# Somiq — application architecture

This document describes how the **Somiq** Kotlin Multiplatform (KMP) client is structured: modules, presentation patterns, navigation, and cross-cutting UI concerns. It complements the [README](../README.md) and feature-specific notes (e.g. [login & signup flow](./LOGIN_FLOW_PLAN.md)).

---

## 1. High-level shape

| Layer | Role | Typical location |
|-------|------|------------------|
| **App shell** | DI, `App()`, root navigation, splash | `:composeApp` |
| **Feature UI** | Screens, feature ViewModels, feature contracts | `:feature-*` (e.g. `feature-auth`, `feature-chat`) |
| **Core** | Shared primitives: networking, storage, navigation keys, `BaseViewModel`, global effects | `:core` |
| **Domain / data** | Use cases, repositories (often co-located with features or shared modules) | Per feature or `:core` |

All **Compose UI** that ships in the app is wired through **`:composeApp`** for Android, iOS, and Desktop (JVM), sharing `commonMain` code where possible.

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

---

## 3. `BaseViewModel`

**Location:** `core/.../presentation/viewmodel/BaseViewModel.kt`

```text
BaseViewModel<S : BaseUiState, I : BaseUiIntent, E : BaseUiEffect>(initialState: S)
```

| API | Responsibility |
|-----|----------------|
| `state: StateFlow<S>` | Current UI state; use `setState { }` for updates. |
| `effect: Flow<E>` | One-shot effects; collect in the composable host. |
| `handleIntent(intent)` | Public entry; delegates to `onIntent`. |
| `emitEffect(effect)` | Queue an effect (buffered channel). |

### 3.1 Initial state **must** be passed into `super(...)`

`MutableStateFlow(initialState)` runs **during** the `BaseViewModel` constructor. Subclass **properties** (e.g. `private val phone`) are **not** initialized until after the superclass constructor returns.

Therefore:

- **Do:** `class X(...) : BaseViewModel<UiState, ...>(UiState(phoneDisplay = phone))` — use **constructor parameters** when building the first `UiState`.
- **Don’t:** Override `initialState` with a getter that reads subclass `val` fields; that can crash or read garbage on some targets.

This applies to any screen that seeds state from navigation arguments (phone, user ids, etc.).

---

## 4. Global UI effects

**Location:** `core/.../presentation/effect/GlobalUiEffect.kt`

`GlobalUiEffect` is a **separate channel** from feature `Effect` types. It is used for app-wide transient UI such as **snackbars** when a feature should not own the scaffold (e.g. validation messages from a ViewModel that uses `GlobalEffectDispatcher`).

Feature screens may still use **local** `Effect.ShowSnackbar` variants where the host already collects feature effects.

---

## 5. Navigation

- **Keys:** `core/.../presentation/navigation/Destination.kt` — serializable `NavKey` types (`Splash`, `OnBoarding`, `HomeGraph`, `Conversation`, etc.).
- **Graph:** `composeApp/.../navigation/AppNavigation.kt` — `NavDisplay` + back stack; feature hosts receive lambdas for navigation.
- **Saved state:** `composeApp/.../navigation/RememberNavBackStack.kt` — polymorphic serialization for `NavKey` (required for KMP / non-reflection targets).

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

**Koin** is used across modules. Feature ViewModels are registered in feature modules (e.g. `authViewModelModule`); `composeApp` aggregates modules for the running target.

---

## 8. Related documents

| Document | Topic |
|----------|--------|
| [LOGIN_FLOW_PLAN.md](./LOGIN_FLOW_PLAN.md) | Phone / OTP / signup / home navigation |
| [NAVIGATION_ANIMATIONS.md](../core/src/commonMain/kotlin/com/ranjan/somiq/core/presentation/navigation/NAVIGATION_ANIMATIONS.md) | Transition notes |
| [README.md](../README.md) | Targets, build commands |

---

*Last updated to reflect constructor-based `BaseViewModel(initialState)` and unified phone auth.*
