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
    private var lastSearchInput: String? = null

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
        fileSearchManager.search(search)
    }

    override fun onInputChanged(inputCharSequence: CharSequence?) {
    }

    override fun onInputFocusChanged(hasFocus: Boolean) {
    }

    private fun syncScreen() {
        val files = ArrayList<File>()
        lastSearchInput?.let {
            val searchResult = fileSearchManager.getSearchResult(it)
            files.addAll(searchResult.files)
        }
        screen.populate(files)
    }

    private fun performSearchInternal(search: String) {
        lastSearchInput = search
    }

    private fun createFileSearchListener() = object : FileSearchManager.FileSearchListener {
        override fun onFileSearchResultChanged(query: String) {
        }
    }
}
