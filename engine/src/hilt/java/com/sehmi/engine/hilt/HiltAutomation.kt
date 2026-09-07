package com.sehmi.engine.hilt

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import com.sehmi.engine.UiEngine
import dagger.hilt.EntryPoints
import dagger.hilt.android.testing.HiltAndroidRule
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * A [ComposeTestRule] wrapper that integrates with Hilt and the UI Automation Engine.
 */
class HiltAutomationComposeTestRule<A : ComponentActivity>(
    private val hiltRule: HiltAndroidRule,
    private val composeRule: AndroidComposeTestRule<*, A>
) : ComposeTestRule by composeRule {

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                // The order is critical: Hilt must be ready, then Engine registers the rule.
                UiEngine.setComposeRule(composeRule)
                try {
                    // We wrap the execution to ensure Hilt and Compose rules run in order
                    composeRule.apply(base, description).evaluate()
                } finally {
                    UiEngine.clearComposeRule()
                }
            }
        }
    }
}

/**
 * Creates a [ComposeTestRule] that integrates Hilt injection with the UI Automation Engine.
 *
 * This helper ensures that Hilt is properly initialized before the UI interaction starts
 * and that the [UiEngine] is aware of the test rule.
 *
 * @param hiltRule The [HiltAndroidRule] for the current test.
 * @param activityClass The Activity class to launch for the test.
 */
fun <A : ComponentActivity> createHiltUiAutomationRule(
    hiltRule: HiltAndroidRule,
    activityClass: Class<A>
): ComposeTestRule {
    val composeRule = createAndroidComposeRule<A>(activityClass)
    return HiltAutomationComposeTestRule<A>(hiltRule, composeRule)
}

/**
 * Provides access to Hilt-injected singletons (entry points) within a test or robot.
 *
 * This is useful for accessing internal components like Repositories or Managers 
 * without needing constructor injection in your Robots.
 */
inline fun <reified T> getTestEntryPoint(): T {
    val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
    return EntryPoints.get(context, T::class.java)
}
