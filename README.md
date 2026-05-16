This is a Kotlin Multiplatform project following the [new KMP default structure](https://blog.jetbrains.com/kotlin/2026/05/new-kmp-default-structure/): **`:shared`** holds shared UI and logic; each platform has its own application module.

| Shell | Gradle module | Role |
|-------|----------------|------|
| **Android** | [`:androidApp`](./androidApp) (`assembleDebug` / Run) | APK entry point |
| **iOS** | [`:shared`](./shared) + [`iosApp`](./iosApp) (Xcode) | Swift hosts `MainViewController` from the `Shared` framework |
| **Desktop (JVM)** | [`:desktopApp`](./desktopApp) (`run`) | Compose Desktop window; same `App()` as mobile |

Shared UI, navigation, and DI live under **[shared/src/commonMain](./shared/src/commonMain)**. Platform code: [iosMain](./shared/src/iosMain).

**Architecture:** [docs/ARCHITECTURE.md](.docs/ARCHITECTURE.md) (modules, MVI-style presentation, `BaseViewModel`, navigation, auth summary). **Auth flow detail:** [docs/LOGIN_FLOW_PLAN.md](.docs/LOGIN_FLOW_PLAN.md).

* [/iosApp](./iosApp/iosApp) — Xcode host for the iOS app (embeds the `Shared` framework).

### Build and Run Android Application

- on macOS/Linux
  ```shell
  ./gradlew :androidApp:assembleDebug
  ```
- on Windows
  ```shell
  .\gradlew.bat :androidApp:assembleDebug
  ```

### Build and Run Desktop (JVM) Application

- on macOS/Linux
  ```shell
  ./gradlew :desktopApp:run
  ```
- on Windows
  ```shell
  .\gradlew.bat :desktopApp:run
  ```

### Build and Run iOS Application

Use the IDE run configuration or open [/iosApp](./iosApp) in Xcode.

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html) and [Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform/#compose-multiplatform).
