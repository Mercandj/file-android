package com.mercandalli.android.apps.files.search

import com.mercandalli.android.apps.files.main.MainFileRowViewModel
import com.mercandalli.android.apps.files.main.MainFileViewModel
import com.mercandalli.sdk.files.api.FileSearchManager
import java.io.File

class SearchFragmentPresenter(
    private val screen: SearchFragmentContract.Screen,
    private val fileSearchManager: FileSearchManager
) : SearchFragmentContract.UserAction {

    override fun onCreate() {
    }

    override fun onQueryTextSubmit(query: String) {
        fileSearchManager.search(query)
        screen.show(
            listOf(
                MainFileRowViewModel(
                    "Local Files",
                    listOf(
                        MainFileViewModel.create(
                            com.mercandalli.sdk.files.api.File.create(File("/FakeResult"))
                        )
                    )
                )
            )
        )
    }
}
