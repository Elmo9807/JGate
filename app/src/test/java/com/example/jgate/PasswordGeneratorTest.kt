package com.example.jgate

import com.example.jgate.ui.screens.generatePassword
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for the password generator function.
 * These run on the JVM without an emulator.
 */
class PasswordGeneratorTest {

    @Test
    fun generatedPassword_correctLength() {
        val password = generatePassword(16, true, true, true, true)
        assertEquals(16, password.length)
    }

    @Test
    fun generatedPassword_shortLength() {
        val password = generatePassword(8, true, true, true, true)
        assertEquals(8, password.length)
    }

    @Test
    fun generatedPassword_maxLength() {
        val password = generatePassword(32, true, true, true, true)
        assertEquals(32, password.length)
    }

    @Test
    fun generatedPassword_uppercaseOnly() {
        val password = generatePassword(20, true, false, false, false)
        assertTrue(password.all { it in 'A'..'Z' })
    }

    @Test
    fun generatedPassword_lowercaseOnly() {
        val password = generatePassword(20, false, true, false, false)
        assertTrue(password.all { it in 'a'..'z' })
    }

    @Test
    fun generatedPassword_numbersOnly() {
        val password = generatePassword(20, false, false, true, false)
        assertTrue(password.all { it in '0'..'9' })
    }

    @Test
    fun generatedPassword_symbolsOnly() {
        val symbols = "!@#\$%^&*()_+-=[]{}|;:,.<>?"
        val password = generatePassword(20, false, false, false, true)
        assertTrue(password.all { it in symbols })
    }

    @Test
    fun generatedPassword_noSelectionDefaultsToLowercase() {
        val password = generatePassword(20, false, false, false, false)
        assertTrue(password.all { it in 'a'..'z' })
    }

    @Test
    fun generatedPassword_containsMixedCharacters() {
        // Generate a long password to increase probability of all types appearing
        val password = generatePassword(100, true, true, true, true)
        assertTrue("Should contain uppercase", password.any { it in 'A'..'Z' })
        assertTrue("Should contain lowercase", password.any { it in 'a'..'z' })
        assertTrue("Should contain numbers", password.any { it in '0'..'9' })
    }
}