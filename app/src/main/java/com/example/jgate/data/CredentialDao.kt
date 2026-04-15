package com.example.jgate.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object: Defines all database operations
 * Room is used to generate SQL implementation based on the annotation
 */

@Dao
interface CredentialDao {

    // Flow emits a new list each time a table change is detected
    @Query("SELECT * FROM credentials ORDER BY dateModified DESC")
    fun getAllCredentials(): Flow<List<Credential>>

    @Query("SELECT * FROM credentials WHERE id = :id")
    fun getCredentialById(id: Int): Flow<Credential?>

    @Query("SELECT * FROM credentials WHERE siteName LIKE '%' || :query || '%' ORDER BY dateModified DESC")
    fun searchCredentials(query: String): Flow<List<Credential>>

    // suspend runs on background thread through coroutine
    @Insert
    suspend fun insert(credential: Credential)

    @Update
    suspend fun update(credential: Credential)

    @Delete
    suspend fun delete(credential: Credential)
}