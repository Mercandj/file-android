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

    //region File
    private class FileAdapterDelegate(
            private val fileClickListener: FileRow.FileClickListener?
    ) : AbsListItemAdapterDelegate<Any, Any, VideoRowHolder>() {

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
            playlistViewHolder.bind(model as File)
        }
    }

    private class VideoRowHolder(
            private val view: FileRow) :
            RecyclerView.ViewHolder(view) {
        fun bind(file: File) {
            view.setFile(file)
        }
    }
    //endregion File
}