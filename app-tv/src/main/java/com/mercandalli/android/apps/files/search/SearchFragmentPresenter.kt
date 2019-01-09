package com.mercandalli.android.apps.files.search

import com.mercandalli.sdk.files.api.FileSearchManager

class SearchFragmentPresenter(
    private val screen: SearchFragmentContract.Screen,
    private val fileSearchManager: FileSearchManager
) : SearchFragmentContract.UserAction {

    override fun onCreate() {
    }

    override fun onQueryTextSubmit(query: String) {
        fileSearchManager.search(query)
    }
}
