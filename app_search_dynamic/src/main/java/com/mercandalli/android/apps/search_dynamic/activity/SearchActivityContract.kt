@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.search_dynamic.activity

import com.mercandalli.sdk.files.api.File

interface SearchActivityContract {

    interface UserAction {

        fun onCreate()

        fun onDestroy()

        fun onBackClicked()

        fun onSearchPerformed(search: String)

        fun onInputChanged(inputCharSequence: CharSequence?)

        fun onInputFocusChanged(hasFocus: Boolean)

        fun onSearchIconClicked(search: String)
    }

    interface Screen {

        fun populate(list: List<File>)

        fun showKeyboard()

        fun hideKeyBoard()

        fun quit()
    }
}
