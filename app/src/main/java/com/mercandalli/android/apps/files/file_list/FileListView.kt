@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.file_list

import android.content.Context
import android.os.Environment
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mercandalli.android.apps.files.R
import com.mercandalli.android.apps.files.file.FileProvider
import com.mercandalli.android.apps.files.file_list_row.FileListRow
import com.mercandalli.android.apps.files.main.ApplicationGraph
import com.mercandalli.sdk.files.api.File
import com.mercandalli.sdk.files.api.FileChildrenManager
import com.mercandalli.sdk.files.api.FileDeleteManager
import com.mercandalli.sdk.files.api.FileCopyCutManager
import com.mercandalli.sdk.files.api.FileOpenManager
import com.mercandalli.sdk.files.api.FileRenameManager
import com.mercandalli.sdk.files.api.FileSizeManager

class FileListView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), FileListContract.Screen {

    private val view = View.inflate(context, R.layout.view_file_list, this)
    private val refresh: SwipeRefreshLayout = view.findViewById(R.id.view_file_list_refresh)
    private val recyclerView: RecyclerView = view.findViewById(R.id.view_file_list_recycler_view)
    private val emptyTextView: TextView = view.findViewById(R.id.view_file_list_empty_view)
    private val errorTextView: TextView = view.findViewById(R.id.view_file_list_error)
    private val fab: FloatingActionButton = view.findViewById(R.id.view_file_list_fab)
    private val adapter = FileListAdapter(createFileClickListener())
    private val userAction = createUserAction()

    private var fileLongClickListener: FileListRow.FileLongClickListener? = null
    private var fileListViewSelectedFileListener: FileListViewSelectedFileListener? = null

    init {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        refresh.setOnRefreshListener {
            userAction.onRefresh()
        }
        fab.setOnClickListener {
            userAction.onFabUpArrowClicked()
        }
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
        emptyTextView.visibility = VISIBLE
    }

    override fun hideEmptyView() {
        emptyTextView.visibility = GONE
    }

    override fun showErrorMessage() {
        errorTextView.visibility = VISIBLE
    }

    override fun hideErrorMessage() {
        errorTextView.visibility = GONE
    }

    override fun showFiles(files: List<File>) {
        recyclerView.visibility = VISIBLE
        adapter.populate(files)
    }

    override fun hideFiles() {
        recyclerView.visibility = GONE
    }

    override fun showLoader() {
        refresh.isRefreshing = true
    }

    override fun selectPath(path: String?) {
        adapter.selectPath(path)
    }

    override fun hideLoader() {
        refresh.isRefreshing = false
    }

    override fun showFabUpArrow() {
        fab.show()
    }

    override fun hideFabUpArrow() {
        fab.hide()
    }

    override fun setEmptyTextColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        emptyTextView.setTextColor(color)
    }

    override fun setErrorTextColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        errorTextView.setTextColor(color)
    }

    override fun notifyListenerCurrentPathChanged(currentPath: String) {
        fileListViewSelectedFileListener?.onSelectedFilePathChanged(currentPath)
    }

    fun setFileLongClickListener(listener: FileListRow.FileLongClickListener?) {
        fileLongClickListener = listener
    }

    fun setFileListViewSelectedFileListener(listener: FileListViewSelectedFileListener?) {
        fileListViewSelectedFileListener = listener
    }

    fun getCurrentPath() = userAction.getCurrentPath()

    fun setFileManagers(
        fileProvider: FileProvider,
        fileChildrenManager: FileChildrenManager,
        fileDeleteManager: FileDeleteManager,
        fileCopyCutManager: FileCopyCutManager,
        fileOpenManager: FileOpenManager,
        fileRenameManager: FileRenameManager,
        fileSizeManager: FileSizeManager,
        rootPath: String
    ) {
        userAction.onSetFileManagers(
            fileChildrenManager,
            fileOpenManager,
            rootPath
        )
        adapter.setFileManagers(
            fileProvider,
            fileCopyCutManager,
            fileDeleteManager,
            fileRenameManager,
            fileSizeManager
        )
    }

    private fun createFileClickListener() = object : FileListRow.FileClickListener {
        override fun onFileClicked(file: File) {
            userAction.onFileClicked(file)
        }
    }

    private fun createUserAction() = if (isInEditMode) {
        object : FileListContract.UserAction {
            override fun onAttached() {}
            override fun onDetached() {}
            override fun onRefresh() {}
            override fun onFileClicked(file: File) {}
            override fun onFabUpArrowClicked() {}
            override fun onSetFileManagers(
                fileChildrenManager: FileChildrenManager,
                fileOpenManager: FileOpenManager,
                rootPath: String
            ) {
            }

            override fun getCurrentPath() = "/"
        }
    } else {
        val fileChildrenManager = ApplicationGraph.getFileChildrenManager()
        val fileOpenManager = ApplicationGraph.getFileOpenManager()
        val fileSortManager = ApplicationGraph.getFileSortManager()
        val themeManager = ApplicationGraph.getThemeManager()
        FileListPresenter(
            this,
            fileChildrenManager,
            fileOpenManager,
            fileSortManager,
            themeManager,
            Environment.getExternalStorageDirectory().absolutePath
        )
    }

    interface FileListViewSelectedFileListener {

        fun onSelectedFilePathChanged(path: String?)
    }
}
