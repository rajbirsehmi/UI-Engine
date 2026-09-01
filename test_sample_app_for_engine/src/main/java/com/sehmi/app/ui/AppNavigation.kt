package com.sehmi.app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sehmi.app.ui.theme.UIAutomationTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val title = when (currentRoute) {
        "home" -> "Automation Test App"
        "gestures" -> "Gestures Test"
        "form" -> "Form Test"
        "scroll" -> "Scroll Test"
        "state" -> "State Test"
        "accessibility" -> "Accessibility & System"
        else -> "Automation Test App"
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(title, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    if (currentRoute != "home") {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") {
                HomeScreen(
                    onNavigateToGestures = { navController.navigate("gestures") },
                    onNavigateToForm = { navController.navigate("form") },
                    onNavigateToScroll = { navController.navigate("scroll") },
                    onNavigateToState = { navController.navigate("state") }
                ) { navController.navigate("accessibility") }
            }
            composable("gestures") { GestureTestScreen() }
            composable("form") { FormTestScreen() }
            composable("scroll") { ScrollTestScreen() }
            composable("state") { StateTestScreen() }
            composable("accessibility") { AccessibilityTestScreen() }
        }
    }
}

@Composable
fun HomeScreen(
    onNavigateToGestures: () -> Unit,
    onNavigateToForm: () -> Unit,
    onNavigateToScroll: () -> Unit,
    onNavigateToState: () -> Unit,
    onNavigateToAccessibility: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("home_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Select a Test Category",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        NavigationCard("Gestures", "Test taps, double taps, and sliders", "nav_gestures", onNavigateToGestures)
        NavigationCard("Forms", "Test input fields and buttons", "nav_form", onNavigateToForm)
        NavigationCard("Scrolling", "Test list scrolling and lazy layouts", "nav_scroll", onNavigateToScroll)
        NavigationCard("State", "Test checkboxes, switches, and dialogs", "nav_state", onNavigateToState)
        NavigationCard("Deep Interactions", "Test Accessibility and System actions", "nav_accessibility", onNavigateToAccessibility)
    }
}

@Composable
fun NavigationCard(
    title: String,
    subtitle: String,
    tag: String,
    onClick: () -> Unit
) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .testTag(tag)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = title, style = MaterialTheme.typography.titleLarge)
            Text(text = subtitle, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    UIAutomationTheme {
        HomeScreen({}, {}, {}, {}, {})
    }
}

@Preview(showBackground = true)
@Composable
fun AppNavigationPreview() {
    UIAutomationTheme {
        AppNavigation()
    }
}
