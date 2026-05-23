package com.example.mypin

import android.app.Application
import com.example.mypin.di.addPinModule
import com.example.mypin.di.authModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyPinApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyPinApplication)
            modules(authModule, addPinModule)
        }
    }
}
