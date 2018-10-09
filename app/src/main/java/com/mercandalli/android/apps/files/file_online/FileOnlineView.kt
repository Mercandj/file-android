package com.mercandalli.android.apps.files.file_online

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.mercandalli.android.apps.files.file_list.FileListView
import com.mercandalli.android.apps.files.main.ApplicationGraph
import com.mercandalli.sdk.files.api.FileCopyCutManager
import com.mercandalli.sdk.files.api.FileDeleteManager
import com.mercandalli.sdk.files.api.FileOpenManager
import com.mercandalli.sdk.files.api.FileRenameManager

class FileOnlineView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val fileColumnListView = FileListView(context)
    private val fileOnlineManager = ApplicationGraph.getFileOnlineManager()

    init {
        fileColumnListView.setFileManagers(
                fileOnlineManager,
                object : FileOpenManager {
                    override fun open(path: String, mime: String?) {

                    }
                },
                object : FileDeleteManager {
                    override fun delete(path: String): Boolean {
                        return false
                    }
                },
                object : FileCopyCutManager {
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

                },
                object : FileRenameManager {
                    override fun rename(path: String, fileName: String) {

                    }
                }
        )
        addView(fileColumnListView)
    }
}