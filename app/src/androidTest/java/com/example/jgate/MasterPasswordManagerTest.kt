package com.example.jgate
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Before

import org.junit.Assert.*

import com.example.jgate.data.MasterPasswordManager

@RunWith(AndroidJUnit4::class)
class MasterPasswordManagerTest {

    private lateinit var manager: MasterPasswordManager

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        context.getSharedPreferences("janus_vault_prefs", 0).edit().clear().apply()
        manager = MasterPasswordManager(context)
    }

    @Test
    fun isPasswordSetReturnsFalseInitially() {
        assertFalse(manager.isPasswordSet())
    }

    @Test
    fun isPasswordSetReturnsTrueAfterSetting() {
        manager.setPassword("testpass123")
        assertTrue(manager.isPasswordSet())
    }

    @Test
    fun verifyCorrectPasswordReturnsTrue() {
        manager.setPassword("mypassword")
        assertTrue(manager.verifyPassword("mypassword"))
    }

    @Test
    fun verifyWrongPasswordReturnsFalse() {
        manager.setPassword("mypassword")
        assertFalse(manager.verifyPassword("wrongpassword"))
    }

    @Test
    fun verifyPasswordIsCaseSensitive() {
        manager.setPassword("MyPassword")
        assertFalse(manager.verifyPassword("mypassword"))
        assertFalse(manager.verifyPassword("MYPASSWORD"))
    }
}