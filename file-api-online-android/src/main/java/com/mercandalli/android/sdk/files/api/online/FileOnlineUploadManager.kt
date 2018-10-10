package com.mercandalli.android.sdk.files.api.online

import com.mercandalli.sdk.files.api.File

interface FileOnlineUploadManager {

    fun upload(file: File)
}