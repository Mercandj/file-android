package com.mercandalli.android.apps.files.main

import com.mercandalli.sdk.files.api.File

/**
 * Movie class represents video entity with title, description, image thumbs and video url.
 */
data class MainFileViewModel(
    val title: String? = null,
    val backgroundImageUrl: String? = null,
    val cardImageUrl: String? = null,
    val videoUrl: String? = null,
    val path: String,
    val directory: Boolean
) {

    companion object {

        fun create(file: File): MainFileViewModel {
            return MainFileViewModel(
                file.name,
                "http://commondatastorage.googleapis.com/android-tv/Sample%20videos/Zeitgeist/Zeitgeist%202010_%20Year%20in%20Review/bg.jpg",
                "http://commondatastorage.googleapis.com/android-tv/Sample%20videos/Zeitgeist/Zeitgeist%202010_%20Year%20in%20Review/card.jpg",
                "http://commondatastorage.googleapis.com/android-tv/Sample%20videos/Zeitgeist/Zeitgeist%202010_%20Year%20in%20Review.mp4",
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
