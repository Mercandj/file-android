package com.mercandalli.android.apps.files.file_online

import com.mercandalli.android.apps.files.dialog.DialogManager
import com.mercandalli.android.apps.files.R
import com.mercandalli.android.apps.files.toast.ToastManager
import com.mercandalli.android.sdk.files.api.online.FileOnlineDownloadManager

class FileOnlinePresenter(
        private val screen: FileOnlineContract.Screen,
        private val dialogManager: DialogManager,
        private val fileOnlineDownloadManager: FileOnlineDownloadManager,
        private val toastManager: ToastManager,
        private val rootPath: String
) : FileOnlineContract.UserAction {

    private var path: String? = null
    private val dialogListener = createDialogListener()

    override fun onAttached() {
        dialogManager.registerListener(dialogListener)
    }

    override fun onDetached() {
        dialogManager.unregisterListener(dialogListener)
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

    private fun download() {
        val currentPath = path
        if (currentPath == null) {
            toastManager.toast("Oops, there is an error")
            return
        }
        val filespaceLocalJavaFile = java.io.File(rootPath, "filespace")
        if (!filespaceLocalJavaFile.exists()) {
            filespaceLocalJavaFile.mkdirs()
        }
        toastManager.toast("Download started")
        val outputJavaFile = java.io.File(
                filespaceLocalJavaFile,
                currentPath
        )
        val outputJavaParentFile = outputJavaFile.parentFile
        if (!outputJavaParentFile.exists()) {
            outputJavaParentFile.mkdirs()
        }
        fileOnlineDownloadManager.download(
                currentPath,
                outputJavaFile
        )
    }

    private fun createDialogListener() = object : DialogManager.Listener {

        override fun onDialogPositiveClicked(dialogAction: DialogManager.DialogAction): Boolean {
            if (dialogAction.dialogId != DIALOG_DOWNLOAD_ID) {
                return false
            }
            download()
            return true
        }

        override fun onDialogNegativeClicked(dialogAction: DialogManager.DialogAction) {
        }
    }

    companion object {
        private const val DIALOG_DOWNLOAD_ID = "DIALOG_DOWNLOAD_ID"
    }
}