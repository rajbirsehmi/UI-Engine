# Release Notes - v0.1.0-alpha

## [0.1.0-alpha] - 2026-09-02

### Added
- **Hilt Support**: Integrated Hilt Dependency Injection support for testing.
- **Product Flavors**: Added `di` flavor dimension with `standard` and `hilt` flavors.
- **Variant-Aware Publishing**: Configured Maven publishing to be variant-aware. Gradle now automatically resolves the correct flavor (Hilt or Standard) based on the consumer's project configuration.
- **KSP Integration**: Added Kotlin Symbol Processing (KSP) support for faster and more efficient code generation in the Hilt flavor.
- **Library Guardrails**: Implemented `resourcePrefix = "engine_"` to prevent resource name collisions when integrated into host applications.

### Changed
- **Version Bump**: Official release of the `0.1.0-alpha` series.
- **Dependency Optimization**: Cleaned up the Version Catalog (`libs.versions.toml`). Removed several unused transitive dependencies (including `appcompat`, `material`, and legacy test runners) to keep the library footprint minimal.
- **Enhanced Robustness**: System actions (`pressBack`, `pressHome`) now include automatic `waitForIdle()` synchronization to ensure the UI is stable before and after system interrupts.
- **Improved Logging**: Migrated internal logging to SLF4J-style placeholders (`{}`) for better performance and consistency across the engine.
- **Hilt Upgrade**: Updated Hilt to version `2.60.1` for maximum compatibility with the latest Android Gradle Plugin (AGP 8.x+).

### Fixed
- **Code Quality**: Resolved numerous Kotlin warnings, including unused parameters in catch blocks and missing trailing commas.
- **Plugin Resolution**: Fixed timing issues in the build script where the Hilt plugin was applied before the Android extension was fully initialized.
- **Publishing Logic**: Resolved `SoftwareComponent` naming conflicts that occurred when introducing multiple build flavors.
- **Lint Guard**: Fixed a warning in the custom `:engine-lint` module to ensure static analysis is clean and reliable.
