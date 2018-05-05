package com.mercandalli.android.apps.files.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.mercandalli.android.apps.files.R
import com.mercandalli.android.apps.files.file_list.FileListView

class MainActivity : AppCompatActivity() {

    private lateinit var fileListView: FileListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fileListView = findViewById(R.id.activity_main_file_list_view)
    }

    override fun onResume() {
        super.onResume()
        fileListView.onResume()
    }
}
