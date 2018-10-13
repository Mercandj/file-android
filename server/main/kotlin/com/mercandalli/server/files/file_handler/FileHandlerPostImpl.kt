package com.mercandalli.server.files.file_handler

import com.mercandalli.sdk.files.api.File
import com.mercandalli.sdk.files.api.FileCopyCutUtils
import com.mercandalli.sdk.files.api.FileRenameUtils
import com.mercandalli.sdk.files.api.online.FileOnlineAuthentication
import com.mercandalli.server.files.file_repository.FileRepository
import com.mercandalli.server.files.log.LogManager
import com.mercandalli.sdk.files.api.online.response_json.ServerResponse
import com.mercandalli.sdk.files.api.online.response_json.ServerResponseFile
import io.ktor.content.MultiPartData
import io.ktor.content.PartData
import io.ktor.content.forEachPart
import io.ktor.http.Headers
import io.ktor.network.util.ioCoroutineDispatcher
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.withContext
import kotlinx.coroutines.experimental.yield
import org.json.JSONObject
import java.io.InputStream
import java.io.OutputStream
import java.lang.StringBuilder

class FileHandlerPostImpl(
        private val fileRepository: FileRepository,
        private val logManager: LogManager,
        private val fileOnlineAuthentications: List<FileOnlineAuthentication>
) : FileHandlerPost {

    override fun createPost(body: String): String {
        logd("createPost(body: $body)")
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

    override fun renamePost(body: String): String {
        logd("renamePost(body: $body)")
        val fileJsonObject = JSONObject(body)
        val path = fileJsonObject.getString(File.JSON_KEY_PATH)
        val name = fileJsonObject.getString(File.JSON_KEY_NAME)
        val renamedFile = fileRepository.rename(path, name)
        if (renamedFile == null) {
            loge("renamePost: Repository rename failed")
            return ServerResponse.create(
                    "File not renamed. Maybe not found in the server",
                    false
            ).toJsonString()
        }
        if (name.contains("/")) {
            loge("renamePost: Name should not contains /. name == $name")
            return ServerResponse.create(
                    "Name should not contains /. name == $name",
                    false
            ).toJsonString()
        }
        logd("renamePost: repository rename succeeded")
        val folderContainerPath = fileRepository.getFolderContainerPath()
        val javaFile = java.io.File(folderContainerPath, path)
        val renameSucceeded = FileRenameUtils.renameSync(javaFile, name)
        if (!renameSucceeded) {
            loge("renamePost: File not renamed")
            // Revert rename in the repo
            fileRepository.rename(renamedFile.path, java.io.File(path).name)
            return ServerResponse.create(
                    "File not renamed. Java file rename failed",
                    false
            ).toJsonString()
        }
        logd("renamePost: File renamed")
        return ServerResponseFile.create(
                renamedFile,
                "File renamed",
                true
        ).toJsonString()
    }

    override fun copyPost(body: String): String {
        logd("copyPost(body: $body)")
        val fileJsonObject = JSONObject(body)
        val path = fileJsonObject.getString(File.JSON_KEY_PATH)
        val pathDirectoryOutput = fileJsonObject.getString("path_directory_output")
        val copiedFile = fileRepository.copy(path, pathDirectoryOutput)
        if (copiedFile == null) {
            loge("copyPost: Repository copy failed")
            return ServerResponse.create(
                    "File not copy. Maybe not found in the server",
                    false
            ).toJsonString()
        }
        val folderContainerPath = fileRepository.getFolderContainerPath()
        val javaFileInput = java.io.File(folderContainerPath, path)
        val javaFileDirectoryOutput = java.io.File(folderContainerPath, pathDirectoryOutput)
        val succeeded = FileCopyCutUtils.copyJavaFileSync(
                javaFileInput.absolutePath,
                javaFileDirectoryOutput.absolutePath
        )
        if (!succeeded) {
            loge("copyPost: File not copy")
            return ServerResponse.create(
                    "File not copied",
                    false
            ).toJsonString()
        }
        logd("copyPost: File copied")
        return ServerResponseFile.create(
                copiedFile,
                "File copied",
                true
        ).toJsonString()
    }

    override fun cutPost(body: String): String {
        logd("cutPost(body: $body)")
        val fileJsonObject = JSONObject(body)
        val path = fileJsonObject.getString(File.JSON_KEY_PATH)
        val pathDirectoryOutput = fileJsonObject.getString("path_directory_output")
        val cutFile = fileRepository.cut(path, pathDirectoryOutput)
        if (cutFile == null) {
            loge("cutPost: Repository cut failed")
            return ServerResponse.create(
                    "File not cut. Maybe not found in the server",
                    false
            ).toJsonString()
        }
        val folderContainerPath = fileRepository.getFolderContainerPath()
        val javaFileInput = java.io.File(folderContainerPath, path)
        val javaFileDirectoryOutput = java.io.File(folderContainerPath, pathDirectoryOutput)
        val succeeded = FileCopyCutUtils.cutJavaFileSync(
                javaFileInput.absolutePath,
                javaFileDirectoryOutput.absolutePath
        )
        if (!succeeded) {
            loge("cutPost: File not cut")
            return ServerResponse.create(
                    "File not cut",
                    false
            ).toJsonString()
        }
        logd("cutPost: File cut")
        return ServerResponseFile.create(
                cutFile,
                "File cut",
                true
        ).toJsonString()
    }

    override suspend fun uploadPost(
            headers: Headers,
            multipart: MultiPartData
    ): String {
        logd("uploadPost()")
        val authorization = headers["Authorization"]
        val logged = if (authorization == null) {
            false
        } else {
            val token = authorization.replace("Basic ", "")
            FileOnlineAuthentication.isLogged(token, fileOnlineAuthentications)
        }
        if (!logged) {
            loge("uploadPost: Not logged")
            return ServerResponse.create(
                    "Oops, not logged",
                    false
            ).toJsonString()
        }
        logd("uploadPost(): Logged")

        // Processes each part of the multipart input content of the user
        var name: String? = null
        var file: File? = null
        multipart.forEachPart { part ->
            if (part is PartData.FormItem) {
                if (part.name == "json") {
                    val json = part.value
                    logd("uploadPost(json: $json)")
                    val fileJsonObject = JSONObject(json)
                    file = File.fromJson(fileJsonObject)
                    name = file!!.name
                    fileRepository.put(file!!)
                }
            } else if (part is PartData.FileItem) {
                if (name == null) {
                    loge("uploadPost: name == null")
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
                logd("uploadPost: Upload ${javaFile.absolutePath}")
            }

            part.dispose()
        }
        if (file == null) {
            loge("uploadPost: File not uploaded into the repository: file == null")
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
            dispatcher: CoroutineDispatcher = ioCoroutineDispatcher
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