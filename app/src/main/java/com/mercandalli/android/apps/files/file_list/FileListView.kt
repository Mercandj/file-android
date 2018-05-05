package com.mercandalli.android.apps.files.file_list

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.mercandalli.android.apps.files.main.ApplicationGraph
import com.mercandalli.sdk.files.api.File

class FileListView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), FileListContract.Screen {

    private val userAction: FileListContract.UserAction

    init {
        val fileManager = ApplicationGraph.getFileManager()
        userAction = FileListPresenter(
                this,
                fileManager)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        userAction.onAttached()
    }

    override fun onDetachedFromWindow() {
        userAction.onDetached()
        super.onDetachedFromWindow()
    }

    override fun showEmptyView() {

    }

    override fun hideEmptyView() {

    }

    override fun showErrorMessage() {

    }

    override fun hideErrorMessage() {

    }

    override fun showFiles(files: List<File>) {

    }

    override fun hideFiles() {

    }

    override fun showLoader() {

    }

    override fun hideLoader() {

    }

}