package com.mercandalli.android.sdk.files.api.online

import com.mercandalli.sdk.files.api.MediaScanner
import com.mercandalli.sdk.files.api.File
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.android.Main
import kotlinx.coroutines.experimental.launch

internal class FileOnlineUploadManagerImpl(
        private val fileOnlineApi: FileOnlineApi,
        private val mediaScanner: MediaScanner
) : FileOnlineUploadManager {

    override fun upload(file: File, javaFile: java.io.File) {
        GlobalScope.launch(Dispatchers.Default) {
            fileOnlineApi.post(file, javaFile)
            GlobalScope.launch(Dispatchers.Main) {
                mediaScanner.refresh(file.path)
                file.parentPath?.let {
                    mediaScanner.refresh(it)
                }
            }
        }
    }
}