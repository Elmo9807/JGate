package com.example.jgate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jgate.data.Credential
import com.example.jgate.ui.screens.AddCredentialScreen
import com.example.jgate.ui.screens.CredentialListScreen
import com.example.jgate.ui.screens.JanusScreen
import com.example.jgate.ui.theme.JGateTheme
import com.example.jgate.viewmodel.VaultViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JGateTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Get the repository from the app container
                    val application = applicationContext as JanusApplication
                    val viewModel: VaultViewModel = viewModel(
                        factory = VaultViewModel.provideFactory(
                            application.container.credentialRepository
                        )
                    )

                    // Collect the credential list as Compose state
                    val credentials by viewModel.allCredentials.collectAsState()

                    // Set up navigation
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = JanusScreen.CredentialList.name
                    ) {
                        // Main list screen
                        composable(route = JanusScreen.CredentialList.name) {
                            CredentialListScreen(
                                credentials = credentials,
                                onCredentialClick = { credential ->
                                    // We'll wire this to detail screen later
                                },
                                onAddClick = {
                                    navController.navigate(JanusScreen.AddCredential.name)
                                }
                            )
                        }

                        // Add credential screen
                        composable(route = JanusScreen.AddCredential.name) {
                            AddCredentialScreen(
                                onSave = { siteName, userName, password, category, notes ->
                                    viewModel.insert(
                                        Credential(
                                            siteName = siteName,
                                            userName = userName,
                                            encryptedPassword = password,
                                            category = category,
                                            notes = notes
                                        )
                                    )
                                },
                                onNavigateBack = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}