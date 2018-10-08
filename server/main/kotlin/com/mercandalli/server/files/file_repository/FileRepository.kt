package com.mercandalli.server.files.file_repository

import com.mercandalli.sdk.files.api.File

interface FileRepository {

    fun put(file: File)

    fun get(id: String): File

    fun has(id: String): Boolean

    fun get(): List<File>
}