package com.sehmi.app.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sehmi.app.ui.theme.UIAutomationTheme

@Composable
fun ScrollTestScreen() {
    LazyColumn(
        modifier = Modifier.fillMaxSize().testTag("scroll_column")
    ) {
        items(100) { index ->
            ListItem(
                headlineContent = { Text("Item $index") },
                supportingContent = { Text("Descriptive subtitle for item $index") },
                modifier = Modifier.testTag("item_$index")
            )
            if (index < 99) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    thickness = 0.5.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScrollTestScreenPreview() {
    UIAutomationTheme {
        ScrollTestScreen()
    }
}
