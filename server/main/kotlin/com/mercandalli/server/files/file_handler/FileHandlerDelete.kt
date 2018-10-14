package com.mercandalli.server.files.file_handler

import io.ktor.http.Headers

interface FileHandlerDelete {

    fun deleteFile(
            headers: Headers,
            body: String
    ): String
}