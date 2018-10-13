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
        log("createPost(body: $body)")
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
        log("renamePost(body: $body)")
        val fileJsonObject = JSONObject(body)
        val path = fileJsonObject.getString(File.JSON_KEY_PATH)
        val name = fileJsonObject.getString(File.JSON_KEY_NAME)
        val renamedFile = fileRepository.rename(path, name) ?: return ServerResponse.create(
                "File not renamed. Maybe not found in the server",
                false
        ).toJsonString()
        val folderContainerPath = fileRepository.getFolderContainerPath()
        val javaFile = java.io.File(folderContainerPath, path)
        val renameSucceeded = FileRenameUtils.renameSync(javaFile, name)
        if (!renameSucceeded) {
            log("renamePost: File not renamed")
            // Revert rename in the repo
            fileRepository.rename(renamedFile.path, java.io.File(path).name)
            return ServerResponse.create(
                    "File not renamed. Java file rename failed",
                    false
            ).toJsonString()
        }
        log("renamePost: File renamed")
        return ServerResponseFile.create(
                renamedFile,
                "File renamed",
                true
        ).toJsonString()
    }

    override fun copyPost(body: String): String {
        log("copyPost(body: $body)")
        val fileJsonObject = JSONObject(body)
        val path = fileJsonObject.getString(File.JSON_KEY_PATH)
        val pathOutput = fileJsonObject.getString("path_output")
        val copiedFile = fileRepository.copy(path, pathOutput) ?: return ServerResponse.create(
                "File not copy. Maybe not found in the server",
                false
        ).toJsonString()
        val folderContainerPath = fileRepository.getFolderContainerPath()
        val javaFileInput = java.io.File(folderContainerPath, path)
        val javaFileOutput = java.io.File(folderContainerPath, pathOutput)
        val succeeded = FileCopyCutUtils.copyJavaFileSync(
                javaFileInput.absolutePath,
                javaFileOutput.absolutePath
        )
        if (!succeeded) {
            log("copyPost: File not copy")
            return ServerResponse.create(
                    "File not copied",
                    false
            ).toJsonString()
        }
        log("copyPost: File copied")
        return ServerResponseFile.create(
                copiedFile,
                "File copied",
                true
        ).toJsonString()
    }

    override fun cutPost(body: String): String {
        log("cutPost(body: $body)")
        val fileJsonObject = JSONObject(body)
        val path = fileJsonObject.getString(File.JSON_KEY_PATH)
        val pathOutput = fileJsonObject.getString("path_output")
        val cutFile = fileRepository.cut(path, pathOutput) ?: return ServerResponse.create(
                "File not cut. Maybe not found in the server",
                false
        ).toJsonString()
        val folderContainerPath = fileRepository.getFolderContainerPath()
        val javaFileInput = java.io.File(folderContainerPath, path)
        val javaFileOutput = java.io.File(folderContainerPath, pathOutput)
        val succeeded = FileCopyCutUtils.cutJavaFileSync(
                javaFileInput.absolutePath,
                javaFileOutput.absolutePath
        )
        if (!succeeded) {
            log("cutPost: File not cut")
            return ServerResponse.create(
                    "File not cut",
                    false
            ).toJsonString()
        }
        log("cutPost: File cut")
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
        log("uploadPost()")
        val authorization = headers["Authorization"]
        val logged = if (authorization == null) {
            false
        } else {
            val token = authorization.replace("Basic ", "")
            FileOnlineAuthentication.isLogged(token, fileOnlineAuthentications)
        }
        log("uploadPost() logged: $logged")
        if (!logged) {
            return ServerResponse.create(
                    "Oops, not logged",
                    false
            ).toJsonString()
        }

        // Processes each part of the multipart input content of the user
        var name: String? = null
        var file: File? = null
        multipart.forEachPart { part ->
            if (part is PartData.FormItem) {
                if (part.name == "json") {
                    val json = part.value
                    log("uploadPost(json: $json)")
                    val fileJsonObject = JSONObject(json)
                    file = File.fromJson(fileJsonObject)
                    name = file!!.name
                    fileRepository.put(file!!)
                }
            } else if (part is PartData.FileItem) {
                if (name == null) {
                    log("uploadPost() name == null")
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
                log("uploadPost() upload ${javaFile.absolutePath}")
            }

            part.dispose()
        }
        if (file == null) {
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

    private fun log(message: String) {
        logManager.d(TAG, message)
    }

    companion object {
        private const val TAG = "FileHandlerPost"
    }
}