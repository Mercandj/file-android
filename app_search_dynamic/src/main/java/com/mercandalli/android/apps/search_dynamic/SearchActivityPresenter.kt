package com.mercandalli.android.apps.search_dynamic

import com.mercandalli.sdk.files.api.FileSearchManager

class SearchActivityPresenter(
    private val screen: SearchActivityContract.Screen,
    private val fileSearchManager: FileSearchManager
) : SearchActivityContract.UserAction {

    override fun onCreate() {
        screen.showKeyboard()
    }

    override fun onDestroy() {
    }

    override fun onBackClicked() {
        screen.quit()
    }
}
