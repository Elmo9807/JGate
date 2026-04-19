package com.example.jgate.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

/**
 * Reusable form composable shared by both Add & Edit screens.
 * Stateless, receiving all vals as parameters.
 * Changes are sent through callbacks.
 * I would like to be graded on this screen (2/2)
 */

@Composable
fun CredentialForm(
    siteName: String,
    userName: String,
    password: String,
    category: String,
    notes: String,
    onSiteNameChange: (String) -> Unit,
    onUserNameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onNotesChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    buttonText: String,
    modifier: Modifier = Modifier
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = siteName,
            onValueChange = onSiteNameChange,
            label = { Text("Site name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(autoCorrectEnabled = false)
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = userName,
            onValueChange = onUserNameChange,
            label = { Text("Username / Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                autoCorrectEnabled = false
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Password") },
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
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) {
                            Icons.Default.VisibilityOff
                        } else {
                            Icons.Default.Visibility
                        },
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            }
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = category,
            onValueChange = onCategoryChange,
            label = { Text("Category") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(autoCorrectEnabled = false)
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = notes,
            onValueChange = onNotesChange,
            label = { Text("Notes (optional)") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onSaveClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = buttonText)
        }
    }
}

/**
 * Add Credential screen uses the shared CredentialForm.
 * Holds the form state and passes it down to the form.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCredentialScreen(
    onSave: (String, String, String, String, String) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var siteName by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            JanusTopBar(
                title = "Add Credential",
                showBackButton = true,
                onBackClick = onNavigateBack
            )
        }
    ) { innerPadding ->
        CredentialForm(
            siteName = siteName,
            userName = userName,
            password = password,
            category = category,
            notes = notes,
            onSiteNameChange = { siteName = it },
            onUserNameChange = { userName = it },
            onPasswordChange = { password = it },
            onCategoryChange = { category = it },
            onNotesChange = { notes = it },
            onSaveClick = {
                if (siteName.isNotBlank() && userName.isNotBlank() && password.isNotBlank()) {
                    onSave(siteName, userName, password, category, notes)
                    onNavigateBack()
                }
            },
            buttonText = "Save",
            modifier = Modifier.padding(innerPadding)
        )
    }
}

/**
 * Edit Credential screen uses the same shared CredentialForm.
 * Pre-fills the form with existing credential data.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCredentialScreen(
    initialSiteName: String,
    initialUserName: String,
    initialPassword: String,
    initialCategory: String,
    initialNotes: String,
    onSave: (String, String, String, String, String) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var siteName by remember { mutableStateOf(initialSiteName) }
    var userName by remember { mutableStateOf(initialUserName) }
    var password by remember { mutableStateOf(initialPassword) }
    var category by remember { mutableStateOf(initialCategory) }
    var notes by remember { mutableStateOf(initialNotes) }

    Scaffold(
        topBar = {
            JanusTopBar(
                title = "Edit Credential",
                showBackButton = true,
                onBackClick = onNavigateBack
            )
        }
    ) { innerPadding ->
        CredentialForm(
            siteName = siteName,
            userName = userName,
            password = password,
            category = category,
            notes = notes,
            onSiteNameChange = { siteName = it },
            onUserNameChange = { userName = it },
            onPasswordChange = { password = it },
            onCategoryChange = { category = it },
            onNotesChange = { notes = it },
            onSaveClick = {
                if (siteName.isNotBlank() && userName.isNotBlank() && password.isNotBlank()) {
                    onSave(siteName, userName, password, category, notes)
                    onNavigateBack()
                }
            },
            buttonText = "Update",
            modifier = Modifier.padding(innerPadding)
        )
    }
}
