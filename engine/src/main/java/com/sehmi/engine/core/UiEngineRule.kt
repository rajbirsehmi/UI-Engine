package com.sehmi.engine.core

import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.v2.createComposeRule
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * A JUnit Rule that automatically registers the [ComposeTestRule] with [UiEngine].
 *
 * This rule simplifies test setup by handling the lifecycle of the engine's 
 * global [ComposeTestRule] reference. It ensures that the rule is set before each test 
 * starts and cleared after each test finishes.
 *
 * Example usage:
 * ```
 * @get:Rule
 * val composeRule = createComposeRule()
 *
 * @get:Rule
 * val engineRule = UiEngineRule(composeRule)
 * ```
 *
 * @property composeTestRule The [ComposeTestRule] to be managed by the engine.
 */
class UiEngineRule(private val composeTestRule: ComposeTestRule) : TestWatcher() {
    override fun starting(description: Description) {
        UiEngine.setComposeRule(composeTestRule)
    }

    override fun finished(description: Description) {
        UiEngine.clearComposeRule()
    }
}

/**
 * A [ComposeContentTestRule] wrapper that automatically registers itself with [UiEngine].
 *
 * This allows for a single-rule setup in your tests.
 */
internal class AutomationComposeContentTestRule(
    private val baseRule: ComposeContentTestRule
) : ComposeContentTestRule by baseRule {
    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                UiEngine.setComposeRule(baseRule)
                try {
                    baseRule.apply(base, description).evaluate()
                } finally {
                    UiEngine.clearComposeRule()
                }
            }
        }
    }
}

/**
 * Creates a [ComposeContentTestRule] that is automatically registered with the [UiEngine].
 *
 * This is the recommended way to initialize the UI Automation Engine in your tests, 
 * as it eliminates the need for a separate [UiEngineRule] or manual setup.
 *
 * Example:
 * ```
 * @get:Rule
 * val rule = createUiAutomationRule()
 *
 * @Test
 * fun myTest() {
 *     UiEngine.withRobot(MyRobot()) { /* robot logic */ }
 * }
 * ```
 */
fun createUiAutomationRule(): ComposeContentTestRule {
    return AutomationComposeContentTestRule(createComposeRule())
}
