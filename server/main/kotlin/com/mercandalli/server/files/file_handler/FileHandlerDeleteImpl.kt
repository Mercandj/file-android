package com.mercandalli.server.files.file_handler

import com.mercandalli.sdk.files.api.File
import com.mercandalli.server.files.file_repository.FileRepository
import com.mercandalli.server.files.log.LogManager
import com.mercandalli.sdk.files.api.online.response_json.ServerResponse
import com.mercandalli.sdk.files.api.online.response_json.ServerResponseFile
import org.json.JSONObject

class FileHandlerDeleteImpl(
        private val fileRepository: FileRepository,
        private val logManager: LogManager
) : FileHandlerDelete {

    override fun delete(body: String): String {
        logManager.d(TAG, "delete(body: $body)")
        val fileJsonObject = JSONObject(body)
        val path = fileJsonObject.getString(File.JSON_KEY_PATH)
        val deletedFile = fileRepository.delete(path)
        val deleteSucceeded = deletedFile != null
        if (deleteSucceeded) {
            val folderContainerPath = fileRepository.getFolderContainerPath()
            val javaFile = java.io.File(folderContainerPath, deletedFile!!.name)
            javaFile.delete()
        }
        return ServerResponse.create(
                JSONObject(),
                "File deleted in the repository $deleteSucceeded",
                deleteSucceeded
        ).toJsonString()
    }

    companion object {
        private const val TAG = "FileHandlerPost"
    }
}