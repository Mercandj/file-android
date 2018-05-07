package com.mercandalli.android.apps.files.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.mercandalli.android.apps.files.R
import com.mercandalli.android.apps.files.file_horizontal_lists.FileHorizontalLists
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderScriptBlur

class MainActivity : AppCompatActivity() {

    private lateinit var fileHorizontalLists: FileHorizontalLists

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fileHorizontalLists = findViewById(R.id.activity_main_file_horizontal_lists)
        setBottomBarBlur()
    }

    override fun onResume() {
        super.onResume()
        fileHorizontalLists.onResume()
    }

    private fun setBottomBarBlur() {
        val radius = 2f
        val decorView = window.decorView
        val rootView = decorView.findViewById<View>(android.R.id.content) as ViewGroup
        val windowBackground = decorView.background
        findViewById<BlurView>(R.id.activity_main_bottom_bar).setupWith(rootView)
                .windowBackground(windowBackground)
                .blurAlgorithm(RenderScriptBlur(this))
                .blurRadius(radius)
                .setHasFixedTransformationMatrix(true)
    }
}
