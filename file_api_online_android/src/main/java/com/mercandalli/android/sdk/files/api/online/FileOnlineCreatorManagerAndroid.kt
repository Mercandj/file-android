package com.mercandalli.android.sdk.files.api.online

import com.mercandalli.sdk.files.api.File
import com.mercandalli.sdk.files.api.FileCreatorManager
import com.mercandalli.sdk.files.api.MediaScanner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

internal class FileOnlineCreatorManagerAndroid(
    private val fileOnlineApi: FileOnlineApi,
    private val mediaScanner: MediaScanner
) : FileCreatorManager {

    override fun create(
        parentPath: String,
        name: String
    ) {
        val file = File.create(
            parentPath,
            name
        )
        GlobalScope.launch(Dispatchers.Default) {
            fileOnlineApi.post(file)
            GlobalScope.launch(Dispatchers.Main) {
                mediaScanner.refresh(file.path)
                file.parentPath?.let {
                    mediaScanner.refresh(it)
                }
            }
        }
    }
}
