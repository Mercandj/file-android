package com.mercandalli.server.files.file

interface FileGetHandler {

    fun get(id: String): String
}