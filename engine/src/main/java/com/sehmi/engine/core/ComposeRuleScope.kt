package com.sehmi.engine.core

import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.v2.createComposeRule

/**
 * Global entry point for the UI Automation Engine.
 *
 * This object allows for a centralized management of the [ComposeTestRule],
 * enabling robots to be instantiated without explicitly passing the rule in their constructors.
 */
object UiEngine {
    private val rule = ThreadLocal<ComposeTestRule>()

    /**
     * The current [ComposeTestRule] managed by the engine.
     *
     * @throws IllegalStateException if the rule has not been set.
     */
    val composeRule: ComposeTestRule
        get() = rule.get() ?: throw IllegalStateException(
            "ComposeTestRule is not set in UiEngine. " +
            "Ensure you call UiEngine.setComposeRule(composeTestRule) in your @Before setup method, " +
            "or use UiEngine.createRule() to initialize your test rule."
        )

    /**
     * Sets the [ComposeTestRule] for the current test thread.
     */
    fun setComposeRule(composeTestRule: ComposeTestRule) {
        rule.set(composeTestRule)
    }

    /**
     * Clears the [ComposeTestRule] from the engine.
     *
     * It is recommended to call this in your @After teardown method to prevent memory leaks 
     * or state pollution between tests.
     */
    fun clearComposeRule() {
        rule.remove()
    }

    /**
     * Creates a [ComposeContentTestRule] that is automatically registered with this [UiEngine].
     *
     * This eliminates the need for manual setup in your test classes.
     */
    fun createRule(): ComposeContentTestRule {
        return AutomationComposeContentTestRule(createComposeRule())
    }

    /**
     * DSL entry-point to execute actions and assertions on a robot.
     *
     * This version uses the rule stored in [UiEngine], allowing for a cleaner syntax
     * when the rule is set globally.
     *
     * @param T The type of the robot, which must implement [ComposeRuleScope].
     * @param robot The robot instance to execute the block on.
     * @param block The interaction block containing actions/assertions.
     */
    inline fun <T : ComposeRuleScope> withRobot(
        robot: T,
        crossinline block: T.() -> Unit
    ) {
        robot.block()
    }
}

/**
 * Represents a scope that provides access to the [ComposeTestRule].
 *
 * Robots or test classes should implement this interface to gain access to the engine's 
 * robust automation extensions for actions, assertions, and gestures.
 *
 * Implementing this scope allows for a clean DSL-like syntax when performing UI interactions 
 * within the Compose testing framework.
 *
 * @property composeRule The underlying [ComposeTestRule] used for UI interactions.
 */
interface ComposeRuleScope {
    /**
     * The [ComposeTestRule] used for UI interactions. 
     * Defaults to the rule managed by [UiEngine].
     */
    val composeRule: ComposeTestRule
        get() = UiEngine.composeRule
}

/**
 * DSL entry-point to execute actions and assertions on a robot within the context 
 * of a [ComposeTestRule].
 *
 * This function bridges the standard Compose test rule with the custom robot architecture, 
 * allowing developers to write tests that are more readable and maintainable.
 *
 * Example usage:
 * ```
 * composeTestRule.withRobot(LoginRobot()) {
 *     enterUsername("user")
 *     enterPassword("pass")
 *     clickLogin()
 * }
 * ```
 *
 * @param T The type of the robot, which must implement [ComposeRuleScope].
 * @param robot The robot instance to execute the block on.
 * @param block The interaction block containing actions/assertions.
 */
inline fun <T : ComposeRuleScope> ComposeTestRule.withRobot(
    robot: T,
    crossinline block: T.() -> Unit
) {
    robot.block()
}
