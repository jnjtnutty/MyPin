package com.example.mypin

import android.app.Application
import com.example.mypin.di.appModule
import com.example.mypin.di.authModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyPinApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyPinApp)
            modules(appModule, authModule)
        }
    }
}
