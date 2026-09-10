package com.app.anonchat.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    onSignUpSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var dobInput by remember { mutableStateOf("") } // "YYYY-MM-DD" for simplicity; swap for a date picker
    var ageAttested by remember { mutableStateOf(false) }
    var agreedToTerms by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Success) onSignUpSuccess()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Create your account", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(4.dp))
        Text(
            "You'll set up an anonymous profile next. This account info is never shown to other users.",
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = dobInput,
            onValueChange = { dobInput = it },
            label = { Text("Date of birth (YYYY-MM-DD)") },
            supportingText = { Text("Only used to confirm you're 18+. Never shown to other users — your profile only shows an age range.") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = ageAttested, onCheckedChange = { ageAttested = it })
            Text("I confirm I am 18 years of age or older.", style = MaterialTheme.typography.bodySmall)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = agreedToTerms, onCheckedChange = { agreedToTerms = it })
            Text("I agree to the Terms of Service and Privacy Policy.", style = MaterialTheme.typography.bodySmall)
        }

        Spacer(Modifier.height(24.dp))

        if (uiState is AuthUiState.Error) {
            Text(
                (uiState as AuthUiState.Error).message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(Modifier.height(8.dp))
        }

        val dob = runCatching { LocalDate.parse(dobInput) }.getOrNull()
        val formValid = email.isNotBlank() && password.length >= 8 && dob != null && ageAttested && agreedToTerms

        Button(
            onClick = { dob?.let { viewModel.signUp(email, password, it) } },
            enabled = formValid && uiState !is AuthUiState.Loading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (uiState is AuthUiState.Loading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp))
            } else {
                Text("Create account")
            }
        }

        Spacer(Modifier.height(12.dp))
        TextButton(onClick = onNavigateToLogin) {
            Text("Already have an account? Log in")
        }
    }
}
