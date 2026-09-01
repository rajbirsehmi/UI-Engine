package com.sehmi.app

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sehmi.app.robots.*
import com.sehmi.engine.actions.advanceTime
import com.sehmi.engine.actions.advanceTimeUntil
import com.sehmi.engine.core.withRobot
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class AllScreensUiTests {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule(MainActivity::class.java)

    @Test
    fun testGestureInteractions() {
        composeTestRule.withRobot(HomeRobot(composeTestRule)) {
            navigateToGestures()
        }

        composeTestRule.withRobot(GestureRobot(composeTestRule)) {
            verifyStatus("Interact with the box")
            
            tapBox()
            advanceTime(1000)// this is a quick fix.
            verifyStatus("Single Tapped")
            advanceTime(1000)

            doubleTapBox()
            verifyStatus("Double Tapped")
            advanceTime(1000)
            
            longPressBox()
            verifyStatus("Long Pressed")
            advanceTime(1000)
            
            // Slider range is now 0-100, set to 82%
            setSliderValue(0.8f)
            verifySliderStatus("Slider Control: 82%")
            advanceTime(1000)
        }
    }

    @Test
    fun testFormSubmission() {
        composeTestRule.withRobot(HomeRobot(composeTestRule)) {
            navigateToForm()
        }

        composeTestRule.withRobot(FormRobot(composeTestRule)) {
            typeEmail("test@example.com")
            typePassword("password123")
            typeSearch("UI Automation")
            clickSubmit()
            verifyStatus("Submitted: test@example.com")
        }
    }

    @Test
    fun testListScrolling() {
        composeTestRule.withRobot(HomeRobot(composeTestRule)) {
            navigateToScroll()
        }

        composeTestRule.withRobot(ScrollRobot(composeTestRule)) {
            scrollToItem(50)
            verifyItemVisible(50)
            
            scrollToItem(99)
            verifyItemVisible(99)
        }
    }

    @Test
    fun testStateAndDialogs() {
        composeTestRule.withRobot(HomeRobot(composeTestRule)) {
            navigateToState()
        }

        composeTestRule.withRobot(StateRobot(composeTestRule)) {
            assertCheckboxOff()
            toggleCheckbox()
            assertCheckboxOn()
            
            assertSwitchOff()
            toggleSwitch()
            assertSwitchOn()
            
            assertRadioSelected("A")
            selectRadio("B")
            assertRadioSelected("B")
            selectRadio("C")
            assertRadioSelected("C")
            
            assertDisabledButtonIsDisabled()
            
            clickDialogButton()
            confirmDialog()
        }
    }
}
