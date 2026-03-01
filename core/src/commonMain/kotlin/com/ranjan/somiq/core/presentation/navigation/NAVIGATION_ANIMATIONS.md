# Navigation animations

This project uses **type-safe navigation** with Nav3 (`NavKey` and `composable<T>`). All destination checks use the current `NavKey` (e.g. `currentKey as? Home`) — no string routes.

## Adding transition animations

Nav3 **1.1.0+** supports `transitionSpec` and `popTransitionSpec` on `NavDisplay` for enter/exit animations.

1. Upgrade in `gradle/libs.versions.toml`:
   - `nav3Core = "1.1.0-alpha06"` (or latest 1.1.x when stable)

2. Add the Compose animation dependency (e.g. `androidx.compose.animation:animation` or the KMP equivalent).

3. In `AppNavigation.kt`, add to `NavDisplay`:

```kotlin
transitionSpec = {
    ContentTransform(
        targetContentEnter = slideInHorizontally(animationSpec = tween(300)) { it } + fadeIn(animationSpec = tween(300)),
        initialContentExit = slideOutHorizontally(animationSpec = tween(300)) { -it } + fadeOut(animationSpec = tween(300))
    )
},
popTransitionSpec = {
    ContentTransform(
        targetContentEnter = slideInHorizontally(animationSpec = tween(300)) { -it } + fadeIn(animationSpec = tween(300)),
        initialContentExit = slideOutHorizontally(animationSpec = tween(300)) { it } + fadeOut(animationSpec = tween(300))
    )
},
```

With Nav3 **1.0.x**, `NavDisplay` does not expose these parameters; animations require upgrading to 1.1+.
