package com.mercandalli.sdk.files.api

enum class FileExtension(
        private val extension: String
) {

    MP3("mp3"),
    OGG("ogg"),
    WAV("wav"),
    WMA("wma"),
    ZIP("zip");

    fun isCompliant(path: String): Boolean {
        return path.toLowerCase().endsWith(".$extension")
    }
}