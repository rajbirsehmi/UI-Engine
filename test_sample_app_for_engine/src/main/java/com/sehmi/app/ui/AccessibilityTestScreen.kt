package com.sehmi.app.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat

@Composable
fun AccessibilityTestScreen() {
    val context = LocalContext.current
    var notificationSent by remember { mutableStateOf(value = false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("accessibility_screen"),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Accessibility & System Tests",
            style = MaterialTheme.typography.headlineMedium
        )

        // Elements for focus order testing
        Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("focus_1")
        ) {
            Text("First Focusable Element")
        }

        OutlinedButton(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("focus_2")
        ) {
            Text("Second Focusable Element")
        }

        // Element missing content description (for negative audit test)
        Box(
            modifier = Modifier
                .size(60.dp)
                .clickable { }
                .semantics { /* intentionally missing contentDescription */ }
                .testTag("missing_label_box")
        )

        // Element with proper content description
        Box(
            modifier = Modifier
                .size(60.dp)
                .clickable { }
                .semantics { contentDescription = "Properly labeled box" }
                .testTag("labeled_box"),
            contentAlignment = Alignment.Center
        ) {
            Text("Labeled")
        }

        HorizontalDivider()

        Text(
            text = "System Actions",
            style = MaterialTheme.typography.titleLarge
        )

        Button(
            onClick = {
                sendTestNotification(context)
                notificationSent = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("send_notification_btn")
        ) {
            Text("Send Test Notification")
        }

        if (notificationSent) {
            Text(
                "Notification Sent!",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.testTag("notification_sent_msg")
            )
        }
    }
}

private fun sendTestNotification(context: Context) {
    val channelId = "test_channel"
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val channel = NotificationChannel(channelId, "Test Channel", NotificationManager.IMPORTANCE_DEFAULT)
    notificationManager.createNotificationChannel(channel)

    val notification = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setContentTitle("Engine Test")
        .setContentText("This is a test notification for the UI Engine.")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)
        .build()

    notificationManager.notify(1, notification)
}
