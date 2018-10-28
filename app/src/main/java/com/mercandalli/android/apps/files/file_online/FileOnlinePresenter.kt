package com.mercandalli.android.apps.files.file_online

import com.mercandalli.android.apps.files.dialog.DialogManager
import com.mercandalli.android.apps.files.R

class FileOnlinePresenter(
        private val screen: FileOnlineContract.Screen,
        private val dialogManager: DialogManager
) : FileOnlineContract.UserAction {

    private var path: String? = null

    override fun onAttached() {
    }

    override fun onDetached() {
    }

    override fun onFileOpenClicked(path: String) {
        this.path = path
        dialogManager.alert(
                DIALOG_DOWNLOAD_ID,
                R.string.file_online_download_title,
                R.string.file_online_download_message,
                R.string.file_online_download_positive,
                R.string.file_online_download_negative
        )
    }

    companion object {
        private const val DIALOG_DOWNLOAD_ID = "DIALOG_DOWNLOAD_ID"
    }
}