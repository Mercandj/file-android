@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.search_dynamic.list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.mercandalli.android.apps.files.file_list_row.FileListRow
import com.mercandalli.sdk.files.api.File

class SearchListAdapter(
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

    //region File
    private class FileAdapterDelegate(
        private val fileListClickListener: FileListRow.FileClickListener?
    ) : AbsListItemAdapterDelegate<Any, Any, FileRowHolder>() {

        private val fileListRows = ArrayList<FileListRow>()

        override fun isForViewType(o: Any, list: List<Any>, i: Int): Boolean {
            return o is File
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup): FileRowHolder {
            val view = FileListRow(viewGroup.context)
            view.layoutParams = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT)
            view.setFileClickListener(fileListClickListener)
            fileListRows.add(view)
            return FileRowHolder(view)
        }

        override fun onBindViewHolder(
            model: Any,
            playlistViewHolder: FileRowHolder,
            list: List<Any>
        ) {
            playlistViewHolder.bind(model as File)
        }
    }

    private class FileRowHolder(
        private val view: FileListRow
    ) : RecyclerView.ViewHolder(view) {
        fun bind(file: File) {
            view.setFile(file, null)
        }
    }
    //endregion File
}
