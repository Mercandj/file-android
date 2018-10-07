package com.mercandalli.server.files.file

interface FileGetHandler {

    fun get(): String

    fun post(): String

    fun get(id: String): String
}