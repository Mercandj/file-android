package com.mercandalli.server.files.file_handler

import com.mercandalli.sdk.files.api.File
import com.mercandalli.sdk.files.api.FileSizeResult
import com.mercandalli.sdk.files.api.FileSizeUtils
import com.mercandalli.sdk.files.api.online.response_json.ServerResponse
import com.mercandalli.server.files.file_repository.FileRepository
import com.mercandalli.server.files.log.LogManager
import com.mercandalli.sdk.files.api.online.response_json.ServerResponseFile
import com.mercandalli.sdk.files.api.online.response_json.ServerResponseFiles
import org.json.JSONObject

class FileHandlerGetImpl(
        private val fileRepository: FileRepository,
        private val logManager: LogManager
) : FileHandlerGet {

    override fun get(): String {
        logManager.d(TAG, "get()")
        val files = fileRepository.get()
        return ServerResponseFiles.create(
                files,
                "Get all the files",
                true
        ).toJsonString()
    }

    override fun get(id: String): String {
        logManager.d(TAG, "get(id: $id)")
        val file = File.createFake(id)
        return ServerResponseFile.create(
                file,
                "Get one file",
                true
        ).toJsonString()
    }

    override fun getFromParent(parentPath: String): String {
        logManager.d(TAG, "getFromParent(id: $parentPath)")
        val files = fileRepository.getFromParent(parentPath)
        return ServerResponseFiles.create(
                files,
                "Get one file",
                true
        ).toJsonString()
    }

    override fun getSize(path: String?): String {
        logManager.d(TAG, "getSize(id: $path)")
        if (path == null) {
            return ServerResponse.create(
                    "Path is null",
                    false
            ).toJsonString()
        }
        val folderContainerPath = fileRepository.getFolderContainerPath()
        val javaFile = java.io.File(folderContainerPath, path)
        val fileSizeResult = FileSizeUtils.computeSizeFromJavaFileSync(javaFile.absolutePath)
        if (fileSizeResult.status != FileSizeResult.Status.LOADED_SUCCEEDED) {
            return ServerResponse.create(
                    "Error load error. ${fileSizeResult.status}",
                    false
            ).toJsonString()
        }
        val jsonObject = JSONObject()
        jsonObject.put("size", fileSizeResult.path)
        return ServerResponse.create(
                jsonObject,
                "Get one file",
                true
        ).toJsonString()
    }

    companion object {
        private const val TAG = "FileHandlerGet"
    }
}