package com.example.jgate

import android.os.Bundle
import android.view.WindowManager
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
import com.example.jgate.ui.screens.LoginScreen
import com.example.jgate.ui.screens.PasswordGeneratorScreen
import com.example.jgate.ui.theme.JGateTheme
import com.example.jgate.viewmodel.VaultViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
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
                    val searchQuery by viewModel.searchQuery.collectAsState()
                    val navController = rememberNavController()
                    val masterPasswordManager = application.container.masterPasswordManager

                    NavHost(
                        navController = navController,
                        startDestination = JanusScreen.Login.name
                    ) {
                        // Login screen
                        composable(route = JanusScreen.Login.name) {
                            LoginScreen(
                                isFirstLaunch = !masterPasswordManager.isPasswordSet(),
                                onLogin = { password ->
                                    val success = masterPasswordManager.verifyPassword(password)
                                    if (success) {
                                        navController.navigate(JanusScreen.CredentialList.name) {
                                            popUpTo(JanusScreen.Login.name) { inclusive = true }
                                        }
                                    }
                                    success
                                },
                                onCreate = { password ->
                                    masterPasswordManager.setPassword(password)
                                    navController.navigate(JanusScreen.CredentialList.name) {
                                        popUpTo(JanusScreen.Login.name) { inclusive = true }
                                    }
                                }
                            )
                        }

                        // Main list screen

                        composable(route = JanusScreen.CredentialList.name) {
                            CredentialListScreen(
                                credentials = credentials,
                                searchQuery = searchQuery,
                                onSearchQueryChange = { viewModel.updateSearchQuery(it) },
                                onCredentialClick = { credential ->
                                    navController.navigate(
                                        JanusScreen.CredentialDetail.name + "/${credential.id}"
                                    )
                                },
                                onAddClick = {
                                    navController.navigate(JanusScreen.AddCredential.name)
                                },
                                onLogoutClick = {
                                    navController.navigate(JanusScreen.Login.name) {
                                        popUpTo(0) { inclusive = true }
                                    }
                                },
                                onGeneratorClick = {
                                    navController.navigate(JanusScreen.PasswordGenerator.name)
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
                            val credentialId = backStackEntry.arguments?.getInt("credentialId")
                                ?: return@composable

                            val credentialState by viewModel.getCredentialById(credentialId)
                                .collectAsState(initial = null)
                            val credential = credentialState ?: return@composable

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

                        // Edit credential screen - takes a credential ID as navigation argument

                        composable(
                            route = JanusScreen.EditCredential.name + "/{credentialId}",
                            arguments = listOf(
                                navArgument("credentialId") { type = NavType.IntType }
                            )
                        ) { backStackEntry ->
                            val credentialId = backStackEntry.arguments?.getInt("credentialId")
                                ?: return@composable

                            val credentialState by viewModel.getCredentialById(credentialId)
                                .collectAsState(initial = null)
                            val credential = credentialState ?: return@composable

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
                                    navController.popBackStack(
                                        route = JanusScreen.CredentialList.name,
                                        inclusive = false
                                    )
                                }
                            )
                        }

                        // Password generator screen
                        composable(route = JanusScreen.PasswordGenerator.name) {
                            PasswordGeneratorScreen(
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