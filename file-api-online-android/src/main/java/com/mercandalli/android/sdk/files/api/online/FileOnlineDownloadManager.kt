package com.mercandalli.android.sdk.files.api.online

import com.mercandalli.sdk.files.api.File

interface FileOnlineDownloadManager {

    fun download(file: File, javaFile: java.io.File)
}