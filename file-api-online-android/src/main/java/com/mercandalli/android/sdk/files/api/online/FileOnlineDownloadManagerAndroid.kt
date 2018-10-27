package com.mercandalli.android.sdk.files.api.online

import com.mercandalli.sdk.files.api.File
import com.mercandalli.sdk.files.api.MediaScanner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

internal class FileOnlineDownloadManagerAndroid(
        private val fileOnlineApi: FileOnlineApi,
        private val mediaScanner: MediaScanner
) : FileOnlineDownloadManager {

    private val listeners = ArrayList<FileOnlineDownloadManager.DownloadListener>()
    private val uploadProgressListener = createUploadProgressListener()

    override fun download(
            inputFile: File,
            outputJavaFile: java.io.File
    ) {
        notifyDownloadStarted(inputFile)
        GlobalScope.launch(Dispatchers.Default) {
            fileOnlineApi.getDownload(
                    inputFile,
                    outputJavaFile,
                    uploadProgressListener
            )
            GlobalScope.launch(Dispatchers.Main) {
                val outputFile = File.create(outputJavaFile)
                mediaScanner.refresh(outputFile.path)
                outputFile.parentPath?.let {
                    mediaScanner.refresh(it)
                }
                notifyDownloadEnded(outputFile)
            }
        }
    }

    override fun registerListener(
            listener: FileOnlineDownloadManager.DownloadListener
    ) {
        if (listeners.contains(listener)) {
            return
        }
        listeners.add(listener)
    }

    override fun unregisterListener(
            listener: FileOnlineDownloadManager.DownloadListener
    ) {
        listeners.remove(listener)
    }

    private fun notifyDownloadStarted(
            file: File
    ) {
        for (listener in listeners) {
            listener.onDownloadStarted(file)
        }
    }

    private fun notifyDownloadProgress(
            file: File,
            current: Long,
            size: Long
    ) {
        for (listener in listeners) {
            listener.onDownloadProgress(file, current, size)
        }
    }

    private fun notifyDownloadEnded(
            file: File
    ) {
        for (listener in listeners) {
            listener.onDownloadEnded(file)
        }
    }

    private fun createUploadProgressListener() = object : FileOnlineApi.DownloadProgressListener {
        override fun onDownloadProgress(
                file: File,
                current: Long,
                size: Long
        ) {
            notifyDownloadProgress(
                    file,
                    current,
                    size
            )
        }
    }
}