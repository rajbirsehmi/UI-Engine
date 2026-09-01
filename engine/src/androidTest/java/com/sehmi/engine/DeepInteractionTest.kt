package com.sehmi.engine

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sehmi.engine.actions.*
import com.sehmi.engine.core.ComposeRuleScope
import org.junit.Assert.assertThrows
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DeepInteractionTest : ComposeRuleScope {

    @get:Rule
    override val composeRule = createComposeRule()

    @Test
    fun testAccessibilityAuditFailsOnMissingLabels() {
        composeRule.setContent {
            Column(Modifier.testTag("root")) {
                Button(onClick = {}, modifier = Modifier.testTag("valid_button")) {
                    Text("Click Me")
                }
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clickable { }
                        .testTag("invalid_box")
                )
            }
        }

        assertThrows(AssertionError::class.java) {
            assertInteractiveNodesHaveLabels()
        }
    }

    @Test
    fun testRotationGestureCompilesAndRuns() {
        composeRule.setContent {
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .testTag("rotatable_box")
            )
        }

        // We can't easily verify the physical rotation in a unit test without 
        // complex state tracking, but we verify it doesn't crash the engine.
        rotate("rotatable_box", 90f)
    }

    @Test
    fun testMultiFingerSwipeCompilesAndRuns() {
        composeRule.setContent {
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .testTag("swipe_box")
            )
        }

        multiFingerSwipe("swipe_box", fingers = 3, direction = Direction.UP)
    }

    @Test
    fun testSystemActionsContextSafety() {
        // Verifies that UI Automator actions don't crash when invoked
        // Note: These might do nothing or fail if not on a real device/emulator 
        // with specific system state, but we ensure the plumbing works.
        pressHome()
        openNotificationShade()
        pressBack()
    }
}
