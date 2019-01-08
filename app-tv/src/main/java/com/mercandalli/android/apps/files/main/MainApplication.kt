package com.mercandalli.android.apps.files.main

import android.app.Application

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ApplicationGraph.init(this)
    }
}
