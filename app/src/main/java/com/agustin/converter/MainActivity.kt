package com.agustin.converter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import com.agustin.converter.ui.theme.ConverterTheme   // <- your theme file
import com.agustin.converter.ui.ConverterScreen       // <- the screen you built

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ConverterTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ConverterScreen()
                }
            }
        }
    }
}
