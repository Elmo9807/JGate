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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.jgate.data.Credential
import com.example.jgate.ui.screens.AddCredentialScreen
import com.example.jgate.ui.screens.CredentialDetailScreen
import com.example.jgate.ui.screens.CredentialListScreen
import com.example.jgate.ui.screens.EditCredentialScreen
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

                    val credentials by viewModel.allCredentials.collectAsState()
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
                                    // Navigate to detail screen with the credential's ID

                                    navController.navigate(
                                        JanusScreen.CredentialDetail.name + "/${credential.id}"
                                    )
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

                        // Detail screen - takes a credential ID as a navigation argument

                        composable(
                            route = JanusScreen.CredentialDetail.name + "/{credentialId}",
                            arguments = listOf(
                                navArgument("credentialId") { type = NavType.IntType }
                            )
                        ) { backStackEntry ->
                            val credentialId = backStackEntry.arguments?.getInt("credentialId") ?: return@composable
                            // Find the credential in the current list

                            val credential = credentials.find { it.id == credentialId }
                            if (credential != null) {
                                CredentialDetailScreen(
                                    credential = credential,
                                    onEditClick = {
                                        navController.navigate(
                                            JanusScreen.EditCredential.name + "/$credentialId"
                                        )
                                    },
                                    onDeleteClick = {
                                        viewModel.delete(credential)
                                    },
                                    onNavigateBack = {
                                        navController.popBackStack()
                                    }
                                )
                            }
                        }

                        // Edit credential screen - takes a credential ID as navigation argument

                        composable(
                            route = JanusScreen.EditCredential.name + "/{credentialId}",
                            arguments = listOf(
                                navArgument("credentialId") { type = NavType.IntType }
                            )
                        ) { backStackEntry ->
                            val credentialId = backStackEntry.arguments?.getInt("credentialId") ?: return@composable
                            val credential = credentials.find { it.id == credentialId }
                            if (credential != null) {
                                EditCredentialScreen(
                                    initialSiteName = credential.siteName,
                                    initialUserName = credential.userName,
                                    initialPassword = credential.encryptedPassword,
                                    initialCategory = credential.category,
                                    initialNotes = credential.notes,
                                    onSave = { siteName, userName, password, category, notes ->
                                        viewModel.update(
                                            credential.copy(
                                                siteName = siteName,
                                                userName = userName,
                                                encryptedPassword = password,
                                                category = category,
                                                notes = notes,
                                                dateModified = System.currentTimeMillis()
                                            )
                                        )
                                    },
                                    onNavigateBack = {
                                        // Pop back twice to return to the list, not the detail screen
                                        // (since the credential data it was showing is now stale)

                                        navController.popBackStack(
                                            route = JanusScreen.CredentialList.name,
                                            inclusive = false
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}