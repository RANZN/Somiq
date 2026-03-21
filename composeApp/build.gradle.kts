import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget

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
    val roomCompiler = libs.room.compiler
    listOf(
        "kspAndroid",
        "kspJvm",
        "kspIosArm64",
        "kspIosSimulatorArm64",
    ).forEach { add(it, roomCompiler) }

    room {
        schemaDirectory(layout.projectDirectory.dir("schemas"))
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
    val kmp = extensions.getByType<KotlinMultiplatformExtension>()
    fun cap(s: String) = s.replaceFirstChar { it.uppercaseChar() }

    /** Common codegen shared by main-line KSP; [useMainCommon] false = commonTest collectors. */
    fun Task.composePreamble(useMainCommon: Boolean) {
        val scope = if (useMainCommon) "Main" else "Test"
        dependsOn(tasks.named("generateComposeResClass"))
        dependsOn(tasks.named("generateResourceAccessorsForCommon$scope"))
        tasks.findByName("generateExpectResourceCollectorsForCommon$scope")?.let { dependsOn(it) }
    }

    fun Task.dependsOnOptional(n: String) = tasks.findByName(n)?.let { dependsOn(it) }

    fun Task.androidKspFrom(compilationName: String) {
        when {
            compilationName.endsWith("UnitTest") -> {
                val bt = cap(compilationName.removeSuffix("UnitTest"))
                composePreamble(useMainCommon = false)
                dependsOnOptional("generateResourceAccessorsForAndroidUnitTest$bt")
                dependsOnOptional("generateResourceAccessorsForAndroidUnitTest")
                dependsOnOptional("generateResourceAccessorsForCommonTest")
            }

            compilationName.endsWith("AndroidTest") -> {
                val bt = cap(compilationName.removeSuffix("AndroidTest"))
                composePreamble(useMainCommon = true)
                dependsOnOptional("generateResourceAccessorsForAndroidInstrumentedTest$bt")
                dependsOnOptional("generateResourceAccessorsForAndroidInstrumentedTest")
                dependsOnOptional("generateResourceAccessorsForCommonMain")
            }

            else -> {
                val bt = cap(compilationName)
                composePreamble(useMainCommon = true)
                dependsOn(tasks.named("generateResourceAccessorsForAndroid$bt"))
                dependsOn(tasks.named("generateResourceAccessorsForAndroidMain"))
                dependsOn(tasks.named("generateActualResourceCollectorsForAndroidMain"))
            }
        }
    }

    fun jvmKspTaskName(compilationName: String) = when (compilationName) {
        "main" -> "kspKotlinJvm"
        else -> "ksp${cap(compilationName)}KotlinJvm"
    }

    fun Task.jvmKspFrom(compilationName: String) {
        val seg = cap(compilationName)
        when (compilationName) {
            "test" -> {
                composePreamble(useMainCommon = false)
                dependsOnOptional("generateResourceAccessorsForJvmTest")
            }

            else -> {
                composePreamble(useMainCommon = true)
                dependsOn(tasks.named("generateResourceAccessorsForJvm$seg"))
                dependsOnOptional("generateActualResourceCollectorsForJvm$seg")
            }
        }
    }

    fun Task.iosMainKsp(suffix: String) {
        composePreamble(useMainCommon = true)
        dependsOn(tasks.named("generateResourceAccessorsFor${suffix}Main"))
        dependsOn(tasks.named("generateActualResourceCollectorsFor${suffix}Main"))
        dependsOnOptional("generateResourceAccessorsForIosMain")
    }

    fun Task.iosTestKsp(suffix: String) {
        composePreamble(useMainCommon = false)
        dependsOnOptional("generateResourceAccessorsFor${suffix}Test")
        dependsOnOptional("generateResourceAccessorsForIosMain")
    }

    kmp.targets.filterIsInstance<KotlinAndroidTarget>().forEach { t ->
        t.compilations.forEach { c ->
            tasks.findByName("ksp${cap(c.name)}KotlinAndroid")?.androidKspFrom(c.name)
        }
    }

    kmp.targets.filterIsInstance<KotlinJvmTarget>().forEach { t ->
        t.compilations.forEach { c ->
            tasks.findByName(jvmKspTaskName(c.name))?.jvmKspFrom(c.name)
        }
    }

    kmp.targets.filter { it.name.startsWith("ios") }.forEach { t ->
        val s = cap(t.name)
        tasks.findByName("kspKotlin$s")?.iosMainKsp(s)
        tasks.findByName("kspTestKotlin$s")?.iosTestKsp(s)
    }
}
