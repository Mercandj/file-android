package com.mercandalli.android.apps.files.audio

interface AudioQueueManager {

    fun next(path: String): String

    fun previous(path: String): String
}
