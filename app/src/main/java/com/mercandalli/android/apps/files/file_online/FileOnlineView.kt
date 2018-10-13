package com.mercandalli.android.apps.files.file_online

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.mercandalli.android.apps.files.file_list.FileListView
import com.mercandalli.android.apps.files.main.ApplicationGraph
import com.mercandalli.sdk.files.api.FileCopyCutManager
import com.mercandalli.sdk.files.api.FileOpenManager

class FileOnlineView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val fileColumnListView = FileListView(context)
    private val fileOnlineManager = ApplicationGraph.getFileOnlineManager()
    private val fileDeleteManager = ApplicationGraph.getFileOnlineDeleteManager()
    private val fileRenameManager = ApplicationGraph.getFileOnlineRenameManager()
    private val fileSizeManager = ApplicationGraph.getFileOnlineSizeManager()

    init {
        val fileOpenManager = createFileOpenManager()
        val fileCopyCutManager = createFileCopyCutManager()
        fileColumnListView.setFileManagers(
                fileOnlineManager,
                fileDeleteManager,
                fileCopyCutManager,
                fileOpenManager,
                fileRenameManager,
                fileSizeManager,
                "/"
        )
        addView(fileColumnListView)
    }

    fun getCurrentPath() = fileColumnListView.getCurrentPath()

    private fun createFileCopyCutManager() = object : FileCopyCutManager {
        override fun copy(path: String) {

        }

        override fun copy(pathInput: String, pathDirectoryOutput: String) {

        }

        override fun cut(path: String) {

        }

        override fun cut(pathInput: String, pathDirectoryOutput: String) {

        }

        override fun getFileToPastePath(): String? {
            return null
        }

        override fun paste(pathDirectoryOutput: String) {

        }

        override fun cancelCopyCut() {

        }

        override fun registerFileToPasteChangedListener(listener: FileCopyCutManager.FileToPasteChangedListener) {

        }

        override fun unregisterFileToPasteChangedListener(listener: FileCopyCutManager.FileToPasteChangedListener) {

        }

        override fun registerPasteListener(listener: FileCopyCutManager.PasteListener) {

        }

        override fun unregisterPasteListener(listener: FileCopyCutManager.PasteListener) {

        }

    }

    private fun createFileOpenManager() = object : FileOpenManager {
        override fun open(path: String, mime: String?) {

        }
    }
}