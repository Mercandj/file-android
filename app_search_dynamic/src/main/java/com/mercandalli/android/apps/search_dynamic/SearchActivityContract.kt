package com.mercandalli.android.apps.search_dynamic

interface SearchActivityContract {

    interface UserAction {

        fun onCreate()

        fun onDestroy()

        fun onBackClicked()
    }

    interface Screen {

        fun showKeyboard()

        fun quit()
    }
}
