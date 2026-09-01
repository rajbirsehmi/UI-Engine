package com.sehmi.app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sehmi.app.ui.theme.UIAutomationTheme

@Composable
fun StateTestScreen() {
    var checked by remember { mutableStateOf(false) }
    var switched by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("A") }
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("state_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ElevatedCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(8.dp)) {
                ListItem(
                    headlineContent = { Text("Checkbox Interaction") },
                    supportingContent = { Text("State: $checked") },
                    trailingContent = {
                        Checkbox(
                            checked = checked,
                            onCheckedChange = { checked = it },
                            modifier = Modifier.testTag("checkbox")
                        )
                    }
                )
                HorizontalDivider()
                ListItem(
                    headlineContent = { Text("Switch Interaction") },
                    supportingContent = { Text("State: $switched") },
                    trailingContent = {
                        Switch(
                            checked = switched,
                            onCheckedChange = { switched = it },
                            modifier = Modifier.testTag("switch")
                        )
                    }
                )
            }
        }

        ElevatedCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .selectableGroup()
            ) {
                Text(
                    text = "Radio Button Group",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp, 8.dp)
                )
                listOf("A", "B", "C").forEach { option ->
                    ListItem(
                        headlineContent = { Text("Option $option") },
                        leadingContent = {
                            RadioButton(
                                selected = (selectedOption == option),
                                onClick = null // Handled by ListItem click
                            )
                        },
                        modifier = Modifier
                            .testTag("radio_$option")
                            .selectable(
                                selected = (selectedOption == option),
                                onClick = { selectedOption = option },
                                role = Role.RadioButton
                            )
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { },
                enabled = false,
                modifier = Modifier
                    .weight(1f)
                    .testTag("disabled_button")
            ) {
                Text("Disabled")
            }

            Button(
                onClick = { showDialog = true },
                modifier = Modifier
                    .weight(1f)
                    .testTag("dialog_button")
            ) {
                Text("Show Dialog")
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    TextButton(
                        onClick = { showDialog = false },
                        modifier = Modifier.testTag("dialog_confirm")
                    ) {
                        Text("Confirm")
                    }
                },
                title = { Text("Test Dialog") },
                text = { Text("This dialog is used to verify automation engine capabilities for handling alerts and overlays.") },
                modifier = Modifier.testTag("alert_dialog")
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StateTestScreenPreview() {
    UIAutomationTheme {
        StateTestScreen()
    }
}
