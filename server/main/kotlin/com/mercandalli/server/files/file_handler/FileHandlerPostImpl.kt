@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.server.files.file_handler

import com.mercandalli.sdk.files.api.File
import com.mercandalli.sdk.files.api.FileCopyCutUtils
import com.mercandalli.sdk.files.api.FileRenameUtils
import com.mercandalli.server.files.file_repository.FileRepository
import com.mercandalli.server.files.log.LogManager
import com.mercandalli.sdk.files.api.online.response_json.ServerResponse
import com.mercandalli.sdk.files.api.online.response_json.ServerResponseFile
import com.mercandalli.server.files.authorization.AuthorizationManager
import io.ktor.http.Headers
import io.ktor.http.content.MultiPartData
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.network.util.ioCoroutineDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import org.json.JSONObject
import java.io.InputStream
import java.io.OutputStream
import java.lang.StringBuilder

class FileHandlerPostImpl(
    private val fileRepository: FileRepository,
    private val logManager: LogManager,
    private val authorizationManager: AuthorizationManager
) : FileHandlerPost {

    override fun create(
        headers: Headers,
        body: String
    ): String {
        logd("create(body: $body)")
        val debugMessage = StringBuilder()
        val fileJsonObject = JSONObject(body)
        val file = File.fromJson(fileJsonObject)
        fileRepository.put(file)
        debugMessage.append("File inserted into the repository\n")
        val folderContainerPath = fileRepository.getFolderContainerPath()
        val javaFile = java.io.File(folderContainerPath, file.path)
        if (javaFile.exists()) {
            debugMessage.append("Java file replaced\n")
            javaFile.delete()
        }
        if (file.directory) {
            javaFile.mkdirs()
        } else {
            javaFile.createNewFile()
        }
        return ServerResponseFile.create(
            file,
            debugMessage.toString(),
            true
        ).toJsonString()
    }

    override fun rename(
        headers: Headers,
        body: String
    ): String {
        logd("rename(body: $body)")
        val fileJsonObject = JSONObject(body)
        val path = fileJsonObject.getString(File.JSON_KEY_PATH)
        val name = fileJsonObject.getString(File.JSON_KEY_NAME)
        val renamedFile = fileRepository.rename(path, name)
        if (renamedFile == null) {
            loge("rename: Repository rename failed")
            return ServerResponse.create(
                "File not renamed. Maybe not found in the server",
                false
            ).toJsonString()
        }
        if (name.contains("/")) {
            loge("rename: Name should not contains /. name == $name")
            return ServerResponse.create(
                "Name should not contains /. name == $name",
                false
            ).toJsonString()
        }
        logd("rename: repository rename succeeded")
        val folderContainerPath = fileRepository.getFolderContainerPath()
        val javaFile = java.io.File(folderContainerPath, path)
        val renameSucceeded = FileRenameUtils.renameSync(javaFile, name)
        if (!renameSucceeded) {
            loge("rename: File not renamed")
            // Revert rename in the repo
            fileRepository.rename(renamedFile.path, java.io.File(path).name)
            return ServerResponse.create(
                "File not renamed. Java file rename failed",
                false
            ).toJsonString()
        }
        logd("rename: File renamed")
        return ServerResponseFile.create(
            renamedFile,
            "File renamed",
            true
        ).toJsonString()
    }

    override fun copy(
        headers: Headers,
        body: String
    ): String {
        logd("copy(body: $body)")
        val fileJsonObject = JSONObject(body)
        val path = fileJsonObject.getString(File.JSON_KEY_PATH)
        val pathDirectoryOutput = fileJsonObject.getString("path_directory_output")
        val copiedFile = fileRepository.copy(path, pathDirectoryOutput)
        if (copiedFile == null) {
            loge("copy: Repository copy failed")
            return ServerResponse.create(
                "File not copy. Maybe not found in the server",
                false
            ).toJsonString()
        }
        val folderContainerPath = fileRepository.getFolderContainerPath()
        val javaFileInput = java.io.File(folderContainerPath, path)
        val javaFileDirectoryOutput = java.io.File(folderContainerPath, pathDirectoryOutput)
        logd("copy: javaFileInput.absolutePath: ${javaFileInput.absolutePath}")
        logd("copy: javaFileDirectoryOutput.absolutePath: ${javaFileDirectoryOutput.absolutePath}")
        val succeeded = FileCopyCutUtils.copyJavaFileSync(
            javaFileInput.absolutePath,
            javaFileDirectoryOutput.absolutePath
        )
        if (!succeeded) {
            loge("copy: File not copy")
            return ServerResponse.create(
                "File not copied",
                false
            ).toJsonString()
        }
        logd("copy: File copied")
        return ServerResponseFile.create(
            copiedFile,
            "File copied",
            true
        ).toJsonString()
    }

    override fun cut(
        headers: Headers,
        body: String
    ): String {
        logd("cut(body: $body)")
        val fileJsonObject = JSONObject(body)
        val path = fileJsonObject.getString(File.JSON_KEY_PATH)
        val pathDirectoryOutput = fileJsonObject.getString("path_directory_output")
        val cutFile = fileRepository.cut(path, pathDirectoryOutput)
        if (cutFile == null) {
            loge("cut: Repository cut failed")
            return ServerResponse.create(
                "File not cut. Maybe not found in the server",
                false
            ).toJsonString()
        }
        val folderContainerPath = fileRepository.getFolderContainerPath()
        val javaFileInput = java.io.File(folderContainerPath, path)
        val javaFileDirectoryOutput = java.io.File(folderContainerPath, pathDirectoryOutput)
        logd("cut: javaFileInput.absolutePath: ${javaFileInput.absolutePath}")
        logd("cut: javaFileDirectoryOutput.absolutePath: ${javaFileDirectoryOutput.absolutePath}")
        val succeeded = FileCopyCutUtils.cutJavaFileSync(
            javaFileInput.absolutePath,
            javaFileDirectoryOutput.absolutePath
        )
        if (!succeeded) {
            loge("cut: File not cut")
            return ServerResponse.create(
                "File not cut",
                false
            ).toJsonString()
        }
        logd("cut: File cut")
        return ServerResponseFile.create(
            cutFile,
            "File cut",
            true
        ).toJsonString()
    }

    override suspend fun upload(
        headers: Headers,
        multipart: MultiPartData
    ): String {
        logd("upload()")
        if (!authorizationManager.isAuthorized(headers)) {
            loge("upload: Not logged")
            return ServerResponse.create(
                "Oops, not logged",
                false
            ).toJsonString()
        }
        logd("upload: Logged")

        // Processes each part of the multipart input content of the user
        var name: String? = null
        var file: File? = null
        multipart.forEachPart { part ->
            if (part is PartData.FormItem) {
                if (part.name == "json") {
                    val json = part.value
                    logd("upload(json: $json)")
                    val fileJsonObject = JSONObject(json)
                    file = File.fromJson(fileJsonObject)
                    name = file!!.name
                    fileRepository.put(file!!)
                }
            } else if (part is PartData.FileItem) {
                if (name == null) {
                    loge("upload: name == null")
                    name = part.originalFileName
                }
                val javaFile = java.io.File(
                    fileRepository.getFolderContainerPath(),
                    name
                )
                part.streamProvider().use { inputStream ->
                    javaFile.outputStream().buffered().use { bufferedOutputStream ->
                        inputStream.copyToSuspend(bufferedOutputStream)
                    }
                }
                logd("upload: Upload ${javaFile.absolutePath}")
            }

            part.dispose()
        }
        if (file == null) {
            loge("upload: File not uploaded into the repository: file == null")
            return ServerResponse.create(
                "File not uploaded into the repository",
                false
            ).toJsonString()
        }
        return ServerResponseFile.create(
            file!!,
            "File uploaded into the repository",
            true
        ).toJsonString()
    }

    override suspend fun download(headers: Headers, body: String): java.io.File? {
        logd("download()")
        if (!authorizationManager.isAuthorized(headers)) {
            loge("download: Not logged")
            return null
        }
        val fileJsonObject = JSONObject(body)
        val path = fileJsonObject.getString(File.JSON_KEY_PATH)
        val folderContainerPath = fileRepository.getFolderContainerPath()
        return java.io.File(folderContainerPath, path)
    }

    /**
     * Utility boilerplate method that suspending,
     * copies a [this] [InputStream] into an [out] [OutputStream] in a separate thread.
     *
     * [bufferSize] and [yieldSize] allows to control how and when the suspending is performed.
     * The [dispatcher] allows to specify where will be this executed (for example a specific thread pool).
     * By default the dispatcher uses the [ioCoroutineDispatcher] intended I/O operations.
     */
    private suspend fun InputStream.copyToSuspend(
        out: OutputStream,
        bufferSize: Int = DEFAULT_BUFFER_SIZE,
        yieldSize: Int = 4 * 1_024 * 1_024,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): Long {
        return withContext(dispatcher) {
            val buffer = ByteArray(bufferSize)
            var bytesCopied = 0L
            var bytesAfterYield = 0L
            while (true) {
                val bytes = read(buffer).takeIf { it >= 0 } ?: break
                out.write(buffer, 0, bytes)
                if (bytesAfterYield >= yieldSize) {
                    yield()
                    bytesAfterYield %= yieldSize
                }
                bytesCopied += bytes
                bytesAfterYield += bytes
            }
            return@withContext bytesCopied
        }
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
