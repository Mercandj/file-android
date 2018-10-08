package com.mercandalli.android.apps.files.file_online

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.mercandalli.android.apps.files.file_column_list.FileColumnListView

class FileOnlineView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        val fileColumnListView = FileColumnListView(context)
        //val fileOnlineManager = ApplicationGraph.getFileOnlineManager()
        //fileColumnListView.setFileManager(
        //        fileOnlineManager
        //)
        addView(fileColumnListView)
    }
}