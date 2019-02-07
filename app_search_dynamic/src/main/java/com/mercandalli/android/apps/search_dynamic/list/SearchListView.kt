@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.search_dynamic.list

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.mercandalli.android.apps.search_dynamic.R
import com.mercandalli.sdk.files.api.File

class SearchListView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val view = View.inflate(context, R.layout.view_search_list, this)
    private val recyclerView: RecyclerView = view.findViewById(R.id.view_search_list_recycler_view)

    fun populate(list: List<File>) {
    }
}
