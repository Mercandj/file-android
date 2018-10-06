package com.mercandalli.android.apps.files.file_list

import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.mercandalli.android.apps.files.file_list_row.FileListRow
import com.mercandalli.sdk.files.api.File

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

    //region File
    private class FileAdapterDelegate(
            private val fileListClickListener: FileListRow.FileClickListener?
    ) : AbsListItemAdapterDelegate<Any, Any, VideoRowHolder>() {

        private var selectedPath: String? = null

        override fun isForViewType(o: Any, list: List<Any>, i: Int): Boolean {
            return o is File
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup): VideoRowHolder {
            val videoRow = FileListRow(viewGroup.context)
            videoRow.layoutParams = androidx.recyclerview.widget.RecyclerView.LayoutParams(
                    androidx.recyclerview.widget.RecyclerView.LayoutParams.MATCH_PARENT,
                    androidx.recyclerview.widget.RecyclerView.LayoutParams.WRAP_CONTENT)
            videoRow.setFileClickListener(fileListClickListener)
            return VideoRowHolder(videoRow)
        }

        override fun onBindViewHolder(
                model: Any, playlistViewHolder: VideoRowHolder, list: List<Any>) {
            playlistViewHolder.bind(model as File, selectedPath)
        }

        fun selectPath(path: String?) {
            selectedPath = path
        }
    }

    private class VideoRowHolder(
            private val view: FileListRow) :
            androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        fun bind(file: File, selectedPath: String?) {
            view.setFile(file, selectedPath)
        }
    }
    //endregion File
}