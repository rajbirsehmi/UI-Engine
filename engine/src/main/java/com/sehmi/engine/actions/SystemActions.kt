package com.sehmi.engine.actions

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until
import com.sehmi.engine.core.ComposeRuleScope
import com.sehmi.engine.utils.runRobustly
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.File

private val logger: Logger = LogManager.getLogger("SystemActions")

/**
 * Supported screen orientations for UI testing.
 */
enum class Orientation {
    /** The device's natural orientation (usually portrait for phones). */
    PORTRAIT,
    /** Rotated 90 degrees to the left. */
    LANDSCAPE
}

/**
 * Robustly presses the system back button.
 *
 * Unlike standard Compose actions, this interacts with the system via UI Automator.
 * It is designed to be safe and will not throw an exception if the action cannot be 
 * performed (e.g., already at the home screen).
 */
fun ComposeRuleScope.pressBack() {
    logger.info("Starting pressBack")
    val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    logger.debug("Performing system back button press")
    device.pressBack()
    logger.debug("pressBack completed")
}

/**
 * Presses the system home button using UI Automator.
 *
 * Useful for testing app backgrounding and resumption scenarios.
 */
fun ComposeRuleScope.pressHome() {
    logger.info("Starting pressHome")
    val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    logger.debug("Performing system home button press")
    device.pressHome()
    logger.debug("pressHome completed")
}

/**
 * Rotates the device screen to the specified [orientation] and waits for the 
 * Compose UI to settle.
 *
 * @param orientation The target [Orientation].
 */
fun ComposeRuleScope.rotateScreen(orientation: Orientation) {
    logger.info("Starting rotateScreen: orientation=$orientation")
    val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    logger.debug("Rotating device to $orientation")
    when (orientation) {
        Orientation.PORTRAIT -> device.setOrientationNatural()
        Orientation.LANDSCAPE -> device.setOrientationLeft()
    }
    logger.debug("Waiting for Compose UI to idle after rotation")
    composeRule.waitForIdle()
    logger.debug("rotateScreen completed")
}

/**
 * Safely handles system permission dialogs by clicking the appropriate button.
 *
 * This utility uses fuzzy text matching to handle differences across Android OS versions 
 * and locales (e.g., matching "Allow", "Grant", or "Allow only while using the app").
 *
 * @param allow True to grant the permission, false to deny it.
 */
fun ComposeRuleScope.handlePermissionDialog(allow: Boolean) {
    logger.info("Starting handlePermissionDialog: allow=$allow")
    val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    val buttonText = if (allow) "Allow" else "Deny"
    logger.debug("Searching for permission dialog button with text matching '$buttonText'")
    // Use regex to handle different OS versions/locales
    val permissionButton = device.findObject(UiSelector().textMatches("(?i)($buttonText|Allow|Grant).*"))
    if (permissionButton.exists()) {
        logger.debug("Permission button found, clicking")
        permissionButton.click()
        composeRule.waitForIdle()
        logger.debug("Permission dialog handled and UI idle")
    } else {
        logger.debug("Permission button not found")
    }
}

/**
 * Robustly waits for a specific system window or application package to become active.
 *
 * Useful for cross-app testing or waiting for external components like a browser 
 * or system settings.
 *
 * @param packageName The package name of the application to wait for.
 * @param timeoutMillis Maximum time to wait in milliseconds.
 */
fun ComposeRuleScope.waitForSystemWindow(packageName: String, timeoutMillis: Long = 5000) {
    logger.info("Starting waitForSystemWindow: packageName=$packageName, timeoutMillis=$timeoutMillis")
    val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    logger.debug("Waiting for package $packageName to become active")
    device.wait(Until.hasObject(By.pkg(packageName)), timeoutMillis)
    logger.debug("waitForSystemWindow completed")
}

/**
 * Captures a screenshot of the current device screen and saves it as a PNG file 
 * in the external cache directory.
 *
 * This is primarily used by the robust action pipeline to collect diagnostic artifacts 
 * on failure.
 *
 * @param name The base name for the screenshot file (excluding extension).
 */
fun takeScreenshot(name: String) {
    logger.info("Starting takeScreenshot: name=$name")
    val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    val context = InstrumentationRegistry.getInstrumentation().targetContext
    val path = context.externalCacheDir ?: context.cacheDir
    val file = File(path, "$name.png")
    logger.debug("Saving screenshot to: ${file.absolutePath}")
    device.takeScreenshot(file)
    logger.debug("takeScreenshot completed")
}

/**
 * Robustly opens the system notification shade using UI Automator.
 *
 * This action performs a system-wide swipe from the top of the screen to expand 
 * the notification tray. It is useful for testing app behavior in response to 
 * system notifications or external interrupts.
 */
fun ComposeRuleScope.openNotificationShade() {
    logger.info("Starting openNotificationShade")
    runRobustly("Open notification shade") {
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        logger.debug("Performing system notification shade expansion")
        device.openNotification()
        composeRule.waitForIdle()
    }
    logger.debug("openNotificationShade completed")
}

/**
 * Robustly clicks a notification containing the specified [text] in the notification shade.
 *
 * This interaction leverages UI Automator to find and click a notification by its 
 * title or content text. If the notification shade is not already visible, the 
 * engine will automatically attempt to open it first.
 *
 * @param text The title or content text of the notification to click.
 * @param timeoutMillis Maximum time to wait for the notification to appear in the shade.
 * @throws AssertionError if the notification is not found within the timeout.
 */
fun ComposeRuleScope.clickNotification(text: String, timeoutMillis: Long = 5000L) {
    logger.info("Starting clickNotification: text='$text'")
    runRobustly("Click notification with text: $text") {
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        
        // Ensure shade is open
        if (!device.hasObject(By.textContains(text))) {
            logger.debug("Notification not visible, opening shade")
            device.openNotification()
        }
        
        logger.debug("Waiting for notification with text '$text'")
        val notification = device.wait(Until.findObject(By.textContains(text)), timeoutMillis)
        
        if (notification != null) {
            logger.debug("Notification found, clicking")
            notification.click()
            composeRule.waitForIdle()
        } else {
            throw AssertionError("Notification with text '$text' not found after ${timeoutMillis}ms")
        }
    }
    logger.debug("clickNotification completed for text: $text")
}

/**
 * Robustly toggles a system quick setting tile (e.g., "Dark mode", "Airplane mode").
 *
 * This interaction opens the quick settings panel (double swipe down) and searches 
 * for a tile matching the provided [settingName] (either via content description or text). 
 * Once found, it performs a click and closes the panel.
 *
 * @param settingName The displayed name or description of the quick setting tile.
 * @throws AssertionError if the quick setting tile is not found.
 */
fun ComposeRuleScope.toggleQuickSetting(settingName: String) {
    logger.info("Starting toggleQuickSetting: settingName=$settingName")
    runRobustly("Toggle quick setting: $settingName") {
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        
        logger.debug("Opening quick settings (double swipe down)")
        device.openQuickSettings()
        
        logger.debug("Searching for quick setting tile: $settingName")
        var tile = device.wait(Until.findObject(By.descContains(settingName)), 2000L)
        if (tile == null) {
            tile = device.wait(Until.findObject(By.textContains(settingName)), 2000L)
        }
        
        if (tile != null) {
            logger.debug("Quick setting tile found, clicking")
            tile.click()
            composeRule.waitForIdle()
            // Close quick settings
            device.pressBack()
        } else {
            throw AssertionError("Quick setting tile '$settingName' not found.")
        }
    }
    logger.debug("toggleQuickSetting completed for setting: $settingName")
}

