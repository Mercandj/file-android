package com.mercandalli.server.files.file_handler

import io.ktor.content.MultiPartData

interface FileHandlerPost {

    fun createPost(body: String): String

    fun renamePost(body: String): String

    suspend fun uploadPost(multipart: MultiPartData): String
}