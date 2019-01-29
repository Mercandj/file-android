package com.mercandalli.android.apps.files.search

import android.os.Bundle
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.ObjectAdapter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import com.mercandalli.android.apps.files.main.ApplicationGraph
import com.mercandalli.android.apps.files.main.MainCardPresenter
import com.mercandalli.android.apps.files.main.MainFileRowViewModel

class SearchFragment :
    androidx.leanback.app.SearchFragment(),
    SearchFragmentContract.Screen {

    private var adapter: ArrayObjectAdapter = ArrayObjectAdapter(ListRowPresenter())
    private val userAction by lazy { createUserAction() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userAction.onCreate()
        setSearchResultProvider(object : SearchResultProvider {
            override fun onQueryTextSubmit(query: String?): Boolean {
                userAction.onQueryTextSubmit(query!!)
                return true
            }

            override fun getResultsAdapter(): ObjectAdapter {
                return adapter
            }

            override fun onQueryTextChange(newQuery: String?): Boolean {
                return true
            }
        })
    }

    override fun onDestroy() {
        userAction.onDestroy()
        super.onDestroy()
    }

    override fun show(mainFileRowViewModels: List<MainFileRowViewModel>) {
        adapter.clear()
        val cardPresenter = MainCardPresenter()
        for ((i, mainFileRow) in mainFileRowViewModels.withIndex()) {
            val listRowAdapter = ArrayObjectAdapter(cardPresenter)
            for (file in mainFileRow.files) {
                listRowAdapter.add(file)
            }
            val header = HeaderItem(i.toLong(), mainFileRow.title)
            val listRow = ListRow(header, listRowAdapter)
            adapter.add(listRow)
        }
    }

    private fun createUserAction(): SearchFragmentContract.UserAction {
        val fileSearchManager = ApplicationGraph.getFileSearchManager()
        return SearchFragmentPresenter(
            this,
            fileSearchManager
        )
    }
}
