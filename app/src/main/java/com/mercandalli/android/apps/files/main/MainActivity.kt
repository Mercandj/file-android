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


        val radius = 2f
        val decorView = window.decorView
        //Activity's root View. Can also be root View of your layout (preferably)
        val rootView = decorView.findViewById<View>(android.R.id.content) as ViewGroup
        //set background, if your root layout doesn't have one
        val windowBackground = decorView.background

        findViewById<BlurView>(R.id.blurView).setupWith(rootView)
                .windowBackground(windowBackground)
                .blurAlgorithm(RenderScriptBlur(this))
                .blurRadius(radius)
                .setHasFixedTransformationMatrix(true)
    }

    override fun onResume() {
        super.onResume()
        fileHorizontalLists.onResume()
    }
}
