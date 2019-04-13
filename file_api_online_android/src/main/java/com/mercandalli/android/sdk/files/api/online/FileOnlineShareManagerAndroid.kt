package com.mercandalli.android.sdk.files.api.online

import com.mercandalli.sdk.files.api.FileShareManager

internal class FileOnlineShareManagerAndroid : FileShareManager {

    override fun share(
        path: String
    ) {
        // Nothing here
    }

    override fun isShareSupported(
        path: String
    ) = false
}
