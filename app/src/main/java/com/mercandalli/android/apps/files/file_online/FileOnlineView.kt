package com.mercandalli.android.apps.files.file_online

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.mercandalli.android.apps.files.file_list.FileListView
import com.mercandalli.android.apps.files.main.ApplicationGraph
import com.mercandalli.sdk.files.api.FileOpenManager

class FileOnlineView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val fileListView = FileListView(context)
    private val fileOnlineManager = ApplicationGraph.getFileOnlineManager()
    private val fileCopyCutManager = ApplicationGraph.getFileOnlineCopyCutManager()
    private val fileDeleteManager = ApplicationGraph.getFileOnlineDeleteManager()
    private val fileRenameManager = ApplicationGraph.getFileOnlineRenameManager()
    private val fileSizeManager = ApplicationGraph.getFileOnlineSizeManager()

    init {
        val fileOpenManager = createFileOpenManager()
        fileListView.setFileManagers(
                fileOnlineManager,
                fileDeleteManager,
                fileCopyCutManager,
                fileOpenManager,
                fileRenameManager,
                fileSizeManager,
                "/"
        )
        addView(fileListView)
    }

    fun getCurrentPath() = fileListView.getCurrentPath()

    fun setFileListViewSelectedFileListener(listener: FileListView.FileListViewSelectedFileListener?) {
        fileListView.setFileListViewSelectedFileListener(listener)
    }

    private fun createFileOpenManager() = object : FileOpenManager {
        override fun open(path: String, mime: String?) {
            // TODO
        }
    }
}