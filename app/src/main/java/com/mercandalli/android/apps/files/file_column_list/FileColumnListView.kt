@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.file_column_list

import android.content.Context
import android.os.Environment
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.recyclerview.widget.RecyclerView
import com.mercandalli.android.apps.files.R
import com.mercandalli.android.apps.files.file_column_row.FileColumnRow
import com.mercandalli.android.apps.files.main.ApplicationGraph
import com.mercandalli.sdk.files.api.File
import com.mercandalli.sdk.files.api.FileChildrenManager

class FileColumnListView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), FileColumnListContract.Screen {

    private val view = View.inflate(context, R.layout.view_file_column_list, this)
    private val refresh: SwipeRefreshLayout = view.findViewById(R.id.view_file_column_list_refresh)
    private val recyclerView: RecyclerView = view.findViewById(R.id.view_file_column_list_recycler_view)
    private val emptyTextView: TextView = view.findViewById(R.id.view_file_column_list_empty_view)
    private val errorTextView: TextView = view.findViewById(R.id.view_file_column_list_error)
    private val adapter = FileColumnAdapter(createFileClickListener())
    private val userAction = createUserAction()

    private var fileColumnClickListener: FileColumnRow.FileClickListener? = null
    private var fileColumnLongClickListener: FileColumnRow.FileLongClickListener? = null

    init {
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        recyclerView.adapter = adapter
        refresh.setOnRefreshListener {
            userAction.onRefresh()
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

    override fun setEmptyTextColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        emptyTextView.setTextColor(color)
    }

    override fun setErrorTextColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        errorTextView.setTextColor(color)
    }

    fun onResume() {
        userAction.onResume()
    }

    fun setPath(path: String) {
        userAction.onPathChanged(path)
    }

    fun setFileClickListener(listener: FileColumnRow.FileClickListener?) {
        fileColumnClickListener = listener
    }

    fun setFileLongClickListener(listener: FileColumnRow.FileLongClickListener?) {
        fileColumnLongClickListener = listener
    }

    fun onPathSelected(path: String?) {
        userAction.onPathSelected(path)
    }

    fun setFileManager(fileChildrenManager: FileChildrenManager) {
        userAction.onSetFileManager(fileChildrenManager)
    }

    private fun createUserAction() = if (isInEditMode) {
        object : FileColumnListContract.UserAction {
            override fun onAttached() {}
            override fun onDetached() {}
            override fun onResume() {}
            override fun onRefresh() {}
            override fun onPathChanged(path: String) {}
            override fun onPathSelected(path: String?) {}
            override fun onSetFileManager(fileChildrenManager: FileChildrenManager) {}
        }
    } else {
        val fileChildrenManager = ApplicationGraph.getFileChildrenManager()
        val fileSortManager = ApplicationGraph.getFileSortManager()
        val themeManager = ApplicationGraph.getThemeManager()
        FileColumnListPresenter(
            this,
            fileChildrenManager,
            fileSortManager,
            themeManager,
            Environment.getExternalStorageDirectory().absolutePath
        )
    }

    private fun createFileClickListener() = object : FileColumnRow.FileClickListener {
        override fun onFileClicked(file: File) {
            fileColumnClickListener?.onFileClicked(file)
        }
    }
}
