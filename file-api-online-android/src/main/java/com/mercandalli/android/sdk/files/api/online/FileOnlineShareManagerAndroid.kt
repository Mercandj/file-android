package com.mercandalli.android.sdk.files.api.online

import com.mercandalli.sdk.files.api.FileShareManager

internal class FileOnlineShareManagerAndroid : FileShareManager {

    override fun share(path: String) {

    }

    override fun isShareSupported(path: String) = false
}
