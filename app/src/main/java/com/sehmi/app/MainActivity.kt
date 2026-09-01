package com.sehmi.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.sehmi.app.ui.AppNavigation
import com.sehmi.app.ui.theme.UIAutomationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UIAutomationTheme {
                AppNavigation()
            }
        }
    }
}
