# UI Test Automation Framework Implementation Plan

Build a production-grade Jetpack Compose UI test automation framework (`:engine`), custom lint enforcement (`:engine-lint`), and a sample application (`:app`).

## User Review Required

> [!IMPORTANT]
> - The framework relies strictly on Jetpack Compose and UIAutomator. Legacy Espresso dependencies are explicitly avoided.
> - The Lint detector will enforce usage of the engine's DSL across the project, but will allow raw calls within `com.sehmi.engine` (including `.advanced`).

## Proposed Changes

### Module `:engine` (Base: `com.sehmi.engine`)
Consolidate and enhance the core DSL and utilities for robust UI testing.

#### [MODIFY] [GestureActions.kt](file:///D:/Android/Projects/UIAutomationEngine/engine/src/main/java/com/sehmi/engine/actions/GestureActions.kt)
- Add `pinchToZoom` extension.
- Incorporate `clickOnTag`, `clickOnText` from `ClickActions.kt`.
- Add `longClickText`.
- Ensure all actions use `runRobustly` and `waitUntil`.

#### [DELETE] [ClickActions.kt](file:///D:/Android/Projects/UIAutomationEngine/engine/src/main/java/com/sehmi/engine/actions/ClickActions.kt)
- Redundant after consolidation into `GestureActions.kt`.

#### [MODIFY] [SemanticsMatchers.kt](file:///D:/Android/Projects/UIAutomationEngine/engine/src/main/java/com/sehmi/engine/matchers/SemanticsMatchers.kt)
- Add specialized matchers for `Role` (Button, Checkbox, Switch, Tab).

#### [MODIFY] [AdvancedEngineActions.kt](file:///D:/Android/Projects/UIAutomationEngine/engine/src/main/java/com/sehmi/engine/advanced/AdvancedEngineActions.kt)
- Ensure all DSL methods (`gesture`, `keySequence`, `semantics`, `rawNodeInteraction`) correctly handle the optional `tag` parameter.

---

### Module `:engine-lint`
Enhance lint rules to provide QuickFixes and enforce architectural directives.

#### [MODIFY] [DirectComposeTestUsageDetector.kt](file:///D:/Android/Projects/UIAutomationEngine/engine-lint/src/main/java/com/sehmi/engine/lint/DirectComposeTestUsageDetector.kt)
- Implement `LintFix` to automatically convert raw Compose test calls to engine DSL extensions.

---

### Module `:app` (Base: `com.sehmi.app`)
Create a comprehensive sample app to exercise the automation engine.

#### [NEW] [MainActivity.kt](file:///D:/Android/Projects/UIAutomationEngine/app/src/main/java/com/sehmi/app/MainActivity.kt)
- Entry point with Hilt `@AndroidEntryPoint`.

#### [NEW] [AppNavigation.kt](file:///D:/Android/Projects/UIAutomationEngine/app/src/main/java/com/sehmi/app/AppNavigation.kt)
- Compose Navigation setup.

#### [NEW] Screens: [GestureTestScreen](file:///D:/Android/Projects/UIAutomationEngine/app/src/main/java/com/sehmi/app/screens/GestureTestScreen.kt), [FormTestScreen](file:///D:/Android/Projects/UIAutomationEngine/app/src/main/java/com/sehmi/app/screens/FormTestScreen.kt), [ScrollTestScreen](file:///D:/Android/Projects/UIAutomationEngine/app/src/main/java/com/sehmi/app/screens/ScrollTestScreen.kt), [StateTestScreen](file:///D:/Android/Projects/UIAutomationEngine/app/src/main/java/com/sehmi/app/screens/StateTestScreen.kt)
- Specialized screens with explicit `testTag` attributes.

#### [NEW] Instrumented Tests (in `src/androidTest/`): [LoginRobot.kt](file:///D:/Android/Projects/UIAutomationEngine/app/src/androidTest/java/com/sehmi/app/robots/LoginRobot.kt), [AppAutomationTests.kt](file:///D:/Android/Projects/UIAutomationEngine/app/src/androidTest/java/com/sehmi/app/AppAutomationTests.kt)
- Robots and test cases demonstrating both high-level DSL and `executeAdvancedAction`.

## Verification Plan

### Automated Tests
- Run unit tests for `:engine-lint` to verify detector and QuickFixes.
- Run instrumented tests in `:app` using `./gradlew :app:connectedAndroidTest`.

### Manual Verification
- Verify diagnostics (Logcat dumps and screenshots) are correctly generated on test failures.
