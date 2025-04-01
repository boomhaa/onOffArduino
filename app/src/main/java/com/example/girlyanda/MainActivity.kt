package com.example.girlyanda

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.girlyanda.application.AppAnalyzer
import com.example.girlyanda.ui.screens.MainScreen
import com.example.girlyanda.ui.theme.ApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val app = AppAnalyzer(applicationContext)
        setContent {
                ApplicationTheme {
                    MainScreen(app = app)
            }
        }
    }
}

