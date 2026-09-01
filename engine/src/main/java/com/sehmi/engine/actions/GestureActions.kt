package com.sehmi.engine.actions

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.click
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.doubleClick
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.pinch
import androidx.compose.ui.test.swipeDown
import androidx.compose.ui.test.swipeLeft
import androidx.compose.ui.test.swipeRight
import androidx.compose.ui.test.swipeUp
import com.sehmi.engine.core.ComposeRuleScope
import com.sehmi.engine.utils.runRobustly
import com.sehmi.engine.utils.waitUntil
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

private val logger: Logger = LogManager.getLogger("GestureActions")

/**
 * Supported directions for swipe gestures within the UI automation engine.
 */
enum class Direction {
    /** Swipe from bottom to top. */
    UP,
    /** Swipe from top to bottom. */
    DOWN,
    /** Swipe from right to left. */
    LEFT,
    /** Swipe from left to right. */
    RIGHT
}

/**
 * Performs a robust click on a node identified by its test tag.
 *
 * This action leverages the engine's robust action pipeline ([runRobustly]), which includes:
 * - Automatic scrolling to the element if it's within a scrollable container.
 * - Explicit waiting until the node is displayed and enabled.
 * - Automatic retry logic to handle potential flakiness.
 * - Diagnostic capture (screenshot and semantics tree dump) on failure.
 *
 * @param testTag The unique identifier for the UI element.
 * @param useUnmergedTree Whether to use the unmerged semantics tree for lookups.
 * @throws AssertionError if the node is not found or interaction fails after retries.
 */
fun ComposeRuleScope.clickOnTag(testTag: String, useUnmergedTree: Boolean = false) {
    logger.info("Starting clickOnTag: testTag=$testTag, useUnmergedTree=$useUnmergedTree")
    runRobustly("Click on tag: $testTag", testTag) {
        waitUntil {
            val interaction = composeRule.onNodeWithTag(testTag, useUnmergedTree)
            try {
                logger.debug("Attempting to scroll to tag: $testTag")
                interaction.performScrollTo()
            } catch (e: AssertionError) {
                // Ignore if scroll parent not found
            }
            logger.debug("Waiting for tag $testTag to be displayed and enabled")
            interaction
                .assertIsDisplayed()
                .assertIsEnabled()
            logger.debug("Performing semantics click on tag: $testTag")
            interaction.performClick()
            composeRule.waitForIdle()
        }
    }
    logger.debug("clickOnTag completed for tag: $testTag")
}

/**
 * Performs a robust click on a node containing the specified text.
 *
 * This action leverages the engine's robust action pipeline ([runRobustly]), which includes:
 * - Automatic scrolling to the element.
 * - Explicit waiting for visibility and enabled state.
 * - Retry logic and diagnostic capture on failure.
 *
 * @param text The text content of the node to click.
 * @param useUnmergedTree Whether to use the unmerged semantics tree for lookups.
 * @throws AssertionError if the node is not found or interaction fails after retries.
 */
fun ComposeRuleScope.clickOnText(text: String, useUnmergedTree: Boolean = false) {
    logger.info("Starting clickOnText: text=$text, useUnmergedTree=$useUnmergedTree")
    runRobustly("Click on text: $text") {
        waitUntil {
            val interaction = composeRule.onNodeWithText(text, useUnmergedTree = useUnmergedTree)
            try {
                logger.debug("Attempting to scroll to text: $text")
                interaction.performScrollTo()
            } catch (e: AssertionError) {
                // Ignore
            }
            logger.debug("Waiting for text '$text' to be displayed and enabled")
            interaction
                .assertIsDisplayed()
                .assertIsEnabled()
            logger.debug("Performing semantics click on text: $text")
            interaction.performClick()
            composeRule.waitForIdle()
        }
    }
    logger.debug("clickOnText completed for text: $text")
}

/**
 * Performs a robust long click on a node identified by its test tag.
 *
 * Leverages the robust action pipeline ([runRobustly]) to ensure the node is visible and
 * scrolled into view before performing the long press gesture.
 *
 * @param testTag The unique identifier for the UI element.
 * @param useUnmergedTree Whether to use the unmerged semantics tree for lookups.
 * @throws AssertionError if the node is not found or interaction fails after retries.
 */
fun ComposeRuleScope.longClickTag(testTag: String, useUnmergedTree: Boolean = false) {
    logger.info("Starting longClickTag: testTag=$testTag, useUnmergedTree=$useUnmergedTree")
    runRobustly("Long click on tag: $testTag", testTag) {
        waitUntil {
            val interaction = composeRule.onNodeWithTag(testTag, useUnmergedTree)
            try {
                logger.debug("Attempting to scroll to tag: $testTag")
                interaction.performScrollTo()
            } catch (e: AssertionError) {
                // Ignore
            }
            logger.debug("Waiting for tag $testTag to be displayed")
            interaction.assertIsDisplayed()
            logger.debug("Performing long click on tag: $testTag")
            interaction.performTouchInput { longClick() }
            composeRule.waitForIdle()
        }
    }
    logger.debug("longClickTag completed for tag: $testTag")
}

/**
 * Performs a robust long click on a node containing the specified text.
 *
 * Leverages the robust action pipeline ([runRobustly]) to ensure the node is visible and
 * scrolled into view before performing the long press gesture.
 *
 * @param text The text content of the node to long click.
 * @param useUnmergedTree Whether to use the unmerged semantics tree for lookups.
 * @throws AssertionError if the node is not found or interaction fails after retries.
 */
fun ComposeRuleScope.longClickText(text: String, useUnmergedTree: Boolean = false) {
    logger.info("Starting longClickText: text=$text, useUnmergedTree=$useUnmergedTree")
    runRobustly("Long click on text: $text") {
        waitUntil {
            val interaction = composeRule.onNodeWithText(text, useUnmergedTree = useUnmergedTree)
            try {
                logger.debug("Attempting to scroll to text: $text")
                interaction.performScrollTo()
            } catch (e: AssertionError) {
                // Ignore
            }
            logger.debug("Waiting for text '$text' to be displayed")
            interaction.assertIsDisplayed()
            logger.debug("Performing long click on text: $text")
            interaction.performTouchInput { longClick() }
        }
    }
    logger.debug("longClickText completed for text: $text")
}

/**
 * Performs a robust double click on a node identified by its test tag.
 *
 * Leverages the robust action pipeline ([runRobustly]) to ensure the node is visible and
 * scrolled into view before performing the double click gesture.
 *
 * @param testTag The unique identifier for the UI element.
 * @param useUnmergedTree Whether to use the unmerged semantics tree for lookups.
 * @throws AssertionError if the node is not found or interaction fails after retries.
 */
fun ComposeRuleScope.doubleClickTag(testTag: String, useUnmergedTree: Boolean = false) {
    logger.info("Starting doubleClickTag: testTag=$testTag, useUnmergedTree=$useUnmergedTree")
    runRobustly("Double click on tag: $testTag", testTag) {
        waitUntil {
            val interaction = composeRule.onNodeWithTag(testTag, useUnmergedTree)
            try {
                logger.debug("Attempting to scroll to tag: $testTag")
                interaction.performScrollTo()
            } catch (e: AssertionError) {
                // Ignore
            }
            logger.debug("Waiting for tag $testTag to be displayed")
            interaction.assertIsDisplayed()
            logger.debug("Performing double click on tag: $testTag")
            interaction.performTouchInput { doubleClick() }
            composeRule.waitForIdle()
        }
    }
    logger.debug("doubleClickTag completed for tag: $testTag")
}

/**
 * Performs a robust swipe gesture on a node identified by its test tag.
 *
 * Leverages the robust action pipeline ([runRobustly]) to ensure the target node is ready
 * for interaction. The swipe direction is specified by the [direction] parameter.
 *
 * @param testTag The unique identifier for the node to swipe on.
 * @param direction The [Direction] of the swipe.
 * @param useUnmergedTree Whether to use the unmerged semantics tree for lookups.
 * @throws AssertionError if the node is not found or swipe fails.
 */
fun ComposeRuleScope.swipe(testTag: String, direction: Direction, useUnmergedTree: Boolean = false) {
    logger.info("Starting swipe: testTag=$testTag, direction=$direction, useUnmergedTree=$useUnmergedTree")
    runRobustly("Swipe $direction on tag: $testTag", testTag) {
        waitUntil {
            val interaction = composeRule.onNodeWithTag(testTag, useUnmergedTree)
            try {
                logger.debug("Attempting to scroll to tag: $testTag")
                interaction.performScrollTo()
            } catch (e: AssertionError) {
                // Ignore
            }
            logger.debug("Performing swipe $direction on tag: $testTag")
            interaction
                .performTouchInput {
                    when (direction) {
                        Direction.UP -> swipeUp()
                        Direction.DOWN -> swipeDown()
                        Direction.LEFT -> swipeLeft()
                        Direction.RIGHT -> swipeRight()
                    }
                }
        }
    }
    logger.debug("swipe completed for tag: $testTag")
}

/**
 * Performs a robust drag and drop gesture from a source node to a target node.
 *
 * This action attempts to scroll both the source and target nodes into view before
 * calculating coordinates and performing the drag-and-drop touch sequence.
 *
 * @param sourceTag The test tag of the element to be dragged.
 * @param targetTag The test tag of the destination where the element should be dropped.
 * @throws AssertionError if either node is not found or gesture fails.
 */
fun ComposeRuleScope.dragAndDrop(sourceTag: String, targetTag: String) {
    logger.info("Starting dragAndDrop: sourceTag=$sourceTag, targetTag=$targetTag")
    runRobustly("Drag from $sourceTag to $targetTag", sourceTag) {
        try {
            logger.debug("Scrolling to source tag: $sourceTag")
            composeRule.onNodeWithTag(sourceTag).performScrollTo()
        } catch (e: AssertionError) {}
        try {
            logger.debug("Scrolling to target tag: $targetTag")
            composeRule.onNodeWithTag(targetTag).performScrollTo()
        } catch (e: AssertionError) {}
        
        logger.debug("Calculating centers for drag and drop")
        val sourceNode = composeRule.onNodeWithTag(sourceTag).fetchSemanticsNode()
        val targetNode = composeRule.onNodeWithTag(targetTag).fetchSemanticsNode()
        
        val sourceCenter = sourceNode.boundsInRoot.center
        val targetCenter = targetNode.boundsInRoot.center
        
        logger.debug("Performing drag from $sourceCenter to $targetCenter")
        composeRule.onNodeWithTag(sourceTag).performTouchInput {
            down(sourceCenter)
            moveTo(targetCenter)
            up()
        }
    }
    logger.debug("dragAndDrop completed from $sourceTag to $targetTag")
}

/**
 * Performs a robust pinch-to-zoom gesture on a node identified by its test tag.
 *
 * Useful for maps, images, or other components that support multi-touch scaling.
 * Leverages the robust action pipeline ([runRobustly]).
 *
 * @param testTag The unique identifier for the UI element.
 * @param zoomIn True to zoom in (pinch out), false to zoom out (pinch in).
 * @param useUnmergedTree Whether to use the unmerged semantics tree for lookups.
 * @throws AssertionError if the node is not found or gesture fails.
 */
@OptIn(ExperimentalTestApi::class)
fun ComposeRuleScope.pinchToZoom(testTag: String, zoomIn: Boolean = true, useUnmergedTree: Boolean = false) {
    logger.info("Starting pinchToZoom: testTag=$testTag, zoomIn=$zoomIn, useUnmergedTree=$useUnmergedTree")
    runRobustly("${if (zoomIn) "Zoom In" else "Zoom Out"} on tag: $testTag", testTag) {
        waitUntil {
            val interaction = composeRule.onNodeWithTag(testTag, useUnmergedTree)
            try {
                logger.debug("Attempting to scroll to tag: $testTag")
                interaction.performScrollTo()
            } catch (e: AssertionError) {}
            
            logger.debug("Performing pinch gesture (zoomIn=$zoomIn) on tag: $testTag")
            interaction
                .performTouchInput {
                    val start0 = center + Offset(-width / 4f, -height / 4f)
                    val end0 = center + Offset(-width / 2f, -height / 2f)
                    val start1 = center + Offset(width / 4f, height / 4f)
                    val end1 = center + Offset(width / 2f, height / 2f)
                    
                    if (zoomIn) {
                        pinch(start0, end0, start1, end1)
                    } else {
                        pinch(end0, start0, end1, start1)
                    }
                }
        }
    }
    logger.debug("pinchToZoom completed for tag: $testTag")
}

/**
 * Performs a robust click at a specific percentage offset within a node.
 *
 * This is useful for interacting with specific parts of a component where standard
 * [performClick] might not hit the desired area.
 *
 * @param testTag The unique identifier for the UI element.
 * @param xPercentage Horizontal offset from 0.0 (left) to 1.0 (right).
 * @param yPercentage Vertical offset from 0.0 (top) to 1.0 (bottom).
 * @param useUnmergedTree Whether to use the unmerged semantics tree for lookups.
 * @throws AssertionError if the node is not found or gesture fails.
 */
fun ComposeRuleScope.clickAtOffset(testTag: String, xPercentage: Float, yPercentage: Float, useUnmergedTree: Boolean = false) {
    logger.info("Starting clickAtOffset: testTag=$testTag, offset=($xPercentage, $yPercentage), useUnmergedTree=$useUnmergedTree")
    runRobustly("Click at offset ($xPercentage, $yPercentage) on tag: $testTag", testTag) {
        val interaction = composeRule.onNodeWithTag(testTag, useUnmergedTree)
        try {
            logger.debug("Attempting to scroll to tag: $testTag")
            interaction.performScrollTo()
        } catch (e: AssertionError) {}
        
        logger.debug("Performing click at offset ($xPercentage, $yPercentage) on tag: $testTag")
        interaction.performTouchInput {
            click(Offset(width * xPercentage, height * yPercentage))
        }
    }
    logger.debug("clickAtOffset completed for tag: $testTag")
}

/**
 * Performs a robust two-finger rotation gesture on a node identified by its test tag.
 *
 * This interaction calculates an orbital path for two touch pointers around the center 
 * of the semantics node. It is useful for verifying behaviors in maps, image editors, 
 * or custom 3D viewers.
 *
 * @param testTag The unique identifier for the UI element to rotate.
 * @param degrees The number of degrees to rotate (positive for clockwise, negative for counter-clockwise).
 * @param durationMillis The duration of the rotation gesture in milliseconds.
 * @param useUnmergedTree Whether to use the unmerged semantics tree for lookups.
 * @throws AssertionError if the node is not found or interaction fails.
 */
fun ComposeRuleScope.rotate(
    testTag: String,
    degrees: Float,
    durationMillis: Long = 500L,
    useUnmergedTree: Boolean = false
) {
    logger.info("Starting rotate: testTag=$testTag, degrees=$degrees, duration=$durationMillis")
    runRobustly("Rotate $degrees degrees on tag: $testTag", testTag) {
        val interaction = composeRule.onNodeWithTag(testTag, useUnmergedTree)
        try {
            interaction.performScrollTo()
        } catch (e: AssertionError) {}

        interaction.performTouchInput {
            val radius = minOf(width, height) / 4f
            val center = center
            
            val startAngle1 = 0.0
            val startAngle2 = Math.PI
            
            val endAngle1 = startAngle1 + Math.toRadians(degrees.toDouble())
            val endAngle2 = startAngle2 + Math.toRadians(degrees.toDouble())
            
            val p1Start = center + Offset(radius * Math.cos(startAngle1).toFloat(), radius * Math.sin(startAngle1).toFloat())
            val p2Start = center + Offset(radius * Math.cos(startAngle2).toFloat(), radius * Math.sin(startAngle2).toFloat())
            
            val p1End = center + Offset(radius * Math.cos(endAngle1).toFloat(), radius * Math.sin(endAngle1).toFloat())
            val p2End = center + Offset(radius * Math.cos(endAngle2).toFloat(), radius * Math.sin(endAngle2).toFloat())
            
            down(0, p1Start)
            down(1, p2Start)
            
            val steps = (durationMillis / 16).toInt().coerceAtLeast(1)
            for (i in 1..steps) {
                val fraction = i.toFloat() / steps
                val currentAngle1 = startAngle1 + (endAngle1 - startAngle1) * fraction
                val currentAngle2 = startAngle2 + (endAngle2 - startAngle2) * fraction
                
                moveTo(0, center + Offset(radius * Math.cos(currentAngle1).toFloat(), radius * Math.sin(currentAngle1).toFloat()))
                moveTo(1, center + Offset(radius * Math.cos(currentAngle2).toFloat(), radius * Math.sin(currentAngle2).toFloat()))
                advanceEventTime(16)
            }
            
            up(0)
            up(1)
        }
    }
    logger.debug("rotate completed for tag: $testTag")
}

/**
 * Performs a robust multi-finger swipe gesture on a node identified by its test tag.
 *
 * Simulates a simultaneous swipe with multiple touch pointers (2 to 4 fingers). 
 * This is commonly used for system-level gestures (like 3-finger swipe to switch apps) 
 * or custom complex navigation patterns within a professional application.
 *
 * @param testTag The unique identifier for the UI element to swipe on.
 * @param fingers The number of fingers to use (supports 2, 3, or 4).
 * @param direction The [Direction] of the swipe.
 * @param durationMillis The duration of the swipe in milliseconds.
 * @param useUnmergedTree Whether to use the unmerged semantics tree for lookups.
 * @throws IllegalArgumentException if the finger count is not between 2 and 4.
 * @throws AssertionError if the node is not found or interaction fails.
 */
fun ComposeRuleScope.multiFingerSwipe(
    testTag: String,
    fingers: Int,
    direction: Direction,
    durationMillis: Long = 300L,
    useUnmergedTree: Boolean = false
) {
    logger.info("Starting multiFingerSwipe: testTag=$testTag, fingers=$fingers, direction=$direction")
    require(fingers in 2..4) { "multiFingerSwipe supports 2 to 4 fingers." }
    
    runRobustly("$fingers-finger swipe $direction on tag: $testTag", testTag) {
        val interaction = composeRule.onNodeWithTag(testTag, useUnmergedTree)
        try {
            interaction.performScrollTo()
        } catch (e: AssertionError) {}
        
        interaction.performTouchInput {
            val startOffsets = mutableListOf<Offset>()
            val endOffsets = mutableListOf<Offset>()
            
            val spread = 20f // pixels between fingers
            
            for (i in 0 until fingers) {
                val fingerOffset = (i - (fingers - 1) / 2f) * spread
                val start = when (direction) {
                    Direction.UP -> Offset(center.x + fingerOffset, bottom - 10f)
                    Direction.DOWN -> Offset(center.x + fingerOffset, top + 10f)
                    Direction.LEFT -> Offset(right - 10f, center.y + fingerOffset)
                    Direction.RIGHT -> Offset(left + 10f, center.y + fingerOffset)
                }
                val end = when (direction) {
                    Direction.UP -> Offset(center.x + fingerOffset, top + 10f)
                    Direction.DOWN -> Offset(center.x + fingerOffset, bottom - 10f)
                    Direction.LEFT -> Offset(left + 10f, center.y + fingerOffset)
                    Direction.RIGHT -> Offset(right - 10f, center.y + fingerOffset)
                }
                startOffsets.add(start)
                endOffsets.add(end)
            }
            
            // All fingers down
            startOffsets.forEachIndexed { index, offset -> down(index, offset) }
            
            // Move all fingers
            val steps = (durationMillis / 16).toInt().coerceAtLeast(1)
            for (step in 1..steps) {
                val fraction = step.toFloat() / steps
                startOffsets.forEachIndexed { index, start ->
                    val end = endOffsets[index]
                    moveTo(index, start + (end - start) * fraction)
                }
                advanceEventTime(16)
            }
            
            // All fingers up
            for (i in 0 until fingers) {
                up(i)
            }
        }
    }
    logger.debug("multiFingerSwipe completed for tag: $testTag")
}

