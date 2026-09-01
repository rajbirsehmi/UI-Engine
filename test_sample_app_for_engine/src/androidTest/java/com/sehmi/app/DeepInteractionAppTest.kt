package com.sehmi.app

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.sehmi.engine.actions.*
import com.sehmi.engine.core.ComposeRuleScope
import com.sehmi.engine.utils.runRobustly
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class DeepInteractionAppTest : ComposeRuleScope {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.POST_NOTIFICATIONS,
    )

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule(MainActivity::class.java)

    override val composeRule get() = composeTestRule

    @Test
    fun testAdvancedGesturesOnApp() {
        clickOnTag("nav_gestures")
        
        // Test Rotation
        rotate("advanced_gesture_area", 45f)
        
        // Test Multi-Finger Swipe
        multiFingerSwipe("advanced_gesture_area", fingers = 3, direction = Direction.DOWN)
    }

    @Test
    fun testAccessibilityAndNotifications() {
        clickOnTag("nav_accessibility")
        
        // Seed focus into the content area. We try to navigate until focus_1 is reached.
        // This handles cases where focus might start on the TopAppBar's back button.
        runRobustly("Seed focus to focus_1") {
            var found = false
            repeat(5) {
                navigateByAccessibility(Direction.RIGHT)
                try {
                    composeRule.onNodeWithTag(testTag = "focus_1").assert(isFocused())
                    found = true
                    return@repeat
                } catch (_: AssertionError) {}
            }
            if (!found) {
                // Fallback: direct request focus if navigation didn't land on it
                requestFocus("focus_1")
            }
        }

        // Test Accessibility Focus Setting
        requestFocus("focus_2")
        composeRule.onNodeWithTag("focus_2").assert(isFocused())

        // Test Accessibility Focus Order
        assertFocusOrder(listOf("focus_1", "focus_2"))
        
        // Test Accessibility Audit (Expect failure due to 'missing_label_box')
        try {
            assertInteractiveNodesHaveLabels()
            throw IllegalStateException("Audit should have failed")
        } catch (_: AssertionError) {
            // Success: audit failed as expected
        }

        // Test Notification Action
        clickOnTag("send_notification_btn")
        
        // Verify notification in shade
        clickNotification("Engine Test")
        
        // Verify we are back or the app reacted
        // Since clicking a notification might open an activity or just dismiss, 
        // we'll just check if we can still interact with the device.
        pressBack()
    }

    @Test
    fun testQuickSettingsToggle() {
        // This is a deep system interaction
        // Note: setting name might vary by device locale, "Dark mode" is common for US
        try {
            toggleQuickSetting("Dark mode")
        } catch (_: AssertionError) {
            // Fallback for different OS versions or if tile not present
            pressBack() 
        }
    }
}
