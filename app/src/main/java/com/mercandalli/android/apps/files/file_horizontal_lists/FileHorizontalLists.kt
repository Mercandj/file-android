package com.mercandalli.android.apps.files.file_horizontal_lists

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import com.mercandalli.android.apps.files.R
import com.mercandalli.android.apps.files.file_list.FileListView

class FileHorizontalLists @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val fileListView: FileListView

    init {
        View.inflate(context, R.layout.view_file_horizontal_lists, this)
        fileListView = findViewById(R.id.activity_main_file_list_view)
    }

    fun onResume() {
        fileListView.onResume()
    }
}