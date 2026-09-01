# Fix Kotlin Extension Conflict in engine module

The project sync fails with `Cannot add extension with name 'kotlin', as there is an extension already registered with that name`. This typically occurs due to duplicate plugin application or inconsistencies between plugin resolution methods (alias vs id).

## Proposed Changes

### Root Project
#### [MODIFY] [build.gradle.kts](file:///D:/Android/Projects/UIAutomationEngine/build.gradle.kts)
- Convert Kotlin plugin declarations to use `alias` for consistency with Android plugins.

### Engine Module
#### [MODIFY] [build.gradle.kts](file:///D:/Android/Projects/UIAutomationEngine/engine/build.gradle.kts)
- Convert all plugins to use `alias`.
- Remove or comment out the `:lint-rules` dependency since it is not included in `settings.gradle.kts`.
- Ensure `kotlinOptions` are correctly placed (they are currently inside `android {}`, which is correct, but let's double check).

## Verification Plan

### Automated Tests
- Run `gradle sync` to verify the extension conflict is resolved.
- Run `./gradlew :engine:assembleDebug` to ensure compilation works.
