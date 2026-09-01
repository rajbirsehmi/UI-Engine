# UI Automation Engine Implementation Plan

Build a complete, production-grade Jetpack Compose UI test automation framework ecosystem.

## Proposed Changes

### Module `:engine` (com.sehmi.engine)

#### [NEW] [ComposeRuleScope.kt](file:///D:/Android/Projects/UIAutomationEngine/engine/src/main/java/com/sehmi/engine/core/ComposeRuleScope.kt)
Core DSL scope interface and entry point.

#### [NEW] [FlakinessUtils.kt](file:///D:/Android/Projects/UIAutomationEngine/engine/src/main/java/com/sehmi/engine/utils/FlakinessUtils.kt)
Retry logic, tree dumping, and screenshot capture.

#### [NEW] [SystemActions.kt](file:///D:/Android/Projects/UIAutomationEngine/engine/src/main/java/com/sehmi/engine/actions/SystemActions.kt)
Hardware and system-level actions using UiAutomator.

#### [NEW] [GestureActions.kt](file:///D:/Android/Projects/UIAutomationEngine/engine/src/main/java/com/sehmi/engine/actions/GestureActions.kt)
Touch, gestures, and pointer actions.

#### [NEW] [TextAndFocusActions.kt](file:///D:/Android/Projects/UIAutomationEngine/engine/src/main/java/com/sehmi/engine/actions/TextAndFocusActions.kt)
Text input, focus, and keyboard interactions.

#### [NEW] [ScrollActions.kt](file:///D:/Android/Projects/UIAutomationEngine/engine/src/main/java/com/sehmi/engine/actions/ScrollActions.kt)
Scrolling and collection actions.

#### [NEW] [NodeAssertions.kt](file:///D:/Android/Projects/UIAutomationEngine/engine/src/main/java/com/sehmi/engine/assertions/NodeAssertions.kt)
State, bounds, and layout assertions.

#### [NEW] [SemanticsMatchers.kt](file:///D:/Android/Projects/UIAutomationEngine/engine/src/main/java/com/sehmi/engine/matchers/SemanticsMatchers.kt)
Custom matchers for roles and content descriptions.

#### [NEW] [HiltComposeRule.kt](file:///D:/Android/Projects/UIAutomationEngine/engine/src/main/java/com/sehmi/engine/hilt/HiltComposeRule.kt)
#### [NEW] [HiltEntryPoints.kt](file:///D:/Android/Projects/UIAutomationEngine/engine/src/main/java/com/sehmi/engine/hilt/HiltEntryPoints.kt)
Hilt integration helpers.

---

### Module `:engine-lint` (com.sehmi.engine.lint)

#### [NEW] [DirectComposeTestUsageDetector.kt](file:///D:/Android/Projects/UIAutomationEngine/engine-lint/src/main/java/com/sehmi/engine/lint/DirectComposeTestUsageDetector.kt)
Architectural rule enforcement for the engine DSL.

#### [NEW] [EngineIssueRegistry.kt](file:///D:/Android/Projects/UIAutomationEngine/engine-lint/src/main/java/com/sehmi/engine/lint/EngineIssueRegistry.kt)
Registry for custom lint issues.

---

### Module `:app` (com.sehmi.app)

#### [NEW] [MainActivity.kt](file:///D:/Android/Projects/UIAutomationEngine/app/src/main/java/com/sehmi/app/MainActivity.kt)
#### [NEW] [AppNavigation.kt](file:///D:/Android/Projects/UIAutomationEngine/app/src/main/java/com/sehmi/app/AppNavigation.kt)
Sample application UI and navigation.

#### [NEW] Screens (Gesture, Form, Scroll, State)
Test screens for framework validation.

#### [NEW] [LoginRobot.kt](file:///D:/Android/Projects/UIAutomationEngine/app/src/androidTest/java/com/sehmi/app/robots/LoginRobot.kt)
#### [NEW] [EngineTest.kt](file:///D:/Android/Projects/UIAutomationEngine/app/src/androidTest/java/com/sehmi/app/EngineTest.kt)
Instrumented UI tests.

## Verification Plan

### Automated Tests
- Run `:engine-lint:test` to verify detector logic.
- Run `:app:connectedDebugAndroidTest` to verify the framework actions against the sample app.

### Manual Verification
- Verify that lint errors appear in Android Studio when using raw `onNodeWithTag` in the app module.
- Inspect Logcat for unmerged tree dumps on failures.
