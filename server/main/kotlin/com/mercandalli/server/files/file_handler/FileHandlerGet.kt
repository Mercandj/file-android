package com.mercandalli.server.files.file_handler

interface FileHandlerGet {

    fun get(): String

    fun get(id: String): String

    fun getFromParent(parentPath: String): String
}