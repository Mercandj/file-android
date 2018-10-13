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
        logd("get()")
        val files = fileRepository.get()
        return ServerResponseFiles.create(
                files,
                "Get all the files",
                true
        ).toJsonString()
    }

    override fun get(id: String): String {
        logd("get(id: $id)")
        val file = File.createFake(id)
        return ServerResponseFile.create(
                file,
                "Get one file",
                true
        ).toJsonString()
    }

    override fun getFromParent(parentPath: String): String {
        logd("getFromParent(parentPath: $parentPath)")
        val files = fileRepository.getFromParent(parentPath)
        return ServerResponseFiles.create(
                files,
                "Get one file",
                true
        ).toJsonString()
    }

    override fun getSize(path: String?): String {
        logd("getSize(path: $path)")
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