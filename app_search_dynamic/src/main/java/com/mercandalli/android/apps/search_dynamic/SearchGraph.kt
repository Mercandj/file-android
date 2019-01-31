package com.mercandalli.android.apps.search_dynamic

import android.annotation.SuppressLint
import com.mercandalli.android.apps.files.main.ApplicationGraph

class SearchGraph {

    private val fileSearchManagerInternal by lazy {
        ApplicationGraph.getFileModule().createFileSearchManager()
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var graph: SearchGraph? = null

        fun getFileSearchManager() = getGraph().fileSearchManagerInternal

        private fun getGraph(): SearchGraph {
            if (graph == null) {
                graph = SearchGraph()
            }
            return graph!!
        }
    }
}
