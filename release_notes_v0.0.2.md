# 🤖 UI Automation Engine v0.0.2-alpha

This release introduces "Deep Interactions"—a suite of advanced capabilities covering accessibility compliance, complex multi-touch gestures, and deep system orchestration.

## 🚀 Key Features

### ♿ Accessibility First
Testing for inclusive design is now a first-class citizen in the engine:
*   **TalkBack Simulation**: `navigateByAccessibility(direction)` uses system-level traversal to move focus exactly like a screen reader.
*   **Focus Flow Validation**: `assertFocusOrder(tags)` ensures your UI is logically navigable for vision-impaired users.
*   **Automated Audits**: `assertInteractiveNodesHaveLabels()` instantly identifies clickable elements missing accessibility labels.

### 👆 Advanced Multi-Touch Gestures
Expand your testing beyond simple taps and swipes:
*   **Orbital Rotation**: `rotate(degrees)` simulates two-finger rotation for maps, 3D viewers, and photo editors.
*   **Multi-Finger Patterns**: `multiFingerSwipe(fingers, direction)` supports 2, 3, and 4-finger gestures for professional app navigation.

### 📱 System Orchestration
Deep integration with the Android OS via UI Automator:
*   **Notifications**: `openNotificationShade()` and `clickNotification(text)` allow testing of deep-links and system-level alerts.
*   **Quick Settings**: `toggleQuickSetting(name)` can interact with system tiles like Dark Mode or Wi-Fi.

## 🛠 Improvements & Fixes
*   **CI Stability**: GitHub Actions now leverage `macos-latest` runners for hardware-accelerated emulator testing.
*   **Deterministic Focus**: `requestFocus` now reliably seeds focus into Compose containers for traversal tests.
*   **Rich Diagnostics**: Failure reports now include direct references to the `FAILURE_<timestamp>.png` and the semantics tree dump in Logcat.

## 📚 Documentation
*   Full Javadoc refresh for all high-level actions.
*   Updated README with detailed interaction tables.

---
**Full Changelog**: [v0.0.1-alpha...v0.0.2-alpha](https://github.com/rajbirsehmi/UI-Engine/compare/v0.0.1-alpha...v0.0.2-alpha)
