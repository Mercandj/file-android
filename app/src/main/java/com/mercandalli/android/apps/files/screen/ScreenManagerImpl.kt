package com.mercandalli.android.apps.files.screen

import android.content.Context
import com.mercandalli.android.apps.files.file_details.FileDetailsActivity

class ScreenManagerImpl(
    private val context: Context
) : ScreenManager {

    override fun showFileDetails(path: String) {
        FileDetailsActivity.start(
            context,
            path
        )
    }
}
