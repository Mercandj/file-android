package com.mercandalli.android.apps.files

/**
 * Movie class represents video entity with title, description, image thumbs and video url.
 */
data class FileViewModel(
    var id: Long = 0,
    var title: String? = null,
    var backgroundImageUrl: String? = null,
    var cardImageUrl: String? = null,
    var videoUrl: String? = null,
    var studio: String? = null
)
