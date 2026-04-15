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

/**
 * Viewmodel for the credential list screen.
 * Holds UI state and handles user actions such as insert, update and delete.
 * Talks to the REPO, never directly to the Room.
 */

class VaultViewModel(private val repository: CredentialRepository) : ViewModel() {

    // Collects the Flow from the REPO and converts it to a StateFlow, such that Compose may observe it using collectAsState()
    val allCredentials: StateFlow<List<Credential>> =
        repository.allCredentials.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

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