package com.densappstudio.tiltballmaze

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.densappstudio.tiltballmaze.ui.theme.TiltballmazeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TiltballmazeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Empty Activity content
                }
            }
        }
    }
}
