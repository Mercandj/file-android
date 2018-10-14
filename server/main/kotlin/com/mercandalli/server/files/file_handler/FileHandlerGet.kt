package com.mercandalli.server.files.file_handler

import io.ktor.http.Headers

interface FileHandlerGet {

    fun get(
            headers: Headers
    ): String

    fun get(
            headers: Headers,
            id: String
    ): String

    fun getFromParent(
            headers: Headers,
            parentPath: String
    ): String

    fun getSize(
            headers: Headers,
            path: String?
    ): String
}