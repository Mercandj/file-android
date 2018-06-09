package com.mercandalli.android.apps.files.file_list

import android.content.Context
import android.os.Environment
import android.support.design.widget.FloatingActionButton
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.mercandalli.android.apps.files.R
import com.mercandalli.android.apps.files.file_list_row.FileListRow
import com.mercandalli.android.apps.files.main.ApplicationGraph
import com.mercandalli.sdk.files.api.File

class FileListView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), FileListContract.Screen {

    private val userAction: FileListContract.UserAction
    private var fileLongClickListener: FileListRow.FileLongClickListener? = null
    private val adapter = FileListAdapter(createFileClickListener())
    private val refresh: SwipeRefreshLayout
    private val recyclerView: RecyclerView
    private val emptyView: TextView
    private val error: View
    private val fab: FloatingActionButton

    init {
        View.inflate(context, R.layout.view_file_list, this)
        refresh = findViewById(R.id.view_file_list_refresh)
        recyclerView = findViewById(R.id.view_file_list_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = ScaleAnimationAdapter(recyclerView, adapter)
        emptyView = findViewById(R.id.view_file_list_empty_view)
        error = findViewById(R.id.view_file_list_error)
        fab = findViewById(R.id.view_file_list_fab)
        userAction = createUserAction()
        refresh.setOnRefreshListener {
            userAction.onRefresh()
        }
        fab.setOnClickListener {
            userAction.onFabUpArrowClicked()
        }
    }

    private fun createUserAction(): FileListContract.UserAction {
        if (isInEditMode) {
            return object : FileListContract.UserAction {
                override fun onAttached() {}
                override fun onDetached() {}
                override fun onResume() {}
                override fun onRefresh() {}
                override fun onFileClicked(file: File) {}
                override fun onFabUpArrowClicked() {}
            }
        }
        val fileManager = ApplicationGraph.getFileManager()
        val fileSortManager = ApplicationGraph.getFileSortManager()
        return FileListPresenter(
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
        recyclerView.adapter = ScaleAnimationAdapter(recyclerView, adapter)
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

    override fun showFabUpArrow() {
        fab.show()
    }

    override fun hideFabUpArrow() {
        fab.hide()
    }

    fun onResume() {
        userAction.onResume()
    }

    fun setFileLongClickListener(listener: FileListRow.FileLongClickListener?) {
        fileLongClickListener = listener
    }

    private fun createFileClickListener(): FileListRow.FileClickListener {
        return object : FileListRow.FileClickListener {
            override fun onFileClicked(file: File) {
                userAction.onFileClicked(file)
            }
        }
    }

}