package com.sehmi.engine.actions

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performKeyInput
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performSemanticsAction
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.semantics.SemanticsActions
import com.sehmi.engine.core.ComposeRuleScope
import com.sehmi.engine.utils.runRobustly
import com.sehmi.engine.utils.waitUntil
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

private val logger: Logger = LogManager.getLogger("TextAndFocusActions")

/**
 * Robustly enters (appends) text into a node identified by its test tag.
 *
 * This action leverages the engine's robust action pipeline ([runRobustly]), which:
 * - Automatically scrolls to the target node.
 * - Explicitly waits until the node is displayed and ready for input.
 * - Handles potential input flakiness with retries.
 *
 * @param testTag The unique identifier for the text input field.
 * @param text The string to append to the current content of the field.
 * @param useUnmergedTree Whether to use the unmerged semantics tree for lookups.
 * @throws AssertionError if the node is not found or input fails.
 */
fun ComposeRuleScope.enterText(testTag: String, text: String, useUnmergedTree: Boolean = false) {
    logger.info("Starting enterText: text='$text', testTag=$testTag, useUnmergedTree=$useUnmergedTree")
    runRobustly("Enter text '$text' into tag: $testTag", testTag) {
        this.waitUntil {
            val node = composeRule.onNodeWithTag(testTag, useUnmergedTree)
            try {
                logger.debug("Attempting to scroll to tag: $testTag")
                node.performScrollTo()
            } catch (e: AssertionError) {}
            logger.debug("Waiting for tag $testTag to be displayed and performing text input")
            node.assertIsDisplayed()
                .performTextInput(text)
            composeRule.waitForIdle()
        }
    }
    logger.debug("enterText completed for tag: $testTag")
}

/**
 * Robustly replaces the current text in a node with new content.
 *
 * Unlike [enterText], this action clears the existing content before inserting 
 * the new text. It also verifies that the text was correctly updated.
 * Leverages the robust action pipeline ([runRobustly]).
 *
 * @param testTag The unique identifier for the text input field.
 * @param text The new string to set as the field's content.
 * @param useUnmergedTree Whether to use the unmerged semantics tree for lookups.
 * @throws AssertionError if the node is not found, input fails, or verification fails.
 */
fun ComposeRuleScope.replaceText(testTag: String, text: String, useUnmergedTree: Boolean = false) {
    logger.info("Starting replaceText: text='$text', testTag=$testTag, useUnmergedTree=$useUnmergedTree")
    runRobustly("Replace text with '$text' in tag: $testTag", testTag) {
        this.waitUntil {
            val node = composeRule.onNodeWithTag(testTag, useUnmergedTree)
            try {
                logger.debug("Attempting to scroll to tag: $testTag")
                node.performScrollTo()
            } catch (e: AssertionError) {}
            logger.debug("Performing text replacement in tag: $testTag")
            node.assertIsDisplayed()
                .performTextReplacement(text)
            
            composeRule.waitForIdle()
            logger.debug("Verifying text replacement in tag: $testTag")
            node.assertTextContains(text)
        }
    }
    logger.debug("replaceText completed for tag: $testTag")
}

/**
 * Robustly clears all text from a node identified by its test tag.
 *
 * Leverages the robust action pipeline ([runRobustly]) to ensure the node is 
 * visible and interactive before clearing.
 *
 * @param testTag The unique identifier for the UI element.
 * @param useUnmergedTree Whether to use the unmerged semantics tree for lookups.
 * @throws AssertionError if the node is not found or operation fails.
 */
fun ComposeRuleScope.clearText(testTag: String, useUnmergedTree: Boolean = false) {
    logger.info("Starting clearText: testTag=$testTag, useUnmergedTree=$useUnmergedTree")
    runRobustly("Clear text in tag: $testTag", testTag) {
        this.waitUntil {
            val interaction = composeRule.onNodeWithTag(testTag, useUnmergedTree)
            try {
                logger.debug("Attempting to scroll to tag: $testTag")
                interaction.performScrollTo()
            } catch (e: AssertionError) {}
            logger.debug("Performing text clearance in tag: $testTag")
            interaction.assertIsDisplayed()
                .performTextClearance()
            composeRule.waitForIdle()
        }
    }
    logger.debug("clearText completed for tag: $testTag")
}

/**
 * Robustly triggers the default IME (Soft Keyboard) action on a node.
 *
 * Common actions include "Done", "Search", "Next", or "Go".
 * Leverages the robust action pipeline ([runRobustly]).
 *
 * @param testTag The unique identifier for the UI element.
 * @param useUnmergedTree Whether to use the unmerged semantics tree for lookups.
 * @throws AssertionError if the node is not found or action fails.
 */
fun ComposeRuleScope.pressImeAction(testTag: String, useUnmergedTree: Boolean = false) {
    logger.info("Starting pressImeAction: testTag=$testTag, useUnmergedTree=$useUnmergedTree")
    runRobustly("Press IME action on tag: $testTag", testTag) {
        val interaction = composeRule.onNodeWithTag(testTag, useUnmergedTree)
        try {
            logger.debug("Attempting to scroll to tag: $testTag")
            interaction.performScrollTo()
        } catch (e: AssertionError) {}
        logger.debug("Performing IME action on tag: $testTag")
        interaction.performImeAction()
    }
    logger.debug("pressImeAction completed for tag: $testTag")
}

/**
 * Robustly requests accessibility focus for a node identified by its test tag.
 *
 * This action attempts to bring the node into view and then fires a raw semantics 
 * focus request. It verifies that the node actually gains focus before proceeding, 
 * making it a reliable way to seed focus for accessibility traversal tests.
 *
 * @param testTag The unique identifier for the UI element to focus.
 * @param useUnmergedTree Whether to use the unmerged semantics tree for lookups.
 * @throws AssertionError if the node is not found or fails to gain focus.
 */
fun ComposeRuleScope.requestFocus(testTag: String, useUnmergedTree: Boolean = false) {
    logger.info("Starting requestFocus: testTag=$testTag, useUnmergedTree=$useUnmergedTree")
    runRobustly("Request focus on tag: $testTag", testTag) {
        val interaction = composeRule.onNodeWithTag(testTag, useUnmergedTree)
        try {
            logger.debug("Attempting to scroll to tag: $testTag")
            interaction.performScrollTo()
        } catch (e: AssertionError) {}
        logger.debug("Requesting focus for tag: $testTag")
        interaction.performSemanticsAction(SemanticsActions.RequestFocus)
        composeRule.waitForIdle()
        logger.debug("Verifying focus for tag: $testTag")
        composeRule.onNodeWithTag(testTag, useUnmergedTree).assertIsFocused()
    }
    logger.debug("requestFocus completed for tag: $testTag")
}

/**
 * Performs a robust physical hardware key input on a node.
 *
 * This injects both [keyDown] and [keyUp] events for the specified [key].
 * Leverages the robust action pipeline ([runRobustly]).
 *
 * @param testTag The unique identifier for the target UI element.
 * @param key The physical [Key] to press (e.g., Key.Enter, Key.Escape).
 * @param useUnmergedTree Whether to use the unmerged semantics tree for lookups.
 * @throws AssertionError if the node is not found or key input fails.
 */
@OptIn(ExperimentalTestApi::class)
fun ComposeRuleScope.performKeyInput(testTag: String, key: Key, useUnmergedTree: Boolean = false) {
    logger.info("Starting performKeyInput: key=$key, testTag=$testTag, useUnmergedTree=$useUnmergedTree")
    runRobustly("Press key $key on tag: $testTag", testTag) {
        val interaction = composeRule.onNodeWithTag(testTag, useUnmergedTree)
        try {
            logger.debug("Attempting to scroll to tag: $testTag")
            interaction.performScrollTo()
        } catch (e: AssertionError) {}
        logger.debug("Performing key input $key on tag: $testTag")
        interaction.performKeyInput {
            keyDown(key)
            keyUp(key)
        }
    }
    logger.debug("performKeyInput completed for tag: $testTag")
}

