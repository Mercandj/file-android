package com.mercandalli.android.apps.files.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.mercandalli.android.apps.files.R
import com.mercandalli.android.apps.files.file_horizontal_lists.FileHorizontalLists
import com.mercandalli.android.apps.files.file_list.FileListView

class MainActivity : AppCompatActivity() {

    private lateinit var fileHorizontalLists: FileHorizontalLists

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fileHorizontalLists = findViewById(R.id.activity_main_file_horizontal_lists)
    }

    override fun onResume() {
        super.onResume()
        fileHorizontalLists.onResume()
    }
}
