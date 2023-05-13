plugins {
    id("org.jetbrains.kotlin.android")
    id("mediproject.android.feature")
    id("androidx.navigation.safeargs.kotlin")
    id(libs.plugins.kapt.get().pluginId)
}

android {
    namespace = "com.android.mediproject.feature.camera"
    buildFeatures {
        buildConfig = true
    }

    externalNativeBuild {
        cmake {
            version = "3.22.1"
            path = file("src/main/jni/CMakeLists.txt")
        }
    }

    defaultConfig {

        ndk {
            version = "25.2.9519653"
            abiFilters += listOf(
                "armeabi-v7a", "arm64-v8a"
            )
        }
    }

}

hilt {
    enableAggregatingTask = true
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:domain"))
    implementation(libs.bundles.glides)
    kapt(libs.bundles.glides.kapt)
    implementation(libs.bundles.kotlins)
    implementation(libs.bundles.materials)
    implementation(libs.bundles.uiAndroidx)
    implementation(libs.bundles.cameras)

    implementation(libs.androidx.paging.runtime)

    implementation(libs.bundles.navigations)
    implementation(libs.bundles.lifecycles)
    implementation(libs.kotlinx.datetime)

}