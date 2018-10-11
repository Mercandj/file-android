package com.mercandalli.server.files.file_handler

import io.ktor.content.MultiPartData
import io.ktor.http.Headers

interface FileHandlerPost {

    fun createPost(body: String): String

    fun renamePost(body: String): String

    suspend fun uploadPost(
            headers: Headers,
            multipart: MultiPartData
    ): String
}