plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

gradlePlugin {
    plugins {
        create("composeResKspSetup") {
            id = "compose.res.ksp.setup"
            implementationClass = "ComposeResKspSetupPlugin"
        }
    }
}