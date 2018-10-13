package com.mercandalli.android.apps.files.file_list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.mercandalli.android.apps.files.file_list_row.FileListRow
import com.mercandalli.sdk.files.api.*

class FileListAdapter(
        fileListClickListener: FileListRow.FileClickListener?
) : ListDelegationAdapter<List<Any>>() {

    private val fileAdapterDelegate = FileAdapterDelegate(fileListClickListener)

    init {
        delegatesManager.addDelegate(fileAdapterDelegate as AdapterDelegate<List<Any>>)
    }

    fun populate(list: List<Any>) {
        setItems(list)
        notifyDataSetChanged()
    }

    fun selectPath(path: String?) {
        fileAdapterDelegate.selectPath(path)
        notifyDataSetChanged()
    }

    fun setFileManagers(
            fileCopyCutManager: FileCopyCutManager,
            fileDeleteManager: FileDeleteManager,
            fileRenameManager: FileRenameManager,
            fileSizeManager: FileSizeManager
    ) {
        fileAdapterDelegate.setFileManagers(
                fileCopyCutManager,
                fileDeleteManager,
                fileRenameManager,
                fileSizeManager
        )
    }

    //region File
    private class FileAdapterDelegate(
            private val fileListClickListener: FileListRow.FileClickListener?
    ) : AbsListItemAdapterDelegate<Any, Any, VideoRowHolder>() {

        private val fileListRows = ArrayList<FileListRow>()
        private var selectedPath: String? = null

        private var fileCopyCutManager: FileCopyCutManager? = null
        private var fileDeleteManager: FileDeleteManager? = null
        private var fileRenameManager: FileRenameManager? = null
        private var fileSizeManager: FileSizeManager? = null

        override fun isForViewType(o: Any, list: List<Any>, i: Int): Boolean {
            return o is File
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup): VideoRowHolder {
            val view = FileListRow(viewGroup.context)
            view.layoutParams = RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT)
            view.setFileClickListener(fileListClickListener)
            if (fileDeleteManager != null && fileCopyCutManager != null && fileRenameManager != null) {
                view.setFileManagers(
                        fileCopyCutManager!!,
                        fileDeleteManager!!,
                        fileRenameManager!!,
                        fileSizeManager!!
                )
            }
            fileListRows.add(view)
            return VideoRowHolder(view)
        }

        override fun onBindViewHolder(
                model: Any, playlistViewHolder: VideoRowHolder, list: List<Any>) {
            playlistViewHolder.bind(model as File, selectedPath)
        }

        fun setFileManagers(
                fileCopyCutManager: FileCopyCutManager,
                fileDeleteManager: FileDeleteManager,
                fileRenameManager: FileRenameManager,
                fileSizeManager: FileSizeManager
        ) {
            this.fileCopyCutManager = fileCopyCutManager
            this.fileDeleteManager = fileDeleteManager
            this.fileRenameManager = fileRenameManager
            this.fileSizeManager = fileSizeManager
            for (fileListRow in fileListRows) {
                fileListRow.setFileManagers(
                        fileCopyCutManager,
                        fileDeleteManager,
                        fileRenameManager,
                        fileSizeManager
                )
            }
        }

        fun selectPath(path: String?) {
            selectedPath = path
        }
    }

    private class VideoRowHolder(
            private val view: FileListRow
    ) : RecyclerView.ViewHolder(view) {
        fun bind(file: File, selectedPath: String?) {
            view.setFile(file, selectedPath)
        }
    }
    //endregion File
}