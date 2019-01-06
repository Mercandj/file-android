package com.mercandalli.android.apps.files.screen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.Settings
import com.mercandalli.android.apps.files.file.FileProvider
import com.mercandalli.android.apps.files.file_details.FileDetailsActivity

class ScreenManagerImpl(
    private val context: Context
) : ScreenManager {

    override fun showFileDetails(
        path: String,
        fileProvider: FileProvider
    ) {
        FileDetailsActivity.start(
            context,
            path,
            fileProvider
        )
    }

    override fun openSystemSettingsStorage() {
        val intent = Intent(Settings.ACTION_MEMORY_CARD_SETTINGS)
        if (context !is Activity) {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        context.startActivity(intent)
    }
}
