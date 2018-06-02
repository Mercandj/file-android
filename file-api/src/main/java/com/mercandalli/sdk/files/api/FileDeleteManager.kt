package com.mercandalli.sdk.files.api

interface FileDeleteManager {

    fun delete(path: String): Boolean
}