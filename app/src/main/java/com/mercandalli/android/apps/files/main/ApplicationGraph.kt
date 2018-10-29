package com.mercandalli.android.apps.files.main

import android.annotation.SuppressLint
import android.content.Context
import com.mercandalli.android.apps.files.audio.AudioModule
import com.mercandalli.android.apps.files.dialog.DialogModule
import com.mercandalli.android.apps.files.network.NetworkModule
import com.mercandalli.android.apps.files.note.NoteModule
import com.mercandalli.android.apps.files.notification.NotificationModule
import com.mercandalli.android.apps.files.permission.PermissionActivity
import com.mercandalli.android.apps.files.developer.DeveloperModule
import com.mercandalli.android.apps.files.theme.ThemeModule
import com.mercandalli.android.apps.files.version.VersionModule
import com.mercandalli.android.apps.files.hash.HashModule
import com.mercandalli.android.apps.files.network.Network
import com.mercandalli.android.apps.files.toast.ToastModule
import com.mercandalli.android.sdk.files.api.FileModule
import com.mercandalli.android.sdk.files.api.PermissionRequestAddOn
import com.mercandalli.android.sdk.files.api.online.FileOnlineApiNetwork
import com.mercandalli.android.sdk.files.api.online.FileOnlineGraph
import org.json.JSONObject
import java.io.File

class ApplicationGraph(
        private val context: Context
) {
    private val fileModule by lazy { FileModule(context, createPermissionRequestAddOn()) }
    private val developerModule by lazy { DeveloperModule(context) }
    private val networkModule by lazy { NetworkModule() }
    private val noteModule by lazy { NoteModule(context) }

    private val audioManagerInternal by lazy { audioModuleInternal.createAudioManager() }
    private val audioQueueManagerInternal by lazy { audioModuleInternal.createAudioQueueManager(audioManagerInternal) }
    private val audioModuleInternal by lazy { AudioModule(fileSortManagerInternal) }
    private val developerManagerInternal by lazy { developerModule.createDeveloperManager() }
    private val dialogManagerInternal by lazy { DialogModule(context).createDialogManager() }
    private val fileManagerInternal by lazy { fileModule.createFileManager() }
    private val fileMediaScannerInternal by lazy { fileModule.getMediaScanner() }
    private val fileOpenManagerInternal by lazy { fileModule.createFileOpenManager() }
    private val fileDeleteManagerInternal by lazy { fileModule.createFileDeleteManager() }
    private val fileCopyCutManagerInternal by lazy { fileModule.createFileCopyCutManager() }
    private val fileCreatorManagerInternal by lazy { fileModule.createFileCreatorManager() }
    private val fileOnlineManagerInternal by lazy { FileOnlineGraph.getFileOnlineManager() }
    private val fileOnlineCopyCutManagerInternal by lazy { FileOnlineGraph.getFileOnlineCopyCutManager() }
    private val fileOnlineCreatorManagerInternal by lazy { FileOnlineGraph.getFileOnlineCreatorManager() }
    private val fileOnlineDeleteManagerInternal by lazy { FileOnlineGraph.getFileOnlineDeleteManager() }
    private val fileOnlineDownloadManagerInternal by lazy { FileOnlineGraph.getFileOnlineDownloadManager() }
    private val fileOnlineLoginManagerInternal by lazy { FileOnlineGraph.getFileOnlineLoginManager() }
    private val fileOnlineRenameManagerInternal by lazy { FileOnlineGraph.getFileOnlineRenameManager() }
    private val fileOnlineSizeManagerInternal by lazy { FileOnlineGraph.getFileOnlineSizeManager() }
    private val fileOnlineUploadManagerInternal by lazy { FileOnlineGraph.getFileOnlineUploadManager() }
    private val fileShareManagerInternal by lazy { fileModule.createFileShareManager() }
    private val fileRenameManagerInternal by lazy { fileModule.createFileRenameManager() }
    private val fileSizeManagerInternal by lazy { fileModule.createFileSizeManager() }
    private val fileSortManagerInternal by lazy { fileModule.createFileSortManager() }
    private val hashManagerInternal by lazy { HashModule(context).createHashManager() }
    private val network = networkModule.createNetwork()
    private val noteManagerInternal by lazy { noteModule.createNoteManager() }
    private val notificationModuleInternal by lazy { NotificationModule(context, audioManagerInternal) }
    private val notificationAudioManagerInternal by lazy { notificationModuleInternal.createNotificationAudioManager() }
    private val okHttpClientLazy = networkModule.createOkHttpClientLazy()
    private val themeManagerInternal by lazy { ThemeModule(context).createThemeManager() }
    private val toastManagerInternal by lazy { ToastModule(context).createToastManager() }
    private val versionManagerInternal by lazy { VersionModule(context).createVersionManager() }

    private fun createPermissionRequestAddOn() = object : PermissionRequestAddOn {
        override fun requestStoragePermission() {
            PermissionActivity.start(context)
        }
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var graph: ApplicationGraph? = null

        fun getAudioManager() = graph!!.audioManagerInternal
        fun getAudioQueueManager() = graph!!.audioQueueManagerInternal
        fun getDeveloperManager() = graph!!.developerManagerInternal
        fun getDialogManager() = graph!!.dialogManagerInternal
        fun getFileManager() = graph!!.fileManagerInternal
        fun getFileOpenManager() = graph!!.fileOpenManagerInternal
        fun getFileDeleteManager() = graph!!.fileDeleteManagerInternal
        fun getFileCopyCutManager() = graph!!.fileCopyCutManagerInternal
        fun getFileCreatorManager() = graph!!.fileCreatorManagerInternal
        fun getFileOnlineManager() = graph!!.fileOnlineManagerInternal
        fun getFileOnlineCopyCutManager() = graph!!.fileOnlineCopyCutManagerInternal
        fun getFileOnlineCreatorManager() = graph!!.fileOnlineCreatorManagerInternal
        fun getFileOnlineDeleteManager() = graph!!.fileOnlineDeleteManagerInternal
        fun getFileOnlineDownloadManager() = graph!!.fileOnlineDownloadManagerInternal
        fun getFileOnlineLoginManager() = graph!!.fileOnlineLoginManagerInternal
        fun getFileOnlineRenameManager() = graph!!.fileOnlineRenameManagerInternal
        fun getFileOnlineSizeManager() = graph!!.fileOnlineSizeManagerInternal
        fun getFileOnlineUploadManager() = graph!!.fileOnlineUploadManagerInternal
        fun getFileShareManager() = graph!!.fileShareManagerInternal
        fun getFileRenameManager() = graph!!.fileRenameManagerInternal
        fun getFileSizeManager() = graph!!.fileSizeManagerInternal
        fun getFileSortManager() = graph!!.fileSortManagerInternal
        fun getNetwork() = graph!!.network
        fun getHashManager() = graph!!.hashManagerInternal
        fun getNoteManager() = graph!!.noteManagerInternal
        fun getNotificationAudioManager() = graph!!.notificationAudioManagerInternal
        fun getOkHttpClientLazy() = graph!!.okHttpClientLazy
        fun getThemeManager() = graph!!.themeManagerInternal
        fun getToastManager() = graph!!.toastManagerInternal
        fun getVersionManager() = graph!!.versionManagerInternal

        fun init(context: Context) {
            if (graph == null) {
                val applicationContext = context.applicationContext
                graph = ApplicationGraph(
                        applicationContext
                )
                val fileOnlineApiNetwork = createFileOnlineApiNetwork()
                FileOnlineGraph.init(
                        applicationContext,
                        fileOnlineApiNetwork,
                        graph!!.fileMediaScannerInternal
                )
            }
        }

        private fun createFileOnlineApiNetwork() = object : FileOnlineApiNetwork {

            override fun getSync(
                    url: String,
                    headers: Map<String, String>
            ) = getNetwork().getSync(
                    url,
                    headers
            )

            override fun postSync(
                    url: String,
                    headers: Map<String, String>,
                    jsonObject: JSONObject
            ) = getNetwork().postSync(
                    url,
                    headers,
                    jsonObject
            )

            override fun postDownloadSync(
                    url: String,
                    headers: Map<String, String>,
                    jsonObject: JSONObject,
                    javaFile: File,
                    listener: FileOnlineApiNetwork.DownloadProgressListener
            ) = getNetwork().postDownloadSync(
                    url,
                    headers,
                    jsonObject,
                    javaFile,
                    object : Network.DownloadProgressListener {
                        override fun onDownloadProgress(current: Long, size: Long) {
                            listener.onDownloadProgress(current, size)
                        }
                    }
            )

            override fun postUploadSync(
                    url: String,
                    headers: Map<String, String>,
                    jsonObject: JSONObject,
                    javaFile: File,
                    listener: FileOnlineApiNetwork.UploadProgressListener
            ) = getNetwork().postUploadSync(
                    url,
                    headers,
                    jsonObject,
                    javaFile,
                    object : Network.UploadProgressListener {
                        override fun onUploadProgress(current: Long, size: Long) {
                            listener.onUploadProgress(current, size)
                        }
                    }
            )

            override fun deleteSync(
                    url: String,
                    headers: Map<String, String>,
                    jsonObject: JSONObject
            ) = getNetwork().deleteSync(
                    url,
                    headers,
                    jsonObject
            )
        }
    }
}