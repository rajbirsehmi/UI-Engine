package com.sehmi.engine

import androidx.compose.ui.test.junit4.ComposeTestRule
import com.sehmi.engine.core.ComposeRuleScope
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

/**
 * Global configuration and entry point for the UI Automation Engine.
 *
 * This object allows host applications to configure engine-wide settings such as 
 * default timeouts and logging levels, and manages the lifecycle of the [ComposeTestRule].
 */
object UiEngine {
    private val logger: Logger = LogManager.getLogger("UiEngine")
    private val rule = ThreadLocal<ComposeTestRule>()

    /**
     * Configuration settings for the engine.
     */
    data class Configuration(
        /** The default timeout for robust actions in milliseconds. */
        val defaultTimeoutMillis: Long = 5000L,
        /** The polling interval for wait operations in milliseconds. */
        val pollIntervalMillis: Long = 100L,
        /** Whether to automatically capture screenshots on failure. */
        val autoCaptureScreenshots: Boolean = true,
        /** Whether to automatically dump the semantics tree on failure. */
        val autoDumpSemantics: Boolean = true,
    )

    private var _config: Configuration = Configuration()

    /**
     * The current engine configuration.
     */
    val config: Configuration get() = _config

    /**
     * The current [ComposeTestRule] managed by the engine.
     *
     * @throws IllegalStateException if the rule has not been set.
     */
    val composeRule: ComposeTestRule
        get() = rule.get() ?: throw IllegalStateException(
            "ComposeTestRule is not set in UiEngine. " +
            "Ensure you are using createUiAutomationRule() or have registered " +
            "UiEngineRule in your test class."
        )

    /**
     * Initializes the UI Engine with custom configuration.
     *
     * While the engine is largely stateless, calling this method allows for 
     * customizing global behavior before running tests.
     *
     * @param configuration The custom [Configuration] to apply.
     */
    fun configure(configuration: Configuration) {
        logger.info("Configuring UI Engine: $configuration")
        _config = configuration
    }

    /**
     * Sets the [ComposeTestRule] for the current test thread.
     * 
     * Internal use only via JUnit Rules.
     */
    fun setComposeRule(composeTestRule: ComposeTestRule) {
        rule.set(composeTestRule)
    }

    /**
     * Clears the [ComposeTestRule] from the engine.
     *
     * Internal use only via JUnit Rules.
     */
    fun clearComposeRule() {
        rule.remove()
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
