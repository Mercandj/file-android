package com.mercandalli.android.apps.files.main

interface MainFragmentContract {

    interface UserAction {

        fun onCreate()

        fun onDestroy()

        fun onFileClicked(mainFileViewModel: MainFileViewModel)

        fun onSettingsClicked()
    }

    interface Screen {

        fun showFiles(mainFileRowViewModels: List<MainFileRowViewModel>)
    }
}
