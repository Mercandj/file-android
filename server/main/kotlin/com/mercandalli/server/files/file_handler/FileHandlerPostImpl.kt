package com.mercandalli.server.files.file_handler

import com.mercandalli.sdk.files.api.File
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
        val debugMessage = StringBuilder()
        logManager.d(TAG, "createPost(body: $body)")
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
        javaFile.createNewFile()
        return ServerResponseFile.create(
                file,
                debugMessage.toString(),
                true
        ).toJsonString()
    }

    override fun renamePost(body: String): String {
        logManager.d(TAG, "renamePost(body: $body)")
        val fileJsonObject = JSONObject(body)
        val path = fileJsonObject.getString(File.JSON_KEY_PATH)
        val name = fileJsonObject.getString(File.JSON_KEY_NAME)
        val renamedFile = fileRepository.rename(path, name) ?: return ServerResponse.create(
                "File not renamed. Maybe not found in the server",
                false
        ).toJsonString()
        return ServerResponseFile.create(
                renamedFile,
                "File renamed in the repository",
                true
        ).toJsonString()
    }

    override suspend fun uploadPost(
            headers: Headers,
            multipart: MultiPartData
    ): String {
        logManager.d(TAG, "uploadPost()")

        val authorization = headers["Authorization"]
        val logged = if (authorization == null) {
            false
        } else {
            val token = authorization.replace("Basic ", "")
            FileOnlineAuthentication.isLogged(token, fileOnlineAuthentications)
        }
        logManager.d(TAG, "uploadPost() logged: $logged")
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
                    logManager.d(TAG, "uploadPost(json: $json)")
                    val fileJsonObject = JSONObject(json)
                    file = File.fromJson(fileJsonObject)
                    name = file!!.name
                    fileRepository.put(file!!)
                }
            } else if (part is PartData.FileItem) {
                if (name == null) {
                    logManager.d(TAG, "uploadPost() name == null")
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
                logManager.d(TAG, "uploadPost() upload ${javaFile.absolutePath}")
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
            yieldSize: Int = 4 * 1024 * 1024,
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

    companion object {
        private const val TAG = "FileHandlerPost"
    }
}