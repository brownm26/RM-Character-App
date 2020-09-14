package com.mbrown.myapplication

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}