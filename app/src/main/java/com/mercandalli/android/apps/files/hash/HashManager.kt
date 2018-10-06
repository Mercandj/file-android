package com.mercandalli.android.apps.files.hash

interface HashManager {

    fun sha256(text: String, time: Int): String?
}