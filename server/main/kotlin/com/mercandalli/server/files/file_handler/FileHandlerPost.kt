@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.server.files.file_handler

import io.ktor.http.Headers
import io.ktor.http.content.MultiPartData

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
