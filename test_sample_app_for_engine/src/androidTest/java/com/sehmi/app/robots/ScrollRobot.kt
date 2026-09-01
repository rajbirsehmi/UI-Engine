package com.sehmi.app.robots

import androidx.compose.ui.test.junit4.ComposeTestRule
import com.sehmi.engine.actions.scrollToIndex
import com.sehmi.engine.assertions.assertTagDisplayed
import com.sehmi.engine.core.ComposeRuleScope

/**
 * Robot for Scroll screen interactions.
 */
class ScrollRobot(override val composeRule: ComposeTestRule) : ComposeRuleScope {

    fun scrollToItem(index: Int) {
        scrollToIndex("scroll_column", index)
    }

    fun verifyItemVisible(index: Int) {
        assertTagDisplayed("item_$index")
    }
}
