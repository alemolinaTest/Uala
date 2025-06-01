package com.amolina.uala.ui


import android.app.Application
import com.amolina.data.di.databaseModule
import com.amolina.data.di.networkModule
import com.amolina.data.di.useCaseModule
import com.amolina.presentation.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class UalaApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@UalaApp)
            modules(listOf(networkModule, databaseModule, useCaseModule, viewModelModule))
        }
    }
}