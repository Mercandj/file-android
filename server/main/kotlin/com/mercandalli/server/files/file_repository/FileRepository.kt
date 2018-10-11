package com.mercandalli.server.files.file_repository

import com.mercandalli.sdk.files.api.File

interface FileRepository {

    fun getFolderContainerPath(): String

    fun put(file: File)

    fun get(path: String): File

    fun has(path: String): Boolean

    fun get(): List<File>

    fun delete(path: String): Boolean

    fun rename(path: String, name: String): File?
}