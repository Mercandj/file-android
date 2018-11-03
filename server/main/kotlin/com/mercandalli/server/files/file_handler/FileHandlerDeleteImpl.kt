@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.server.files.file_handler

import com.mercandalli.sdk.files.api.File
import com.mercandalli.server.files.file_repository.FileRepository
import com.mercandalli.server.files.log.LogManager
import com.mercandalli.sdk.files.api.online.response_json.ServerResponse
import com.mercandalli.sdk.files.api.online.response_json.ServerResponseFile
import com.mercandalli.server.files.authorization.AuthorizationManager
import io.ktor.http.Headers
import org.json.JSONObject

class FileHandlerDeleteImpl(
    private val fileRepository: FileRepository,
    private val logManager: LogManager,
    private val authorizationManager: AuthorizationManager
) : FileHandlerDelete {

    override fun deleteFile(
        headers: Headers,
        body: String
    ): String {
        logd("delete(body: $body)")
        if (!authorizationManager.isAuthorized(headers)) {
            loge("delete: Not logged")
            return ServerResponse.create(
                "Oops, not logged",
                false
            ).toJsonString()
        }
        val fileJsonObject = JSONObject(body)
        val path = fileJsonObject.getString(File.JSON_KEY_PATH)
        val deletedFile = fileRepository.delete(path)
        val deleteSucceeded = deletedFile != null
        if (deleteSucceeded) {
            logd("delete: succeeded")
            val folderContainerPath = fileRepository.getFolderContainerPath()
            val javaFile = java.io.File(folderContainerPath, deletedFile!!.name)
            javaFile.delete()
        } else {
            loge("delete: Failed to delete: path == $path")
        }
        return ServerResponse.create(
            JSONObject(),
            "File deleted in the repository $deleteSucceeded",
            deleteSucceeded
        ).toJsonString()
    }

    private fun logd(message: String) {
        logManager.d(TAG, message)
    }

    private fun loge(message: String) {
        logManager.e(TAG, message)
    }

    companion object {

        private const val TAG = "FileHandlerPost"
    }
}
