package com.example.jgate.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.jgate.data.Credential
import com.example.jgate.data.CredentialRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

/**
 * Viewmodel for the credential list screen.
 * Holds UI state and handles user actions such as insert, update and delete.
 * Talks to the REPO, never directly to the Room.
 */

class VaultViewModel(private val repository: CredentialRepository) : ViewModel() {

    // Search query state
    val searchQuery = MutableStateFlow("")

    // Credentials filtered by search query
    val allCredentials: StateFlow<List<Credential>> =
        combine(repository.allCredentials, searchQuery) { credentials, query ->
            if (query.isBlank()) {
                credentials
            } else {
                credentials.filter { credential ->
                    credential.siteName.contains(query, ignoreCase = true) ||
                            credential.userName.contains(query, ignoreCase = true) ||
                            credential.category.contains(query, ignoreCase = true)
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun updateSearchQuery(query: String) {
        searchQuery.value = query
    }

    // Each op starts a coroutine so it runs on a background thread
    fun insert(credential: Credential) {
        viewModelScope.launch{
            repository.insert(credential)
        }
    }

    fun update(credential: Credential) {
        viewModelScope.launch {
            repository.update(credential)
        }
    }

    fun delete(credential: Credential) {
        viewModelScope.launch {
            repository.delete(credential)
        }
    }

    /**
     * Gets a single credential by way of ID
     * Returns a Flow such that detail screen updates reactively if the cred is edited elsewhere
     */

    fun getCredentialById(id: Int) : Flow<Credential?> {
        return repository.getCredentialById(id)
    }

    /**
     * Factory that creates VaultViewModel with the repository.
     */

    companion object{
        fun provideFactory(repository: CredentialRepository): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return VaultViewModel(repository) as T
                }
            }
        }
    }
}