package com.sehmi.engine

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.EntryPoints
import dagger.hilt.android.testing.HiltAndroidRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import kotlin.reflect.KProperty

/**
 * A [ComposeTestRule] wrapper that integrates with Hilt and the UI Automation Engine.
 */
class HiltAutomationComposeTestRule<A : ComponentActivity>(
    private val composeRule: AndroidComposeTestRule<*, A>
) : ComposeTestRule by composeRule {

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                UiEngine.setComposeRule(composeRule)
                try {
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
 * @param activityClass The Activity class to launch for the test.
 */
fun <A : ComponentActivity> UiEngine.createHiltRule(
    activityClass: Class<A>
): ComposeTestRule {
    val composeRule = createAndroidComposeRule<A>(activityClass)
    return HiltAutomationComposeTestRule<A>(composeRule)
}

/**
 * A delegate that provides access to Hilt-injected singletons (entry points) within a test or robot.
 */
class HiltEntryPointDelegate<T>(private val entryPointClass: Class<T>) {
    private var value: T? = null

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        if (value == null) {
            val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
            value = EntryPoints.get(context, entryPointClass)
        }
        return value!!
    }
}

/**
 * Provides access to Hilt-injected singletons (entry points) within a test or robot.
 *
 * Example:
 * ```
 * val viewModel: MyViewModel by UiEngine.getTestEntryPoint()
 * ```
 */
inline fun <reified T> UiEngine.getTestEntryPoint(): HiltEntryPointDelegate<T> {
    return HiltEntryPointDelegate(T::class.java)
}
