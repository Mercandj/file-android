package com.mercandalli.android.apps.files.file_list

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
import com.mercandalli.android.apps.files.main.ApplicationGraph
import com.mercandalli.android.apps.files.main.ApplicationGraph.Companion.init
import com.mercandalli.android.apps.files.main.MainApplication
import com.mercandalli.sdk.files.api.File

class FileListView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), FileListContract.Screen {

    private val userAction: FileListContract.UserAction
    private val adapter = FileAdapter()
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
        val fileManager = ApplicationGraph.getFileManager()
        userAction = FileListPresenter(
                this,
                fileManager,
                Environment.getExternalStorageDirectory().absolutePath)
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

    fun onResume() {
        userAction.onResume()
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

    override fun hideLoader() {
        refresh.isRefreshing = false
    }

}