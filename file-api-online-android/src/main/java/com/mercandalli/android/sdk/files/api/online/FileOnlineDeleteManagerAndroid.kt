package com.mercandalli.android.sdk.files.api.online

import com.mercandalli.sdk.files.api.MediaScanner
import com.mercandalli.sdk.files.api.FileDeleteManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

internal class FileOnlineDeleteManagerAndroid(
        private val fileOnlineApi: FileOnlineApi,
        private val mediaScanner: MediaScanner
) : FileDeleteManager {

    private val listeners = ArrayList<FileDeleteManager.FileDeleteCompletionListener>()

    override fun delete(path: String) {
        GlobalScope.launch(Dispatchers.Default) {
            val succeeded = fileOnlineApi.delete(path)
            GlobalScope.launch(Dispatchers.Main) {
                if (succeeded) {
                    val ioFile = java.io.File(path)
                    val parentFile = ioFile.parentFile
                    if (parentFile != null) {
                        val parentPath = parentFile.absolutePath
                        mediaScanner.refresh(parentPath)
                    }
                    mediaScanner.refresh(path)
                }
                for (listener in listeners) {
                    listener.onFileDeletedCompleted(path, succeeded)
                }
            }
        }
    }

    override fun registerFileDeleteCompletionListener(listener: FileDeleteManager.FileDeleteCompletionListener) {
        if (listeners.contains(listener)) {
            return
        }
        listeners.add(listener)
    }

    override fun unregisterFileDeleteCompletionListener(listener: FileDeleteManager.FileDeleteCompletionListener) {
        listeners.remove(listener)
    }
}