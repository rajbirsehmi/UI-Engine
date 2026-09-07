# Release Notes - 0.2.1-alpha

## [0.2.1-alpha] - 2026-11-05

### Added
- **Dedicated Rules Package**: Introduced `com.sehmi.engine.rules` package for all JUnit-related infrastructure, including `UiEngineRule` and the `createUiAutomationRule` factory.

### Fixed
- **API Visibility**: Resolved `Unresolved Reference` errors in consuming applications by consolidating the `UiEngine` entry point and aligning the package structure with the documentation.
- **Lint Rule Updates**: Updated `UiEngineSetupDetector` to support the new package structure, ensuring accurate static analysis.

### Changed
- **Version Bump**: Updated all components to `0.2.1-alpha`.
- **Package Consolidation**: Merged `UiEngine` implementations from the `core` package into the top-level `com.sehmi.engine` package for better discoverability.
