package com.example.jgate.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repository - single source of truth for credential data.
 * Handles encryption and decryption of passwords transparently.
 * The ViewModel and UI only ever see plain text passwords;
 * the database only ever stores encrypted ones.
 */
class CredentialRepository(
    private val credentialDao: CredentialDao,
    private val cryptoManager: CryptoManager = CryptoManager()
) {

    /**
     * Returns all credentials with passwords decrypted.
     * The Flow automatically emits a new list whenever the database changes,
     * and we map over each list to decrypt the passwords on the fly.
     */
    val allCredentials: Flow<List<Credential>> =
        credentialDao.getAllCredentials().map { list ->
            list.map { credential ->
                credential.copy(encryptedPassword = cryptoManager.decrypt(credential.encryptedPassword))
            }
        }

    fun getCredentialById(id: Int): Flow<Credential?> =
        credentialDao.getCredentialById(id).map { credential ->
            credential?.copy(encryptedPassword = cryptoManager.decrypt(credential.encryptedPassword))
        }

    /**
     * Encrypts the password before saving to the database.
     */
    suspend fun insert(credential: Credential) {
        val encrypted = credential.copy(
            encryptedPassword = cryptoManager.encrypt(credential.encryptedPassword)
        )
        credentialDao.insert(encrypted)
    }

    suspend fun update(credential: Credential) {
        val encrypted = credential.copy(
            encryptedPassword = cryptoManager.encrypt(credential.encryptedPassword)
        )
        credentialDao.update(encrypted)
    }

    suspend fun delete(credential: Credential) = credentialDao.delete(credential)
}