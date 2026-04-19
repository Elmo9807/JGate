package com.example.jgate.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Single stored credential in the Vault of Janus
 */

@Entity(tableName = "credentials")
data class Credential(

    // Assign an unique ID automatically
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val siteName: String,
    val userName: String,
    val encryptedPassword: String,
    val category: String,
    val notes: String = "",
    val dateAdded: Long = System.currentTimeMillis(),
    val dateModified: Long = System.currentTimeMillis()
)