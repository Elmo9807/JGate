package com.example.jgate.data

import android.content.Context
import java.security.MessageDigest
import android.util.Base64

/**
 * Manages the master password using SharedPreferences
 *
 * The Master Password is never stored directly. Instead, it is hashed with SHA-256 and the hash is
 * stored. On login, the entered password is hashed and compared to the stored hash.
 *
 * Even if someone accessed the SharedPreferences file, they would only extract the hash, not the actual password.
 * SHA-256 is a one-way function so the password can't be recovered from the hash.
 */

class MasterPasswordManager(context: Context) {

    private val sharedPreferences = context.getSharedPreferences(
        "janus_vault_prefs",
        Context.MODE_PRIVATE
    )

    /**
     * Checks whether a master password has been set.
     * Returns false on first launch, true on subsequent launches.
     */
    fun isPasswordSet(): Boolean {
        return sharedPreferences.contains(KEY_PASSWORD_HASH)
    }

    /**
     * Hashes and stores the master password
     * Called once when the user creates their password on first launch
     */
    fun setPassword(password: String) {
        val hash = hashPassword(password)
        sharedPreferences.edit().putString(KEY_PASSWORD_HASH, hash).apply()
    }

    /**
     * Verifies the entered password against the stored hash
     * Returns true if the password is correct
     */
    fun verifyPassword(password: String): Boolean {
        val storedHash = sharedPreferences.getString(KEY_PASSWORD_HASH, null)
        val enteredHash = hashPassword(password)
        return storedHash == enteredHash
    }

    /**
     * Hashes a password using SHA-256
     * Returns a Base64-encoded string of the hash
     * SHA-256 is a one-way function: you can verify a password
     * against the hash but cannot recover the password from the hash
     */
    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(password.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(hashBytes, Base64.NO_WRAP)
    }

    companion object {
        private const val KEY_PASSWORD_HASH = "master_password_hash"
    }
}