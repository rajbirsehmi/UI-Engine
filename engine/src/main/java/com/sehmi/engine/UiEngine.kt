package com.sehmi.engine

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

/**
 * Global configuration and entry point for the UI Automation Engine.
 *
 * This object allows host applications to configure engine-wide settings such as 
 * default timeouts and logging levels.
 */
object UiEngine {
    private val logger: Logger = LogManager.getLogger("UiEngine")

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
}
