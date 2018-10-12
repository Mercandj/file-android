package com.mercandalli.server.files.file_repository

import com.mercandalli.sdk.files.api.File

interface FileRepository {

    fun getFolderContainerPath(): String

    fun has(path: String): Boolean

    fun get(): List<File>

    fun get(path: String): File

    fun getFromParent(parentPath: String): List<File>

    fun put(file: File)

    fun delete(path: String): File?

    fun rename(path: String, name: String): File?
}