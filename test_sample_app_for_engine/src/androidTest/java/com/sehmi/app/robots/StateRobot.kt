package com.sehmi.app.robots

import androidx.compose.ui.test.junit4.ComposeTestRule
import com.sehmi.engine.actions.clickOnTag
import com.sehmi.engine.assertions.assertIsDisabled
import com.sehmi.engine.assertions.assertIsOff
import com.sehmi.engine.assertions.assertIsOn
import com.sehmi.engine.assertions.assertIsSelected
import com.sehmi.engine.core.ComposeRuleScope

/**
 * Robot for State and Dialog screen interactions.
 */
class StateRobot(override val composeRule: ComposeTestRule) : ComposeRuleScope {

    fun toggleCheckbox() {
        clickOnTag("checkbox", useUnmergedTree = true)
    }

    fun toggleSwitch() {
        clickOnTag("switch", useUnmergedTree = true)
    }

    fun selectRadio(option: String) {
        clickOnTag("radio_$option", useUnmergedTree = true)
    }

    fun clickDialogButton() {
        clickOnTag("dialog_button")
    }

    fun confirmDialog() {
        clickOnTag("dialog_confirm")
    }

    fun assertCheckboxOn() {
        assertIsOn("checkbox", useUnmergedTree = true)
    }

    fun assertCheckboxOff() {
        assertIsOff("checkbox", useUnmergedTree = true)
    }

    fun assertSwitchOn() {
        assertIsOn("switch", useUnmergedTree = true)
    }

    fun assertSwitchOff() {
        assertIsOff("switch", useUnmergedTree = true)
    }

    fun assertRadioSelected(option: String) {
        assertIsSelected("radio_$option", useUnmergedTree = true)
    }

    fun assertDisabledButtonIsDisabled() {
        assertIsDisabled("disabled_button")
    }
}
