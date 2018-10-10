package com.mercandalli.android.sdk.files.api.online

import com.mercandalli.sdk.files.api.FileDeleteManager
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.android.Main
import kotlinx.coroutines.experimental.launch

internal class FileOnlineDeleteManagerAndroid(
        private val fileOnlineApi: FileOnlineApi
) : FileDeleteManager {

    private val listeners = ArrayList<FileDeleteManager.FileDeleteCompletionListener>()

    override fun delete(path: String) {
        GlobalScope.launch(Dispatchers.Default) {
            val succeeded = fileOnlineApi.delete(path)
            GlobalScope.launch(Dispatchers.Main) {
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