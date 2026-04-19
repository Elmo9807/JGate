package com.example.jgate

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Before

import org.junit.Assert.*

import com.example.jgate.data.CryptoManager

@RunWith(AndroidJUnit4::class)
class CryptoManagerTest {

    private lateinit var cryptoManager: CryptoManager

    @Before
    fun setup() {
        cryptoManager = CryptoManager()
    }

    @Test
    fun encryptThenDecryptReturnsOriginal() {
        val original = "MySecurePassword123!"
        val encrypted = cryptoManager.encrypt(original)
        val decrypted = cryptoManager.decrypt(encrypted)
        assertEquals(original, decrypted)
    }

    @Test
    fun encryptProducesNonPlaintext() {
        val original = "TestPassword"
        val encrypted = cryptoManager.encrypt(original)
        assertNotEquals(original, encrypted)
    }

    @Test
    fun encryptSameInputProducesDifferentOutput() {
        val original = "SamePassword"
        val encrypted1 = cryptoManager.encrypt(original)
        val encrypted2 = cryptoManager.encrypt(original)
        assertNotEquals(encrypted1, encrypted2)
    }

    @Test
    fun encryptThenDecryptSpecialCharacters() {
        val original = "p@ssw0rd!#%^&*()"
        val encrypted = cryptoManager.encrypt(original)
        val decrypted = cryptoManager.decrypt(encrypted)
        assertEquals(original, decrypted)
    }

    @Test
    fun encryptThenDecryptLongPassword() {
        val original = "A".repeat(500)
        val encrypted = cryptoManager.encrypt(original)
        val decrypted = cryptoManager.decrypt(encrypted)
        assertEquals(original, decrypted)
    }
}