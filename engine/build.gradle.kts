plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.compiler)
    id("maven-publish")
}

android {
    namespace = "com.sehmi.engine"
    compileSdk = 37

    defaultConfig {
        minSdk = 30
        resourcePrefix = "engine_"
        consumerProguardFiles("consumer-rules.keep")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = false
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

    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }

    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/DEPENDENCIES"
            excludes += "/META-INF/LICENSE"
            excludes += "/META-INF/LICENSE.txt"
            excludes += "/META-INF/NOTICE"
            excludes += "/META-INF/NOTICE.txt"
        }
    }

    publishing {
        singleVariant("release") {
            withJavadocJar()
        }
    }
}

dependencies {
    // Pure Jetpack Compose UI Testing Framework
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.compose.ui.test.junit4)

    // UiAutomator for system-level actions
    api(libs.androidx.uiautomator)

    // Hilt Testing (Transitive API)
    api(libs.hilt.android.testing)
    
    // Core Android Test Library
    api(libs.androidx.core.ktx)

    // Logging (Log4j2)
    api(libs.log4j.api)
    api(libs.log4j.core)

    // Embed custom lint rules into the AAR
    lintPublish(project(":engine-lint"))
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.sehmi.engine"
            artifactId = "robot-testing-engine"
            version = "0.0.2-alpha"

            afterEvaluate {
                from(components["release"])
            }
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/rajbirsehmi/UI-Engine")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
