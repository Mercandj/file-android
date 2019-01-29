package com.mercandalli.android.apps.files.main

import android.app.Activity
import android.os.Bundle
import com.mercandalli.android.apps.files.R

class MainActivity :
    Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
