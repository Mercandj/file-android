package com.mercandalli.server.files.file

interface FilePostHandler {

    fun post(content: String): String
}