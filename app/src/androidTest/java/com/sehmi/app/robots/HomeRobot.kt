package com.sehmi.app.robots

import androidx.compose.ui.test.junit4.ComposeTestRule
import com.sehmi.engine.actions.clickOnTag
import com.sehmi.engine.core.ComposeRuleScope

/**
 * Robot for navigating through the app's home screen.
 */
class HomeRobot(override val composeRule: ComposeTestRule) : ComposeRuleScope {

    fun navigateToGestures() {
        clickOnTag("nav_gestures")
    }

    fun navigateToForm() {
        clickOnTag("nav_form")
    }

    fun navigateToScroll() {
        clickOnTag("nav_scroll")
    }

    fun navigateToState() {
        clickOnTag("nav_state")
    }
}
