plugins {
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    compileOnly(kotlin("stdlib"))
    compileOnly(libs.lint.api)
    compileOnly(libs.lint.checks)
    
    testImplementation(libs.junit)
    testImplementation(libs.lint)
    testImplementation(libs.lint.tests)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}
