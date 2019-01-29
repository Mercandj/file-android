package com.mercandalli.android.apps.files.main

import com.mercandalli.sdk.files.api.File

/**
 * Movie class represents video entity with title, description, image thumbs and video url.
 */
data class MainFileViewModel(
    val title: String? = null,
    val path: String,
    val directory: Boolean
) {

    companion object {

        fun create(file: File): MainFileViewModel {
            return MainFileViewModel(
                file.name,
                file.id,
                file.directory
            )
        }

        fun create(files: List<File>): List<MainFileViewModel> {
            val result = ArrayList<MainFileViewModel>()
            for (file in files) {
                val mainFileViewModel = create(file)
                result.add(mainFileViewModel)
            }
            return result
        }
    }
}
