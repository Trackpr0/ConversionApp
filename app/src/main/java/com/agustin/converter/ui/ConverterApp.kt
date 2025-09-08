@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.agustin.converter.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ConverterApp() {
    MaterialTheme {
        Scaffold(
            topBar = { TopAppBar(title = { Text("Converter") }) }
        ) { padding ->
            ConverterScreen(modifier = Modifier.padding(padding))
        }
    }
}
