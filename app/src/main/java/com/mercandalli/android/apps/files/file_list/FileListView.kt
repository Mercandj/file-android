package com.mercandalli.android.apps.files.file_list

import android.content.Context
import android.os.Environment
import android.support.annotation.IdRes
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.ContextCompat
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

    private val adapter = FileListAdapter(createFileClickListener())
    private val refresh: SwipeRefreshLayout by bind(R.id.view_file_list_refresh)
    private val recyclerView: RecyclerView by bind(R.id.view_file_list_recycler_view)
    private val emptyTextView: TextView by bind(R.id.view_file_list_empty_view)
    private val errorTextView: TextView by bind(R.id.view_file_list_error)
    private val fab: FloatingActionButton by bind(R.id.view_file_list_fab)
    private val userAction = createUserAction()

    private var fileLongClickListener: FileListRow.FileLongClickListener? = null
    private var fileListViewSelectedFileListener: FileListViewSelectedFileListener? = null

    init {
        View.inflate(context, R.layout.view_file_list, this)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = ScaleAnimationAdapter(recyclerView, adapter)
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
        emptyTextView.visibility = View.VISIBLE
    }

    override fun hideEmptyView() {
        emptyTextView.visibility = View.GONE
    }

    override fun showErrorMessage() {
        errorTextView.visibility = View.VISIBLE
    }

    override fun hideErrorMessage() {
        errorTextView.visibility = View.GONE
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

    override fun setEmptyTextColorRes(textColorRes: Int) {
        emptyTextView.setTextColor(ContextCompat.getColor(context, textColorRes))
    }

    override fun setErrorTextColorRes(textColorRes: Int) {
        errorTextView.setTextColor(ContextCompat.getColor(context, textColorRes))
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

    private fun createUserAction() = if (isInEditMode) {
        object : FileListContract.UserAction {
            override fun onAttached() {}
            override fun onDetached() {}
            override fun onRefresh() {}
            override fun onFileClicked(file: File) {}
            override fun onFabUpArrowClicked() {}
        }
    } else {
        val fileManager = ApplicationGraph.getFileManager()
        val fileOpenManager = ApplicationGraph.getFileOpenManager()
        val fileSortManager = ApplicationGraph.getFileSortManager()
        val themeManager = ApplicationGraph.getThemeManager()
        FileListPresenter(
                this,
                fileManager,
                fileOpenManager,
                fileSortManager,
                themeManager,
                Environment.getExternalStorageDirectory().absolutePath
        )
    }

    private fun createFileClickListener() = object : FileListRow.FileClickListener {
        override fun onFileClicked(file: File) {
            userAction.onFileClicked(file)
        }
    }

    private fun <T : View> View.bind(@IdRes res: Int): Lazy<T> {
        @Suppress("UNCHECKED_CAST")
        return lazy(LazyThreadSafetyMode.NONE) { findViewById<T>(res) }
    }

    interface FileListViewSelectedFileListener {
        fun onSelectedFilePathChanged(path: String?)
    }
}