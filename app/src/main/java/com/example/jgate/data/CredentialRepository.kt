package com.example.jgate.data

import kotlinx.coroutines.flow.Flow

/**
 * Repository - single source for the credentials data
 * The ViewModel(VM) speaks to this, not the DAO
 * Encryption logic will inhabit this space whenever I complete it
 */

class CredentialRepository(private val credentialDao: CredentialDao) {
    val allCredentials: Flow<List<Credential>> = credentialDao.getAllCredentials()

    fun getCredentialById(id: Int): Flow<Credential?> = credentialDao.getCredentialById(id)

    fun searchCredentials(query: String): Flow<List<Credential>> = credentialDao.searchCredentials(query)

    suspend fun insert(credential: Credential) = credentialDao.insert(credential)

    suspend fun update(credential: Credential) = credentialDao.update(credential)

    suspend fun delete(credential: Credential) = credentialDao.delete(credential)
}