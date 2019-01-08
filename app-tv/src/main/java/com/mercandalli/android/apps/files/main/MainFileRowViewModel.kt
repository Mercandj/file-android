package com.mercandalli.android.apps.files.main

import com.mercandalli.sdk.files.api.File

data class MainFileRowViewModel(
    val title: String,
    val files: List<MainFileViewModel>
) {

    companion object {

        fun create(
            title: String,
            files: List<File>
        ): MainFileRowViewModel {
            val mainFileViewModels = MainFileViewModel.create(files)
            return MainFileRowViewModel(
                title,
                mainFileViewModels
            )
        }
    }
}
