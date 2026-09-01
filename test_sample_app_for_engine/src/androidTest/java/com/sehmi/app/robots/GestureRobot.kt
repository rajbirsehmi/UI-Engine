package com.sehmi.app.robots

import androidx.compose.ui.test.junit4.ComposeTestRule
import com.sehmi.engine.actions.clickAtOffset
import com.sehmi.engine.actions.clickOnTag
import com.sehmi.engine.actions.doubleClickTag
import com.sehmi.engine.actions.longClickTag
import com.sehmi.engine.assertions.assertTextEquals
import com.sehmi.engine.core.ComposeRuleScope

/**
 * Robot for Gesture screen interactions.
 */
class GestureRobot(override val composeRule: ComposeTestRule) : ComposeRuleScope {

    fun tapBox() {
        clickOnTag("gesture_box")
    }

    fun doubleTapBox() {
        doubleClickTag("gesture_box")
    }

    fun longPressBox() {
        longClickTag("gesture_box")
    }

    /**
     * Sets the slider value by clicking at a specific offset.
     * @param percentage The percentage across the slider (0.0 to 1.0).
     */
    fun setSliderValue(percentage: Float) {
        clickAtOffset("slider", xPercentage = percentage, yPercentage = 0.5f)
    }

    fun verifyStatus(text: String) {
        assertTextEquals("gesture_status", text)
    }

    fun verifySliderStatus(text: String) {
        assertTextEquals("slider_status", text)
    }
}
