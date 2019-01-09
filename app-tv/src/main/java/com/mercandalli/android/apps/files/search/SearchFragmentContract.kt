package com.mercandalli.android.apps.files.search

interface SearchFragmentContract {

    interface UserAction {

        fun onCreate()

        fun onQueryTextSubmit(query: String)
    }

    interface Screen
}
