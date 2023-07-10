package com.aivle.funibuni

import android.app.Application
import com.aivle.presentation.intro.firebase.FirebasePhoneAuthManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        FirebasePhoneAuthManager.initializeFirebaseApp(this)
    }
}