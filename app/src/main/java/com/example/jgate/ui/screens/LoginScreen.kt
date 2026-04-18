package com.example.jgate.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType

/**
 * Login screen with two modes:
 * 1. Create password (first launch)
 * 2. Enter password (returning user)
 *
 * The screen doesn't interact with MasterPasswordManager directly
 * State hoisted: password values come from local state,
 * actions go up through callbacks
 */

@Composable
fun LoginScreen(
    isFirstLaunch: Boolean,
    onLogin: (String) -> Boolean,
    onCreate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var password by remember { mutableStateOf("")}
    var confirmPassword by remember { mutableStateOf("")}
    var passwordVisible by remember { mutableStateOf(false)}
    var errorMessage by remember { mutableStateOf<String?>(null)}

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App title
        Text(
            text = "Gates of Janus",
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Password field
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                errorMessage = null
            },
            label = { Text("Master Password")},
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                autoCorrectEnabled = false
            ),
            visualTransformation = if (passwordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                IconButton(onClick = {passwordVisible = !passwordVisible}) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            },
            isError = errorMessage != null
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Confirm password field

        if(isFirstLaunch) {
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    errorMessage = null
                },
                label = { Text("Confirm Password")},
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    autoCorrectEnabled = false
                ),
                visualTransformation = if (passwordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                isError = errorMessage != null
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Error messaging
        if(errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Submit button
        Button(
            onClick = {
                if(isFirstLaunch) {
                    // New password creation
                    when {
                        password.length < 6 -> {
                            errorMessage = "Password must be at least 6 characters"
                        }
                        password != confirmPassword -> {
                            errorMessage = "Passwords do not match"
                        }
                        else -> {
                            onCreate(password)
                        }
                    }
                } else {
                    // Logging in with existing password
                    val success = onLogin(password)
                    if(!success) {
                        errorMessage = "Incorrect Password"
                        password = ""
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = password.isNotBlank()
        ) {
            Text(
                text = if (isFirstLaunch) "Create Password" else "Unlock Vault"
            )
        }
    }
}