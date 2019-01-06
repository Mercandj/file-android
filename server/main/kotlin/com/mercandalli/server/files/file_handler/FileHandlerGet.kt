@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.server.files.file_handler

import io.ktor.http.Headers

interface FileHandlerGet {

    fun get(
        headers: Headers
    ): String

    fun getFromId(
        headers: Headers,
        id: String
    ): String

    fun getFromPath(
        headers: Headers,
        path: String
    ): String

    fun getChildren(
        headers: Headers,
        parentPath: String
    ): String

    fun getSize(
        headers: Headers,
        path: String?
    ): String
}
