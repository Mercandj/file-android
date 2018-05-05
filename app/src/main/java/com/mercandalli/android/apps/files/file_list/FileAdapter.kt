package com.mercandalli.android.apps.files.file_list

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import com.mercandalli.android.apps.files.file_row.FileRow
import com.mercandalli.sdk.files.api.File

class FileAdapter(
        fileClickListener: FileRow.FileClickListener?
) : ListDelegationAdapter<List<Any>>() {

    private val fileAdapterDelegate = FileAdapterDelegate(fileClickListener)

    init {
        delegatesManager.addDelegate(fileAdapterDelegate as AdapterDelegate<List<Any>>)
    }

    fun populate(list: List<Any>) {
        setItems(list)
        notifyDataSetChanged()
    }

    fun selectPath(path: String) {
        fileAdapterDelegate.selectPath(path)
        notifyDataSetChanged()
    }

    //region File
    private class FileAdapterDelegate(
            private val fileClickListener: FileRow.FileClickListener?
    ) : AbsListItemAdapterDelegate<Any, Any, VideoRowHolder>() {

        private var selectedPath: String? = null

        override fun isForViewType(o: Any, list: List<Any>, i: Int): Boolean {
            return o is File
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup): VideoRowHolder {
            val videoRow = FileRow(viewGroup.context)
            videoRow.layoutParams = RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT)
            videoRow.setFileClickListener(fileClickListener)
            return VideoRowHolder(videoRow)
        }

        override fun onBindViewHolder(
                model: Any, playlistViewHolder: VideoRowHolder, list: List<Any>) {
            playlistViewHolder.bind(model as File, selectedPath)
        }

        fun selectPath(path: String) {
            selectedPath = path
        }
    }

    private class VideoRowHolder(
            private val view: FileRow) :
            RecyclerView.ViewHolder(view) {
        fun bind(file: File, selectedPath: String?) {
            val selected = selectedPath?.startsWith(file.path) ?: false
            view.setFile(file, selected)
        }
    }
    //endregion File
}