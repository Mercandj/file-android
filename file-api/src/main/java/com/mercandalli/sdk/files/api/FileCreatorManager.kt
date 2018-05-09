package com.mercandalli.sdk.files.api

interface FileCreatorManager {

    fun create(path: String, name: String): Boolean
}