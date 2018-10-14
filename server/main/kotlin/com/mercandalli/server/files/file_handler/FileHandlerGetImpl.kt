package com.mercandalli.server.files.file_handler

import com.mercandalli.sdk.files.api.File
import com.mercandalli.sdk.files.api.FileSizeResult
import com.mercandalli.sdk.files.api.FileSizeUtils
import com.mercandalli.sdk.files.api.online.response_json.ServerResponse
import com.mercandalli.server.files.file_repository.FileRepository
import com.mercandalli.server.files.log.LogManager
import com.mercandalli.sdk.files.api.online.response_json.ServerResponseFile
import com.mercandalli.sdk.files.api.online.response_json.ServerResponseFiles
import com.mercandalli.server.files.authorization.AuthorizationManager
import io.ktor.http.Headers
import org.json.JSONObject

class FileHandlerGetImpl(
        private val fileRepository: FileRepository,
        private val logManager: LogManager,
        private val authorizationManager: AuthorizationManager
) : FileHandlerGet {

    override fun get(
            headers: Headers
    ): String {
        logd("get()")
        if (!authorizationManager.isAuthorized(headers)) {
            loge("get: Not logged")
            return ServerResponseFiles.create(
                    ArrayList(),
                    "Oops, not logged",
                    false
            ).toJsonString()
        }
        val files = fileRepository.get()
        return ServerResponseFiles.create(
                files,
                "Get all the files",
                true
        ).toJsonString()
    }

    override fun get(
            headers: Headers,
            id: String
    ): String {
        logd("get(id: $id)")
        if (!authorizationManager.isAuthorized(headers)) {
            loge("get: Not logged")
            return ServerResponse.create(
                    "Oops, not logged",
                    false
            ).toJsonString()
        }
        val file = File.createFake(id)
        return ServerResponseFile.create(
                file,
                "Get one file",
                true
        ).toJsonString()
    }

    override fun getFromParent(
            headers: Headers,
            parentPath: String
    ): String {
        logd("getFromParent(parentPath: $parentPath)")
        if (!authorizationManager.isAuthorized(headers)) {
            loge("getFromParent: Not logged")
            return ServerResponse.create(
                    "Oops, not logged",
                    false
            ).toJsonString()
        }
        val files = fileRepository.getFromParent(parentPath)
        return ServerResponseFiles.create(
                files,
                "Get one file",
                true
        ).toJsonString()
    }

    override fun getSize(
            headers: Headers,
            path: String?
    ): String {
        logd("getSize(path: $path)")
        if (!authorizationManager.isAuthorized(headers)) {
            loge("getSize: Not logged")
            return ServerResponse.create(
                    "Oops, not logged",
                    false
            ).toJsonString()
        }
        if (path == null) {
            loge("getSize: path is null")
            return ServerResponse.create(
                    "Path is null",
                    false
            ).toJsonString()
        }
        val folderContainerPath = fileRepository.getFolderContainerPath()
        val javaFile = java.io.File(folderContainerPath, path)
        val fileSizeResult = FileSizeUtils.computeSizeFromJavaFileSync(javaFile.absolutePath)
        if (fileSizeResult.status != FileSizeResult.Status.LOADED_SUCCEEDED) {
            loge("getSize: Error load error. path == $path. error == ${fileSizeResult.status}")
            return ServerResponse.create(
                    "Error load error. ${fileSizeResult.status}",
                    false
            ).toJsonString()
        }
        val jsonObject = JSONObject()
        jsonObject.put("path", fileSizeResult.path)
        jsonObject.put("size", fileSizeResult.size)
        return ServerResponse.create(
                jsonObject,
                "Get size file",
                true
        ).toJsonString()
    }

    private fun logd(message: String) {
        logManager.d(TAG, message)
    }

    private fun loge(message: String) {
        logManager.e(TAG, message)
    }

    companion object {

        private const val TAG = "FileHandlerGet"
    }
}