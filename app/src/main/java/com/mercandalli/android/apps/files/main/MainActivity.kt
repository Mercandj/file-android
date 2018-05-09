package com.mercandalli.android.apps.files.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.mercandalli.android.apps.files.R
import com.mercandalli.android.apps.files.bottom_bar.BottomBar
import com.mercandalli.android.apps.files.file_horizontal_lists.FileHorizontalLists
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderScriptBlur

class MainActivity : AppCompatActivity(), MainActivityContract.Screen {

    private lateinit var fileHorizontalLists: FileHorizontalLists
    private lateinit var note: View
    private lateinit var bottomBar: BottomBar
    private lateinit var userAction: MainActivityContract.UserAction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fileHorizontalLists = findViewById(R.id.activity_main_file_horizontal_lists)
        note = findViewById(R.id.activity_main_note)
        bottomBar = findViewById(R.id.activity_main_bottom_bar)

        userAction = MainActivityPresenter(
                this
        )
        bottomBar.setOnBottomBarClickListener(object : BottomBar.OnBottomBarClickListener {
            override fun onFileSectionClicked() {
                userAction.onFileSectionClicked()
            }

            override fun onNoteSectionClicked() {
                userAction.onNoteSectionClicked()
            }

            override fun onSettingsSectionClicked() {
                userAction.onSettingsSectionClicked()
            }
        })
        setBottomBarBlur()
    }

    override fun onResume() {
        super.onResume()
        fileHorizontalLists.onResume()
    }

    override fun showFileView() {
        fileHorizontalLists.visibility = View.VISIBLE
    }

    override fun hideFileView() {
        fileHorizontalLists.visibility = View.GONE
    }

    override fun showNoteView() {
        note.visibility = View.VISIBLE
    }

    override fun hideNoteView() {
        note.visibility = View.GONE
    }

    override fun showSettingsView() {

    }

    override fun hideSettingsView() {

    }

    private fun setBottomBarBlur() {
        val radius = 2f
        val decorView = window.decorView
        val rootView = decorView.findViewById<View>(android.R.id.content) as ViewGroup
        val windowBackground = decorView.background
        findViewById<BlurView>(R.id.activity_main_bottom_bar_container).setupWith(rootView)
                .windowBackground(windowBackground)
                .blurAlgorithm(RenderScriptBlur(this))
                .blurRadius(radius)
                .setHasFixedTransformationMatrix(true)
    }
}
