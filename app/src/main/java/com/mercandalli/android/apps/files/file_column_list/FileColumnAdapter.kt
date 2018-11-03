@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.file_column_list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.mercandalli.android.apps.files.file_column_row.FileColumnRow
import com.mercandalli.sdk.files.api.File

class FileColumnAdapter(
    fileColumnClickListener: FileColumnRow.FileClickListener?
) : ListDelegationAdapter<List<Any>>() {

    private val fileAdapterDelegate = FileAdapterDelegate(fileColumnClickListener)

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
        private val fileColumnClickListener: FileColumnRow.FileClickListener?
    ) : AbsListItemAdapterDelegate<Any, Any, VideoRowHolder>() {

        private var selectedPath: String? = null

        override fun isForViewType(o: Any, list: List<Any>, i: Int) = o is File

        override fun onCreateViewHolder(viewGroup: ViewGroup): VideoRowHolder {
            val videoRow = FileColumnRow(viewGroup.context)
            videoRow.layoutParams = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT)
            videoRow.setFileClickListener(fileColumnClickListener)
            return VideoRowHolder(videoRow)
        }

        override fun onBindViewHolder(model: Any, playlistViewHolder: VideoRowHolder, list: List<Any>) {
            playlistViewHolder.bind(model as File, selectedPath)
        }

        fun selectPath(path: String?) {
            selectedPath = path
        }
    }

    private class VideoRowHolder(
        private val view: FileColumnRow
    ) : RecyclerView.ViewHolder(view) {
        fun bind(file: File, selectedPath: String?) {
            view.setFile(file, selectedPath)
        }
    }
    //endregion File
}
