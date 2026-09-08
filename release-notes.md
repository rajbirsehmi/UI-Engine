# Release Notes - 0.2.2-alpha

## [0.2.2-alpha] - 2026-11-06

### Added
- **Documentation Support**: Integrated Dokka to generate Javadoc and Sources JARs, enabling inline documentation and source navigation in consuming applications.

### Fixed
- **CI Build Stability**: Resolved recurring `Unresolved reference` and `@Composable` context errors in CI environments by consolidating Rule logic and refining dependency scoping.
- **Package Resolution**: Eliminated package-level ambiguity by moving test rules directly into the `UiEngine` entry point.

### Changed
- **API Simplification**: Replaced `createUiAutomationRule()` with `UiEngine.createRule()` to streamline test setup.
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
