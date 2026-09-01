package com.sehmi.engine.hilt

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import dagger.hilt.android.testing.HiltAndroidRule
import org.junit.rules.RuleChain
import org.junit.rules.TestRule

/**
 * Factory function to create a [TestRule] that chains [HiltAndroidRule] with 
 * [createAndroidComposeRule].
 *
 * This ensures that Hilt injection is properly initialized before the Compose 
 * test rule starts the activity, which is essential for testing Hilt-enabled 
 * applications.
 *
 * @param A The type of the activity to be launched.
 * @param hiltRule The instance of [HiltAndroidRule] for the current test.
 * @param activityClass The Java class of the activity to launch.
 * @return A combined [TestRule] that manages both Hilt and Compose lifecycles.
 */
fun <A : ComponentActivity> createHiltComposeRule(
    hiltRule: HiltAndroidRule,
    activityClass: Class<A>
): TestRule {
    return RuleChain.outerRule(hiltRule)
        .around(createAndroidComposeRule(activityClass))
}
