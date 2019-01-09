package com.mercandalli.android.apps.files.search

import android.os.Bundle
import androidx.leanback.widget.ObjectAdapter
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.ListRowPresenter
import com.mercandalli.android.apps.files.main.ApplicationGraph

class SearchFragment :
        androidx.leanback.app.SearchFragment(),
        SearchFragmentContract.Screen {

    private var mRowsAdapter: ArrayObjectAdapter? = null
    private val userAction by lazy { createUserAction() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mRowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        userAction.onCreate()

        setSearchResultProvider(object : SearchResultProvider {
            override fun onQueryTextSubmit(query: String?): Boolean {
                userAction.onQueryTextSubmit(query!!)
                return true
            }

            override fun getResultsAdapter(): ObjectAdapter {
                return mRowsAdapter!!
            }

            override fun onQueryTextChange(newQuery: String?): Boolean {
                return true
            }
        })
    }

    private fun createUserAction(): SearchFragmentContract.UserAction {
        val fileSearchManager = ApplicationGraph.getFileSearchManager()
        return SearchFragmentPresenter(
                this,
                fileSearchManager
        )
    }
}
