package com.mercandalli.android.sdk.files.api.online

import com.mercandalli.sdk.files.api.MediaScanner
import com.mercandalli.sdk.files.api.File
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.android.Main
import kotlinx.coroutines.experimental.launch

@Suppress("EXPERIMENTAL_FEATURE_WARNING")
internal class FileOnlineUploadManagerImpl(
        private val fileOnlineApi: FileOnlineApi,
        private val mediaScanner: MediaScanner
) : FileOnlineUploadManager {

    private val listeners = ArrayList<FileOnlineUploadManager.UploadListener>()
    private val uploadProgressListener = createUploadProgressListener()

    override fun upload(
            file: File,
            javaFile: java.io.File
    ) {
        notifyUploadStarted(file)
        GlobalScope.launch(Dispatchers.Default) {
            fileOnlineApi.postUpload(
                    file,
                    javaFile,
                    uploadProgressListener
            )
            GlobalScope.launch(Dispatchers.Main) {
                mediaScanner.refresh(file.path)
                file.parentPath?.let {
                    mediaScanner.refresh(it)
                }
                notifyUploadEnded(file)
            }
        }
    }

    override fun registerListener(
            listener: FileOnlineUploadManager.UploadListener
    ) {
        if (listeners.contains(listener)) {
            return
        }
        listeners.add(listener)
    }

    override fun unregisterListener(
            listener: FileOnlineUploadManager.UploadListener
    ) {
        listeners.remove(listener)
    }

    private fun notifyUploadStarted(
            file: File
    ) {
        for (listener in listeners) {
            listener.onUploadStarted(file)
        }
    }

    private fun notifyUploadProgress(
            file: File,
            current: Long,
            size: Long
    ) {
        for (listener in listeners) {
            listener.onUploadProgress(file, current, size)
        }
    }

    private fun notifyUploadEnded(
            file: File
    ) {
        for (listener in listeners) {
            listener.onUploadEnded(file)
        }
    }

    private fun createUploadProgressListener() = object : FileOnlineApi.UploadProgressListener {
        override fun onUploadProgress(
                file: File,
                current: Long,
                size: Long
        ) {
            notifyUploadProgress(
                    file,
                    current,
                    size
            )
        }
    }
}