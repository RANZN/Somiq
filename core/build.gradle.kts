import org.jetbrains.kotlin.gradle.dsl.JvmTarget

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
        }
        commonMain.dependencies {
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
            implementation(libs.koin.core)

            implementation(libs.room.runtime)
            implementation(libs.sqlite.bundled)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.auth)
            implementation(libs.ktor.ktor.client.content.negotiation)
            implementation(libs.ktor.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.logging)
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

android {
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
    }
}
