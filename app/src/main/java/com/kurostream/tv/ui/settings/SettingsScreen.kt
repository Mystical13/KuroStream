package com.kurostream.tv.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SettingsScreen(navController: NavController) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            item {
                SettingsItem(
                    title = "Player Settings",
                    description = "Video quality, subtitles, audio",
                    modifier = Modifier.focusRequester(focusRequester),
                    onClick = { /* Navigate to player settings */ }
                )
            }

            item {
                SettingsItem(
                    title = "Account Settings",
                    description = "AniList sync, profile management",
                    modifier = Modifier.focusRequester(focusRequester),                    onClick = { /* Navigate to account settings */ }
                )
            }

            item {
                SettingsItem(
                    title = "Extension Management",
                    description = "Install/uninstall anime sources",
                    modifier = Modifier.focusRequester(focusRequester),
                    onClick = { /* Navigate to extension management */ }
                )
            }

            item {
                SettingsItem(
                    title = "Proxy Settings",
                    description = "Torrent streaming configuration",
                    modifier = Modifier.focusRequester(focusRequester),
                    onClick = { /* Navigate to proxy settings */ }
                )
            }

            item {
                SettingsItem(
                    title = "About",
                    description = "Version information and credits",
                    modifier = Modifier.focusRequester(focusRequester),
                    onClick = { /* Navigate to about screen */ }
                )
            }
        }
    }
}

@Composable
fun SettingsItem(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .focusable(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
                
                Spacer(modifier = Modifier.height(2.dp))
                
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(
                onClick = onClick,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.ChevronRight,
                    contentDescription = "Navigate"
                )
            }
        }
    }
}
