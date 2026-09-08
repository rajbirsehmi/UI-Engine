# Release Notes - 0.2.5-alpha

## [0.2.5-alpha] - 2026-11-06

### Fixed
- **Multi-module JitPack Publication**: Resolved critical issue where the main `robot-testing-engine` artifact was not downloadable from JitPack. Added `jitpack.yml` to support proper multi-module resolution for flavored Android libraries.
- **Unified Publication Scope**: Standardized `groupId` to `com.sehmi.engine` across all modules to ensure consistent dependency resolution.

### Changed
- **Version Bump**: Updated all components to `0.2.5-alpha`.

## [0.2.4-alpha] - 2026-11-06

### Fixed
- **JitPack Publication**: Added `jitpack.yml` and unified Group IDs to `com.sehmi.engine` to ensure reliable artifact resolution for multi-module Android projects on JitPack.
- **Hilt Dependency Scoping**: Fixed Hilt dependency scoping in the engine AAR by promoting Hilt libraries to `api` in the Hilt flavor, ensuring they are correctly exposed to consuming applications.
- **Artifact Resolution**: Resolved issues where `0.2.3-alpha` was not found by aligning Version Catalog (`libs.versions.toml`) with the published AAR classifiers.
- **README Correction**: Updated installation instructions to correctly reflect the `artifact` block requirement for Hilt-based projects.

### Changed
- **Publication Refactoring**: Refined publication details to use the `com.sehmi.engine` group ID and descriptive artifact names (`robot-testing-engine`, `engine-lint`) for better clarity.
- **Version Bump**: Updated all components to `0.2.4-alpha`.

## [0.2.3-alpha] - 2026-11-06

### Added
- **Hilt Delegate Support**: Introduced a new property delegate for `UiEngine.getTestEntryPoint()`, allowing for a cleaner `by` syntax in Robots.
- **Improved Hilt Documentation**: Updated `README.md` and `ENGINE_REFERENCE.md` with comprehensive code snippets for Hilt-enabled UI tests.

### Changed
- **Hilt API Consolidation**: Refactored Hilt automation logic into `UiEngine` extensions, ensuring a consistent entry point across all library flavors.
- **Hilt Package Refactoring**: Moved Hilt-specific automation classes to the base `com.sehmi.engine` package (within the Hilt source set) to simplify imports.
- **Version Bump**: Updated all components to `0.2.3-alpha`.

## [0.2.2-alpha] - 2026-11-06

### Added
- **Documentation Support**: Integrated Dokka to generate Javadoc and Sources JARs, enabling inline documentation and source navigation in consuming applications.

### Fixed
- **CI Build Stability**: Resolved recurring `Unresolved reference` and `@Composable` context errors in CI environments by consolidating Rule logic and refining dependency scoping.
- **Package Resolution**: Eliminated package-level ambiguity by moving test rules directly into the `UiEngine` entry point.

### Changed
- **API Simplification**: Replaced standalone rule factories with `UiEngine` extensions:
    - `UiEngine.createRule()` for standard tests.
    - `UiEngine.createHiltRule(Activity::class.java)` for Hilt tests.
- **Hilt Entry Points**: Introduced `UiEngine.getTestEntryPoint()` delegate for easier dependency access in Robots.
- **Dependency Scoping**: Promoted core test dependencies to `api` in the engine module to ensure consistent visibility for host applications.
- **Version Bump**: Updated all components to `0.2.2-alpha`.

## [0.2.1-alpha] - 2026-11-05

### Added
- **Dedicated Rules Package**: Introduced `com.sehmi.engine.junit` package for all JUnit-related infrastructure, including `UiEngineRule` and the `createUiAutomationRule` factory.

### Fixed
- **API Visibility**: Resolved `Unresolved Reference` errors in consuming applications by consolidating the `UiEngine` entry point and aligning the package structure with the documentation.
- **Lint Rule Updates**: Updated `UiEngineSetupDetector` to support the new package structure, ensuring accurate static analysis.

### Changed
- **Version Bump**: Updated all components to `0.2.1-alpha`.
- **Package Consolidation**: Merged `UiEngine` implementations from the `core` package into the top-level `com.sehmi.engine` package for better discoverability.
