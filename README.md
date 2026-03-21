This is a Kotlin Multiplatform project. **Three client targets** are first-class: **Android**, **iOS**, and **Desktop (JVM)** — all from **`:composeApp`**.

| Shell | Gradle module | Role |
|-------|----------------|------|
| **Android** | [`:composeApp`](./composeApp) (`assembleDebug` / Run) | APK |
| **iOS** | [`:composeApp`](./composeApp) + [`iosApp`](./iosApp) (Xcode) | Swift hosts `MainViewController` from the KMP framework |
| **Desktop (JVM)** | [`:composeApp`](./composeApp) (`:composeApp:jvmRun`) | Compose Desktop window; same `App()` as mobile |

Shared UI, navigation, and DI live under **[composeApp/src/commonMain](./composeApp/src/commonMain)**. Platform code: [androidMain](./composeApp/src/androidMain), [iosMain](./composeApp/src/iosMain), [jvmMain](./composeApp/src/jvmMain).

* [/iosApp](./iosApp/iosApp) — Xcode host for the iOS app (embeds the `ComposeApp` framework).

### Build and Run Android Application

- on macOS/Linux
  ```shell
  ./gradlew :composeApp:assembleDebug
  ```
- on Windows
  ```shell
  .\gradlew.bat :composeApp:assembleDebug
  ```

### Build and Run Desktop (JVM) Application

- on macOS/Linux
  ```shell
  ./gradlew :composeApp:jvmRun
  ```
  or
  ```shell
  ./gradlew :composeApp:run
  ```
- on Windows
  ```shell
  .\gradlew.bat :composeApp:jvmRun
  ```

### Build and Run iOS Application

Use the IDE run configuration or open [/iosApp](./iosApp) in Xcode.

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html) and [Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform/#compose-multiplatform).
