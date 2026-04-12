package com.example.jgate.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Credential::class], version = 1, exportSchema = false)
abstract class VaultDatabase : RoomDatabase() {

    abstract fun credentialDao(): CredentialDao

    // Room generates this
    companion object {
        @Volatile
        private var INSTANCE: VaultDatabase? = null

        /**
         * Returns the single database instance
         * Synced patterns prevents double instantiation from separate threads
         */
        fun getDatabase(context: Context): VaultDatabase {
            // Return INSTANCE if it is not null, otherwise, enter synced block
            // Synced block used to prevent simultaneous access, enforcing singleton pattern
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VaultDatabase::class.java,
                    "janus_vault_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}