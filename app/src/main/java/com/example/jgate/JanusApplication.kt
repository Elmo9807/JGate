package com.example.jgate

import android.app.Application
import com.example.jgate.data.AppContainer

/**
 * Custom Application class that creates the AppContainer when the app starts.
 * This exists for the lifetime of the app, so the container and the DB are always available.
 */

class JanusApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}