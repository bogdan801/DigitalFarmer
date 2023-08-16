package com.bogdan801.digitalfarmer.di

import android.app.Application
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        Firebase.database("https://digitalfarmer-6f2c7-default-rtdb.europe-west1.firebasedatabase.app/").setPersistenceEnabled(true)
    }
}