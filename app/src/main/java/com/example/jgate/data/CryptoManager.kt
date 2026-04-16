package com.example.jgate.data

import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/**
 * Handles AES-GCM encryption & decryption of credential passwords
 *
 * Uses Android Keystore to generate & store a secret key (SK) that never leaves secure hardware.
 * The key is created once on first use & is reused for all encrypt/decrypt operations.
 *
 * AES-GCM is an authenticated encryption mode: it provides both confidentiality & integrity security
 * services.
 */

class CryptoManager {

    // Android Keystore secure container for cryptographic keys

    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }

    /**
     * Gets the existing encryption key, or generates a new one if this is a first time the app
     * has been run
     */

    private fun getOrCreateKey(): SecretKey {
        // Check if the key already exists in the keystore

        val existingKey = keyStore.getEntry(KEY_ALIAS, null) as? KeyStore.SecretKeyEntry

        // if the key doesn't exist in the keystore, call generateKey
        return existingKey?.secretKey ?: generateKey()
    }

    /**
     * Generates a new AES key and stores it in the Android Keystore.
     * The key never leaves keystore.
     */
    private fun generateKey() : SecretKey {
    }

    /**
     * Encrypts a plain text string & returns a Base64-encoded string containing both the IV
     * & the ciphertext.
     *
     * Format: Base64(IV + ciphertext)
     * The IV (Initialisation Vector) is required for decryption.
     */

    fun encrypt(plainText: String): String {

    }

    /**
     * Decrypts a Base64-encoded encrypted string back to plaintext.
     * Extracts the IV from the start of the data, then decrypts the rest.
     */
    fun decrypt(encryptedText: String): String {

    }

    companion object{
        private const val KEY_ALIAS = "janus_vault_key"
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
        private const val IV_SIZE = 12
        private const val TAG_SIZE = 128
    }
}