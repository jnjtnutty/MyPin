package com.example.mypin

import android.app.Application
import com.example.mypin.di.authModule
import com.example.mypin.di.mapModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.maplibre.android.MapLibre
import org.maplibre.android.WellKnownTileServer

class MyPinApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        MapLibre.getInstance(this, null, WellKnownTileServer.MapLibre)
        startKoin {
            androidContext(this@MyPinApplication)
            modules(authModule, mapModule)
        }
    }
}
