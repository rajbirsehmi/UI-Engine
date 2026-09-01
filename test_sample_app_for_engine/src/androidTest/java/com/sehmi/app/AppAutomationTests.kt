package com.sehmi.app

import androidx.compose.ui.test.doubleClick
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.swipeUp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sehmi.app.robots.FormRobot
import com.sehmi.engine.actions.clickOnTag
import com.sehmi.engine.actions.pressBack
import com.sehmi.engine.actions.pressHome
import com.sehmi.engine.actions.scrollToTag
import com.sehmi.engine.advanced.executeAdvancedAction
import com.sehmi.engine.core.ComposeRuleScope
import com.sehmi.engine.core.withRobot
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Production-grade instrumented UI tests for the :test_sample_app_for_engine module.
 * Demonstrates the use of the Automation Engine DSL, Robots, and Advanced Actions.
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class AppAutomationTests : ComposeRuleScope {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule(MainActivity::class.java)

    override val composeRule get() = composeTestRule

    @Test
    fun testLoginForm() {
        clickOnTag("nav_form")
        composeTestRule.withRobot(FormRobot(composeTestRule)) {
            typeEmail("admin@example.com")
            typePassword("p@ssword123")
            clickSubmit()
            verifyStatus("Submitted: admin@example.com")
        }
    }

    @Test
    fun testScrollToBottom() {
        clickOnTag("nav_scroll")
        scrollToTag("item_99")
    }

    @Test
    fun testAdvancedGesture() {
        clickOnTag("nav_gestures")
        executeAdvancedAction("gesture_box") {
            gesture {
                swipeUp()
                doubleClick()
            }
        }
    }

    @Test
    fun testSystemActions() {
        pressHome()
        pressBack()
    }
}
