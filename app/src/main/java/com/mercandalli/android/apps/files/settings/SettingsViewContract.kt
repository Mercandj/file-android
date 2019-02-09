package com.mercandalli.android.apps.files.settings

interface SettingsViewContract {

    interface UserAction {

        fun onAttached()

        fun onDetached()
    }

    interface Screen {

        fun populate(list: List<Any>)
    }
}
