@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.search_dynamic.activity

import com.mercandalli.sdk.files.api.File
import com.mercandalli.sdk.files.api.FileSearchManager

class SearchActivityPresenter(
    private val screen: SearchActivityContract.Screen,
    private val fileSearchManager: FileSearchManager
) : SearchActivityContract.UserAction {

    private val fileSearchListener = createFileSearchListener()
    private var search: String? = null

    override fun onCreate() {
        screen.showKeyboard()
        fileSearchManager.registerFileSearchListener(fileSearchListener)
        syncScreen()
    }

    override fun onDestroy() {
        fileSearchManager.unregisterFileSearchListener(fileSearchListener)
    }

    override fun onBackClicked() {
        screen.quit()
    }

    override fun onSearchPerformed(search: String) {
        performSearchInternal(search)
    }

    override fun onInputChanged(inputCharSequence: CharSequence?) {
    }

    override fun onInputFocusChanged(hasFocus: Boolean) {
    }

    override fun onSearchIconClicked(search: String) {
        performSearchInternal(search)
    }

    private fun syncScreen() {
        val files = ArrayList<File>()
        search?.let {
            val searchResult = fileSearchManager.getSearchResult(it)
            files.addAll(searchResult.files)
        }
        screen.populate(files)
    }

    private fun performSearchInternal(search: String) {
        this.search = search
        fileSearchManager.search(search)
        screen.hideKeyBoard()
        syncScreen()
    }

    private fun createFileSearchListener() = object : FileSearchManager.FileSearchListener {
        override fun onFileSearchResultChanged(query: String) {
            if (search != query) {
                return
            }
            syncScreen()
        }
    }
}
