package com.example.tryanimation.try_architecture_code.base

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.clj.fastble.BleManager
import com.example.tryanimation.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        BleManager.getInstance().init(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}