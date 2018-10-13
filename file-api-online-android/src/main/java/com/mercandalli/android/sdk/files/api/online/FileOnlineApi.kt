package com.mercandalli.android.sdk.files.api.online

import com.mercandalli.sdk.files.api.File
import com.mercandalli.sdk.files.api.online.response_json.ServerResponse
import com.mercandalli.sdk.files.api.online.response_json.ServerResponseFiles

internal interface FileOnlineApi {

    fun get(): ServerResponseFiles?

    fun getFromParent(parentPath: String): ServerResponseFiles?

    fun getSize(path: String): ServerResponse?

    fun post(file: File)

    fun post(file: File, javaFile: java.io.File)

    fun delete(path: String): Boolean

    fun rename(path: String, name: String): Boolean

    fun copy(pathInput: String, pathDirectoryOutput: String): Boolean

    fun cut(pathInput: String, pathDirectoryOutput: String): Boolean
}