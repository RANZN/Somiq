import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    jvm()
    /*

    js {
        browser()
        binaries.executable()
    }
        @OptIn(ExperimentalWasmDsl::class)
        wasmJs {
            browser()
            binaries.executable()
        }
    */

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
            implementation(libs.ktor.client.okhttp)
        }
        commonMain.dependencies {
            // Feature modules
            api(project(":core"))
            api(project(":auth"))
            api(project(":home"))
            
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation(compose.materialIconsExtended)
            implementation(libs.compose.navigation)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.koin.compose.viewmodel)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }

        val iosMain by creating {
            dependsOn(getByName("commonMain"))
            dependencies {
            }
        }

        getByName("iosArm64Main").dependsOn(iosMain)
        getByName("iosSimulatorArm64Main").dependsOn(iosMain)
    }
}

android {
    namespace = "com.ranjan.somiq"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.ranjan.somiq"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    dependencies {
        debugImplementation(libs.chucker)
        releaseImplementation(libs.chucker.release)
    }
}

dependencies {
    debugImplementation(compose.uiTooling)

    ksp(libs.room.compiler)
    debugImplementation(compose.uiTooling)
    // KSP support for Room Compiler.
    add("kspAndroid", libs.room.compiler)
    add("kspIosArm64", libs.room.compiler)           // For physical iOS devices
    add("kspIosSimulatorArm64", libs.room.compiler) // For M-series Mac simulators
    add("kspJvm", libs.room.compiler) // For Desktop

    room {
        schemaDirectory("$projectDir/schemas")
    }
}

compose.desktop {
    application {
        mainClass = "com.ranjan.somiq.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.ranjan.somiq"
            packageVersion = "1.0.0"
        }
    }
}

afterEvaluate {
    android.buildTypes.forEach { buildType ->
        val buildTypeName = buildType.name.replaceFirstChar(Char::titlecase)
        val kspTaskName = "ksp${buildTypeName}KotlinAndroid"
        tasks.named(kspTaskName) {
            dependsOn(tasks.named("generateResourceAccessorsForAndroid$buildTypeName"))
            dependsOn(tasks.named("generateResourceAccessorsForAndroidMain"))
            dependsOn(tasks.named("generateActualResourceCollectorsForAndroidMain"))
            dependsOn(tasks.named("generateComposeResClass"))
            dependsOn(tasks.named("generateResourceAccessorsForCommonMain"))
            tasks.findByName("generateExpectResourceCollectorsForCommonMain")?.let { dependsOn(it) }
        }
        
        // Fix test KSP task dependencies
        val testKspTaskName = "ksp${buildTypeName}UnitTestKotlinAndroid"
        tasks.findByName(testKspTaskName)?.let { testKspTask ->
            tasks.findByName("generateResourceAccessorsForAndroidUnitTest$buildTypeName")?.let { testKspTask.dependsOn(it) }
            tasks.findByName("generateResourceAccessorsForAndroidUnitTest")?.let { testKspTask.dependsOn(it) }
            tasks.findByName("generateResourceAccessorsForCommonTest")?.let { testKspTask.dependsOn(it) }
            tasks.findByName("generateResourceAccessorsForCommonMain")?.let { testKspTask.dependsOn(it) }
            tasks.findByName("generateComposeResClass")?.let { testKspTask.dependsOn(it) }
            tasks.findByName("generateExpectResourceCollectorsForCommonMain")?.let { testKspTask.dependsOn(it) }
        }
    }
    
    // iOS KSP task dependencies
    listOf("IosSimulatorArm64", "IosArm64").forEach { iosTarget ->
        val kspTaskName = "kspKotlin$iosTarget"
        tasks.findByName(kspTaskName)?.let { kspTask ->
            try {
                kspTask.dependsOn(tasks.named("generateResourceAccessorsFor${iosTarget}Main"))
            } catch (e: Exception) { }
            try {
                kspTask.dependsOn(tasks.named("generateActualResourceCollectorsFor${iosTarget}Main"))
            } catch (e: Exception) { }
            try {
                kspTask.dependsOn(tasks.named("generateComposeResClass"))
            } catch (e: Exception) { }
            try {
                kspTask.dependsOn(tasks.named("generateResourceAccessorsForCommonMain"))
            } catch (e: Exception) { }
            tasks.findByName("generateExpectResourceCollectorsForCommonMain")?.let { kspTask.dependsOn(it) }
            tasks.findByName("generateResourceAccessorsForIosMain")?.let { kspTask.dependsOn(it) }
        }
    }
}
