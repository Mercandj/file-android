package com.mercandalli.android.apps.files.file_column_list

import android.content.Context
import android.os.Environment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.mercandalli.android.apps.files.R
import com.mercandalli.android.apps.files.file_column_row.FileColumnRow
import com.mercandalli.android.apps.files.main.ApplicationGraph
import com.mercandalli.sdk.files.api.File

class FileColumnListView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), FileColumnListContract.Screen {

    private val userAction: FileColumnListContract.UserAction
    private var fileColumnClickListener: FileColumnRow.FileClickListener? = null
    private var fileColumnLongClickListener: FileColumnRow.FileLongClickListener? = null
    private val adapter = FileColumnAdapter(createFileClickListener())
    private val refresh: SwipeRefreshLayout
    private val recyclerView: RecyclerView
    private val emptyView: TextView
    private val error: View

    init {
        View.inflate(context, R.layout.view_file_list, this)
        refresh = findViewById(R.id.view_file_list_refresh)
        recyclerView = findViewById(R.id.view_file_list_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        emptyView = findViewById(R.id.view_file_list_empty_view)
        error = findViewById(R.id.view_file_list_error)
        userAction = createUserAction()
        refresh.setOnRefreshListener {
            userAction.onRefresh()
        }
    }

    private fun createUserAction(): FileColumnListContract.UserAction {
        if (isInEditMode) {
            return object : FileColumnListContract.UserAction {
                override fun onAttached() {}
                override fun onDetached() {}
                override fun onResume() {}
                override fun onRefresh() {}
                override fun onPathChanged(path: String) {}
                override fun onPathSelected(path: String?) {}
            }
        }
        val fileManager = ApplicationGraph.getFileManager()
        val fileSortManager = ApplicationGraph.getFileSortManager()
        return FileColumnListPresenter(
                this,
                fileManager,
                fileSortManager,
                Environment.getExternalStorageDirectory().absolutePath)
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
        emptyView.visibility = View.VISIBLE
    }

    override fun hideEmptyView() {
        emptyView.visibility = View.GONE
    }

    override fun showErrorMessage() {
        error.visibility = View.VISIBLE
    }

    override fun hideErrorMessage() {
        error.visibility = View.GONE
    }

    override fun showFiles(files: List<File>) {
        recyclerView.visibility = View.VISIBLE
        adapter.populate(files)
    }

    override fun hideFiles() {
        recyclerView.visibility = View.GONE
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

    private fun createFileClickListener(): FileColumnRow.FileClickListener {
        return object : FileColumnRow.FileClickListener {
            override fun onFileClicked(file: File) {
                fileColumnClickListener?.onFileClicked(file)
            }
        }
    }

}