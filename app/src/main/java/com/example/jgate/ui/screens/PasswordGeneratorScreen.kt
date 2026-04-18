package com.example.jgate.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import kotlin.random.Random

/**
 * Password Generator screen.
 * Allows user to configure length and char types,
 * then generates a random password matching those settings.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordGeneratorScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var passwordLength by remember { mutableFloatStateOf(16f)}
    var includeUppercase by remember { mutableStateOf(true)}
    var includeLowercase by remember {mutableStateOf(true)}
    var includeNumbers by remember { mutableStateOf(true)}
    var includeSymbols by remember { mutableStateOf(true)}
    var generatedPassword by remember { mutableStateOf("")}

    val clipboardManager = LocalClipboardManager.current

    // Initial password generation
    if(generatedPassword.isEmpty()) {
        generatedPassword = generatePassword(
            length = passwordLength.toInt(),
            uppercase = includeUppercase,
            lowercase = includeLowercase,
            numbers = includeNumbers,
            symbols = includeSymbols
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Password Generator")},
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) {
        innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Display generated password
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = generatedPassword,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontFamily = FontFamily.Monospace
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    // Copy to clipboard

                    IconButton(onClick = {
                        clipboardManager.setText(AnnotatedString(generatedPassword))
                    }) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy Password"
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Length slider

            Text(
                text = "Length: ${passwordLength.toInt()}",
                style = MaterialTheme.typography.titleMedium
            )
            Slider(
                value = passwordLength,
                onValueChange = { passwordLength = it},
                valueRange = 8f..32f,
                steps = 23,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Character type toggle menu

            Text(
                text = "Character Type Toggle Menu",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            ToggleRow(
                label = "Uppercase (A-Z)",
                checked = includeUppercase,
                onCheckedChange = { includeUppercase = it}
            )
            ToggleRow(
                label = "Lowercase (a-z)",
                checked = includeLowercase,
                onCheckedChange = { includeLowercase = it }
            )
            ToggleRow(
                label = "Numbers (0-9)",
                checked = includeNumbers,
                onCheckedChange = { includeNumbers = it }
            )
            ToggleRow(
                label = "Symbols (!@#\$%)",
                checked = includeSymbols,
                onCheckedChange = { includeSymbols = it}
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Generation button
            Button(
                onClick = {
                    generatedPassword = generatePassword(
                        length = passwordLength.toInt(),
                        uppercase = includeUppercase,
                        lowercase = includeLowercase,
                        numbers = includeNumbers,
                        symbols = includeSymbols
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.padding(4.dp))
                Text("Generate")
            }
        }
    }
}

/**
 * A row with a label and a toggle switch.
 * Extracted as a reusable composable to avoid repetition.
 */
@Composable
fun ToggleRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

/**
 * Generates a random password based on the given configuration.
 * Pure Kotlin logic, no external dependencies.
 *
 * If no character types are selected, defaults to lowercase.
 */
fun generatePassword(
    length: Int,
    uppercase: Boolean,
    lowercase: Boolean,
    numbers: Boolean,
    symbols: Boolean
): String {
    val uppercaseChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    val lowercaseChars = "abcdefghijklmnopqrstuvwxyz"
    val numberChars = "0123456789"
    val symbolChars = "!@#\$%^&*()_+-=[]{}|;:,.<>?"

    var charPool = ""
    if (uppercase) charPool += uppercaseChars
    if (lowercase) charPool += lowercaseChars
    if (numbers) charPool += numberChars
    if (symbols) charPool += symbolChars

    // Default to lowercase if nothing is selected
    if (charPool.isEmpty()) charPool = lowercaseChars

    return (1..length)
        .map { charPool[Random.nextInt(charPool.length)] }
        .joinToString("")
}