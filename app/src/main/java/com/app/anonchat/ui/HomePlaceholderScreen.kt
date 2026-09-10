package com.app.anonchat.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * Temporary landing screen for Phase 1. Confirms auth worked end-to-end.
 * Replaced in Phase 2 by anonymous profile creation.
 */
@Composable
fun HomePlaceholderScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("You're signed in.", style = MaterialTheme.typography.headlineSmall)
        Text("Phase 2 (anonymous profile setup) goes here next.", style = MaterialTheme.typography.bodyMedium)
    }
}
