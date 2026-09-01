# 🤖 UI Automation Engine

[![Android](https://img.shields.io/badge/Platform-Android-brightgreen.svg?style=flat-square)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-blue.svg?style=flat-square)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-navy.svg?style=flat-square)](https://developer.android.com/jetpack/compose)

**UI Automation Engine** is an industrial-grade testing framework for Jetpack Compose. It eliminates the most common pain points of UI testing: **flakiness**, **race conditions**, and **opaque failure logs**. 

By providing a robust orchestration layer over standard Compose APIs, it ensures your tests are stable, readable, and provide immediate diagnostic artifacts on failure.

---

## 📖 Table of Contents
- [Core Philosophy](#-core-philosophy)
- [Architecture: The Robot Pattern](#-architecture-the-robot-pattern)
- [The Robustness Pipeline](#-the-robustness-pipeline)
- [Key Features](#-key-features)
- [Diagnostic Infrastructure](#-diagnostic-infrastructure)
- [Advanced Time Control](#-advanced-time-control)
- [Hilt Integration](#-hilt-integration)
- [Static Analysis (Lint)](#-static-analysis-lint)
- [Installation](#-installation)

---

## 🎯 Core Philosophy

Testing UI shouldn't be about fighting the framework. Most Compose tests fail because they check for elements before recomposition finishes or because they don't handle scroll states. 

The Engine follows three core principles:
1. **Readable DSL**: Tests should read like user stories.
2. **Deterministic Execution**: Every action is synchronized with the Compose clock and idle state.
3. **Artifact-First Failures**: A failing test should tell you *why* it failed with a screenshot and a tree dump, not just a stack trace.

---

## 🏗 Architecture: The Robot Pattern

The engine enforces the **Robot Pattern**, separating the "What" of the test from the "How" of the implementation.

### 1. The Robot (`ComposeRuleScope`)
Robots implement the `ComposeRuleScope` to gain access to the engine's robust action suite.

```kotlin
class GestureRobot(override val composeRule: ComposeTestRule) : ComposeRuleScope {
    fun tapMainBox() {
        clickOnTag("gesture_box") // Automatic idle-sync & scroll
    }

    fun verifyStatus(text: String) {
        assertTextEquals("gesture_status", text) // Automatic polling
    }
}
```

### 2. The DSL (`withRobot`)
Entry point that provides a clean scope for test execution.

```kotlin
@Test
fun testGestureFlow() {
    composeTestRule.withRobot(GestureRobot(composeTestRule)) {
        tapMainBox()
        verifyStatus("Tapped")
    }
}
```

---

## 🛡 The Robustness Pipeline

Every extension method in the engine (like `clickOnTag`, `enterText`, `swipe`) is wrapped in a `runRobustly` block. This pipeline performs the following steps:

1.  **Idle Synchronization**: Waits for the Compose UI to be stable.
2.  **Auto-Scrolling**: Attempts to bring the node into the viewport before interaction.
3.  **Polling & Retry**: Uses `waitUntil` to handle elements that might be appearing via animations or network delays.
4.  **Failure Capture**: If all retries fail, it automatically triggers the diagnostic suite.

---

## ✨ Key Features

| Action Category | Examples | Benefits |
| :--- | :--- | :--- |
| **Gestures** | `clickOnTag`, `doubleTap`, `longPress`, `dragAndDrop`, `pinchToZoom` | Handles complex multi-touch interactions with built-in idle waiting. |
| **Text Input** | `enterText`, `replaceText`, `clearText`, `pressImeAction` | Ensures the keyboard is ready and verifies state after input. |
| **Scrolling** | `scrollToTag`, `scrollToIndex`, `swipeUntilVisible` | Prevents "Node not found" errors in long LazyColumns. |
| **System** | `pressBack`, `pressHome`, `handlePermissionDialog` | Seamlessly interacts with Android OS dialogs using UIAutomator. |

---

## 📸 Diagnostic Infrastructure

Debugging failed CI runs is often impossible without visual feedback. The Engine provides:

### Automatic Screenshots
On every `AssertionError`, a high-resolution screenshot is saved to the device's cache directory: `FAILURE_<timestamp>.png`.

### Semantics Tree Dump
The engine dumps the entire **unmerged semantics tree** to Logcat under the `ComposeAutomation` tag. This reveals the exact state of the UI at the microsecond of failure.

### Log4j2 Integration
The entire engine is instrumented with Log4j2. You can see every internal decision in your logs:
```text
INFO  GestureActions - Starting clickOnTag: testTag=login_button
DEBUG GestureActions - Attempting to scroll to tag: login_button
DEBUG GestureActions - Waiting for tag to be displayed and enabled
DEBUG GestureActions - Performing semantics click on tag: login_button
```

---

## 🕒 Advanced Time Control

Testing animations? The Engine provides a safer way to manipulate the `MainTestClock`.

*   **`advanceTime(ms)`**: Advances the clock by a specific duration.
*   **`advanceTimeUntil(condition)`**: Advances the clock in frame-increments until a UI state is met (perfect for finishing animations).
*   **`withPausedClock { ... }`**: Automatically pauses the clock, runs your logic, and resumes it, ensuring no side effects on other tests.

---

## 💉 Hilt Integration

The engine is built for modern DI-heavy architectures.

*   **Rule Chaining**: Use `createHiltComposeRule` to correctly order your Hilt and Compose rules.
*   **Entry Points**: Use `getTestEntryPoint<T>()` inside your Robots to access injected dependencies (like repositories or database managers) without boilerplate.

---

## 🚫 Static Analysis (Lint)

The `:engine-lint` module ensures your team doesn't regress into flaky habits. It detects direct usage of standard Compose APIs like `performClick()` or `onNodeWithTag()` and flags them as errors, suggesting the robust Engine equivalent via QuickFix.

---

## 🛠 Installation

### 1. Add Repository
In your root `settings.gradle.kts`, add the JitPack repository:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

### 2. Add Dependency
Add the engine to your module-level `build.gradle.kts`:

```kotlin
dependencies {
    // UI Automation Engine (Standard Version)
    androidTestImplementation("com.github.rajbirsehmi:UI-Engine:0.0.1")
}
```

### 3. Handle Resource Collisions (Log4j2)
Add this to your `build.gradle.kts` to avoid packaging errors:
```kotlin
android {
    packaging {
        resources {
            excludes += "/META-INF/DEPENDENCIES"
            excludes += "/META-INF/LICENSE*"
            excludes += "/META-INF/NOTICE*"
        }
    }
}
```

---

## 📄 License
Copyright © 2026 Sehmi. Built with reliability in mind.
