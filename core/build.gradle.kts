import com.android.build.api.dsl.LibraryExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
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
            baseName = "Core"
            isStatic = true
        }
    }

    jvm()

    sourceSets {
        androidMain.dependencies {
            implementation(libs.koin.android)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.chucker)
            implementation(libs.androidx.datastore.preferences)
        }
        commonMain.dependencies {
            implementation(libs.runtime)
            implementation(libs.foundation)
            implementation(libs.material3)
            implementation(libs.ui)
            implementation(libs.components.resources)
            implementation(libs.ui.tooling.preview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation(libs.material.icons.extended)
            implementation(libs.jetbrains.navigation3.ui)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.core)

            implementation(libs.room.runtime)
            implementation(libs.sqlite.bundled)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.auth)
            implementation(libs.ktor.ktor.client.content.negotiation)
            implementation(libs.ktor.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.logging)
            
            // Coil for image loading
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)

            implementation(libs.androidx.datastore.preferences.core)
            implementation(libs.androidx.datastore.core.okio)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            implementation(libs.ktor.client.cio)
        }

        val iosMain by creating {
            dependsOn(getByName("commonMain"))
            dependencies {
                implementation("io.ktor:ktor-client-darwin:${libs.versions.ktor.get()}")
            }
        }

        getByName("iosArm64Main").dependsOn(iosMain)
        getByName("iosSimulatorArm64Main").dependsOn(iosMain)
    }
}

configure<LibraryExtension> {
    namespace = "com.ranjan.somiq.core"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // Room KSP - only for Android for now
    // For full KMP support, you'll need to configure @ConstructedBy properly
    add("kspAndroid", libs.room.compiler)
    // Temporarily disabled for other platforms until @ConstructedBy is properly configured
    // add("kspIosArm64", libs.room.compiler)
    // add("kspIosSimulatorArm64", libs.room.compiler)
    // add("kspJvm", libs.room.compiler)

    room {
        schemaDirectory("$projectDir/schemas")
    }
}

// Compose Resources before KSP (Room); inlined from former build-logic `compose.res.ksp.setup`.
afterEvaluate {
    val kmp = extensions.getByType<KotlinMultiplatformExtension>()
    fun cap(s: String) = s.replaceFirstChar { it.uppercaseChar() }

    fun org.gradle.api.Task.composePreamble(useMainCommon: Boolean) {
        val scope = if (useMainCommon) "Main" else "Test"
        dependsOn(tasks.named("generateComposeResClass"))
        dependsOn(tasks.named("generateResourceAccessorsForCommon$scope"))
        tasks.findByName("generateExpectResourceCollectorsForCommon$scope")?.let { dependsOn(it) }
    }

    fun org.gradle.api.Task.dependsOnOptional(n: String) = tasks.findByName(n)?.let { dependsOn(it) }

    fun org.gradle.api.Task.androidKspFrom(compilationName: String) {
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

    fun jvmKspTaskName(compilationName: String) =
        when (compilationName) {
            "main" -> "kspKotlinJvm"
            else -> "ksp${cap(compilationName)}KotlinJvm"
        }

    fun org.gradle.api.Task.jvmKspFrom(compilationName: String) {
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

    fun org.gradle.api.Task.iosMainKsp(suffix: String) {
        composePreamble(useMainCommon = true)
        dependsOn(tasks.named("generateResourceAccessorsFor${suffix}Main"))
        dependsOn(tasks.named("generateActualResourceCollectorsFor${suffix}Main"))
        dependsOnOptional("generateResourceAccessorsForIosMain")
    }

    fun org.gradle.api.Task.iosTestKsp(suffix: String) {
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