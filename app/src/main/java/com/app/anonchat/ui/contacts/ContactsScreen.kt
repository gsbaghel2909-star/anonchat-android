package com.app.anonchat.ui.contacts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.anonchat.data.contacts.ContactWithProfile

@Composable
fun ContactsScreen(
    viewModel: ContactsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        Text("Contacts", style = MaterialTheme.typography.headlineSmall)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            OutlinedTextField(
                value = state.searchUsername,
                onValueChange = viewModel::onSearchUsernameChange,
                label = { Text("Add by username") },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
        }

        Button(
            onClick = { viewModel.sendRequest() },
            enabled = !state.isSearching && state.searchUsername.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (state.isSearching) "Sending..." else "Send request")
        }

        state.searchMessage?.let {
            Text(
                it,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        if (state.isLoading) {
            CircularProgressIndicator()
        } else if (state.contacts.isEmpty()) {
            Text(
                "No contacts yet. Add someone by their username above.",
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(state.contacts) { contact ->
                    ContactRowItem(
                        contact = contact,
                        onRespond = viewModel::respondToRequest,
                        onRemove = viewModel::removeContact
                    )
                }
            }
        }
    }
}

@Composable
private fun ContactRowItem(
    contact: ContactWithProfile,
    onRespond: (contactId: String, accept: Boolean) -> Unit,
    onRemove: (contactId: String) -> Unit
) {
    var showRemoveConfirm by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(contact.otherUsername, style = MaterialTheme.typography.titleMedium)

        when (contact.status) {
            "accepted" -> {
                Text("Contact", style = MaterialTheme.typography.bodySmall)
                TextButton(onClick = { showRemoveConfirm = true }) {
                    Text("Remove")
                }
            }
            "pending" -> if (contact.iAmRequester) {
                Text("Request sent — waiting for them to accept", style = MaterialTheme.typography.bodySmall)
            } else {
                Row(modifier = Modifier.padding(top = 4.dp)) {
                    Button(onClick = { onRespond(contact.contactId, true) }) {
                        Text("Accept")
                    }
                    OutlinedButton(
                        onClick = { onRespond(contact.contactId, false) },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text("Decline")
                    }
                }
            }
            "declined" -> Text("Declined", style = MaterialTheme.typography.bodySmall)
        }

        Divider(modifier = Modifier.padding(top = 8.dp))
    }

    if (showRemoveConfirm) {
        AlertDialog(
            onDismissRequest = { showRemoveConfirm = false },
            title = { Text("Remove ${contact.otherUsername}?") },
            text = { Text("You'll need to add them again by username if you want to chat later.") },
            confirmButton = {
                TextButton(onClick = {
                    onRemove(contact.contactId)
                    showRemoveConfirm = false
                }) {
                    Text("Remove")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRemoveConfirm = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}