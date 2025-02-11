import com.android.tools.r8.internal.sa
import kotlin.io.extension

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.kotlinx.kover)
}

android {
    namespace = "com.project.learningkitsupplier"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.project.learningkitsupplier"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.foundation.layout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.api.services)

    implementation(libs.ts.components)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    kapt(libs.hilt.android.compiler)
    kapt(libs.hilt.compiler)


    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.truth)
    testImplementation(libs.truth.java8.extension)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.core.testing)
}

koverReport {
    val excludePackages = listOf(
        "dagger.hilt.internal.aggregatedroot.codegen.*",
        "hilt_aggregated_deps.*",
        "com.project.learningkitsupplier.*.di.*",
        "com.project.learningkitsupplier.*.Hilt_*",
        "com.project.learningkitsupplier.*.*_Factory*",
        "com.project.learningkitsupplier.*.*_HiltModules*",
        "com.project.learningkitsupplier.*.*Module_*",
        "com.project.learningkitsupplier.*.*MembersInjector*",
        "com.project.learningkitsupplier.*.*_Impl*",
        "com.project.learningkitsupplier.ComposableSingletons*",
        "com.project.learningkitsupplier.BuildConfig*",
        "com.project.learningkitsupplier.*.Fake*",
        "com.project.learningkitsupplier.app.ComposableSingletons*",
        "*_*Factory.*",
        "*_*Factory*",
        "*_Factory.*",
        "Hilt_*",
        "*_Hilt*",
        "*.navigation.*"
    )

    val includePackages = listOf(
        "com.project.learningkitsupplier.data.*",
        "com.project.learningkitsupplier.domain*",
        "com.project.learningkitsupplier.ui.*.viewmodel",
        "com.project.learningkitsupplier.ui.*.uistate",
        "com.project.learningkitsupplier.ui.*.model",
    )

    filters {
        excludes {
            classes(
                "dagger.hilt.internal.aggregatedroot.codegen.*",
                "hilt_aggregated_deps.*",
                "com.project.learningkitsupplier.*.di.*",
                "com.project.learningkitsupplier.*.Hilt_*",
                "com.project.learningkitsupplier.*.*_Factory*",
                "com.project.learningkitsupplier.*.*_HiltModules*",
                "com.project.learningkitsupplier.*.*Module_*",
                "com.project.learningkitsupplier.*.*MembersInjector*",
                "com.project.learningkitsupplier.*.*_Impl*",
                "com.project.learningkitsupplier.ComposableSingletons*",
                "com.project.learningkitsupplier.BuildConfig*",
                "com.project.learningkitsupplier.*.Fake*",
                "com.project.learningkitsupplier.app.ComposableSingletons*"
            )

            packages(
                "kotlinx.coroutines.*"
            )
        }
    }

    androidReports("debug") {
        xml {
            onCheck = true

            setReportFile(file("result.xml"))

            filters {
                excludes {
                    classes(
                        excludePackages
                    )

                    packages(
                        "kotlinx.coroutines.*"
                    )
                }

                includes {
                    packages(
                        includePackages
                    )
                }
            }
        }
        html {
            title = "Kover Report"

            charset = "UTF-8"

            onCheck = true

            filters {
                excludes {
                    classes(
                        excludePackages
                    )

                    packages(
                        "kotlinx.coroutines.*"
                    )
                }

                includes {
                    packages(
                        includePackages
                    )
                }
            }
        }
    }
}