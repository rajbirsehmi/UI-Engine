package com.sehmi.app.robots

import androidx.compose.ui.test.junit4.ComposeTestRule
import com.sehmi.engine.actions.clickOnTag
import com.sehmi.engine.actions.replaceText
import com.sehmi.engine.assertions.assertTextContains
import com.sehmi.engine.core.ComposeRuleScope

/**
 * Robot for Form screen interactions, replacing LoginRobot.
 */
class FormRobot(override val composeRule: ComposeTestRule) : ComposeRuleScope {

    fun typeEmail(email: String) {
        replaceText("email_field", email)
    }

    fun typePassword(password: String) {
        replaceText("password_field", password)
    }

    fun typeSearch(query: String) {
        replaceText("search_field", query)
    }

    fun clickSubmit() {
        clickOnTag("submit_button")
    }

    fun verifyStatus(expectedSubstring: String) {
        assertTextContains("status_text", expectedSubstring)
    }
}
