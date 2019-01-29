package com.mercandalli.android.apps.files.search

import com.mercandalli.android.apps.files.main.MainFileRowViewModel

interface SearchFragmentContract {

    interface UserAction {

        fun onCreate()

        fun onDestroy()

        fun onQueryTextSubmit(query: String)
    }

    interface Screen {

        fun show(mainFileRowViewModels: List<MainFileRowViewModel>)
    }
}
