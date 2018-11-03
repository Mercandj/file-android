package com.mercandalli.server.files.window

interface MainContract {

    interface UserAction {

        fun onStartClicked()

        fun onStopClicked()

        fun onPullSubFoldersClicked()
    }

    interface Screen
}
