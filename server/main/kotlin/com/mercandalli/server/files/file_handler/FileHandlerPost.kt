package com.mercandalli.server.files.file_handler

interface FileHandlerPost {

    fun post(body: String): String

    fun renamePost(body: String): String
}