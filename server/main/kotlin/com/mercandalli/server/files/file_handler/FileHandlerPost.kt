package com.mercandalli.server.files.file_handler

import io.ktor.content.MultiPartData
import io.ktor.http.Headers

interface FileHandlerPost {

    fun create(
            headers: Headers,
            body: String
    ): String

    fun rename(
            headers: Headers,
            body: String
    ): String

    fun copy(
            headers: Headers,
            body: String
    ): String

    fun cut(
            headers: Headers,
            body: String
    ): String

    suspend fun upload(
            headers: Headers,
            multipart: MultiPartData
    ): String

    suspend fun download(
            headers: Headers,
            body: String
    ): java.io.File?
}