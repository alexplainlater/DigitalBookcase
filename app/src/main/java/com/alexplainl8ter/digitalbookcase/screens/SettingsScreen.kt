package com.alexplainl8ter.digitalbookcase.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexplainl8ter.digitalbookcase.ApiType
import com.alexplainl8ter.digitalbookcase.SettingsDataStoreManager
import com.alexplainl8ter.digitalbookcase.ui.theme.DigitalBookcaseTheme
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val settingsDataStoreManager = remember { SettingsDataStoreManager(context) }
    val coroutineScope = rememberCoroutineScope()

    var selectedApiType by remember { mutableStateOf(ApiType.GOOGLE_BOOKS) } // Default value - will be overridden by DataStore

    // Load API type from DataStore when screen is composed
    LaunchedEffect(Unit) {
        settingsDataStoreManager.apiTypeFlow.collect { apiType ->
            selectedApiType = apiType
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Column {
            Text(
                text = "Choose Book Search Source:",
                style = MaterialTheme.typography.titleMedium
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Google Books"
                    , modifier = Modifier
                        .padding(end = 185.dp))
                RadioButton(
                    selected = selectedApiType == ApiType.GOOGLE_BOOKS,
                    onClick = {
                        selectedApiType = ApiType.GOOGLE_BOOKS
                        coroutineScope.launch {
                            settingsDataStoreManager.saveApiType(ApiType.GOOGLE_BOOKS) // Save to DataStore
                        }
                    }
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "OpenLibrary"
                    , modifier = Modifier
                        .padding(end = 200.dp)
                )
                RadioButton(
                    selected = selectedApiType == ApiType.OPEN_LIBRARY,
                    onClick = {
                        selectedApiType = ApiType.OPEN_LIBRARY
                        coroutineScope.launch {
                            settingsDataStoreManager.saveApiType(ApiType.OPEN_LIBRARY) // Save to DataStore
                        }
                    }
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Smart Merge"
                    , modifier = Modifier
                        .padding(end = 195.dp))

                RadioButton(
                    selected = selectedApiType == ApiType.SMART_MERGE,
                    onClick = {
                        selectedApiType = ApiType.SMART_MERGE
                        coroutineScope.launch {
                            settingsDataStoreManager.saveApiType(ApiType.SMART_MERGE) // Save to DataStore
                        }
                    }
                )
            }

            HorizontalDivider(thickness = 1.dp)

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text ="Keep Screen On During Slideshow"
                    , modifier = Modifier
                        .padding( end = 35.dp)
                )

                Switch(
                    checked = true,
                    onCheckedChange = {
                        //do nothing currently
                        TODO() // need to edit some setting to force the screen to stay on during the slideshow
                    },
                    enabled = false
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    DigitalBookcaseTheme {
        SettingsScreen()
    }
}