package com.example.jgate.data

import android.content.Context

/**
 * App container for dependency injection
 * Creates the DB and REPO once, so the rest of the app
 * can access without creating their own versions
 */

class AppContainer(context: Context) {
    private val database = VaultDatabase.getDatabase(context)
    val credentialRepository = CredentialRepository(database.credentialDao())
}