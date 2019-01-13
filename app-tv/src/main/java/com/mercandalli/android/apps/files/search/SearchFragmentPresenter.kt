package com.mercandalli.android.apps.files.search

import com.mercandalli.android.apps.files.main.MainFileRowViewModel
import com.mercandalli.android.apps.files.main.MainFileViewModel
import com.mercandalli.sdk.files.api.FileSearchManager
import com.mercandalli.sdk.files.api.FileSearchResult

class SearchFragmentPresenter(
    private val screen: SearchFragmentContract.Screen,
    private val fileSearchManager: FileSearchManager
) : SearchFragmentContract.UserAction {

    private var query = ""

    private val fileSearchListener = createFileSearchListener()

    override fun onCreate() {
        fileSearchManager.registerFileSearchListener(fileSearchListener)
    }

    override fun onDestroy() {
        fileSearchManager.unregisterFileSearchListener(fileSearchListener)
    }

    override fun onQueryTextSubmit(query: String) {
        this.query = query
        val fileSearchResult = fileSearchManager.search(query)
        syncResult(fileSearchResult)
    }

    private fun syncResult(
        fileSearchResult: FileSearchResult = fileSearchManager.getSearchResult(query)
    ) {
        val files = fileSearchResult.files
        val mainFileViewModels = MainFileViewModel.create(files)
        val mainFileRowViewModels = listOf(
            MainFileRowViewModel(
                "Local Files",
                mainFileViewModels
            )
        )
        screen.show(mainFileRowViewModels)
    }

    private fun createFileSearchListener() = object : FileSearchManager.FileSearchListener {
        override fun onFileSearchResultChanged(query: String) {
            if (this@SearchFragmentPresenter.query != query) {
                return
            }
            syncResult()
        }
    }
}
