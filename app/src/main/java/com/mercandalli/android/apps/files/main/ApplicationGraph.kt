package com.mercandalli.android.apps.files.main

import android.annotation.SuppressLint
import android.content.Context
import com.mercandalli.android.apps.files.audio.AudioModule
import com.mercandalli.android.apps.files.dialog.DialogModule
import com.mercandalli.android.apps.files.network.NetworkModule
import com.mercandalli.android.apps.files.note.NoteModule
import com.mercandalli.android.apps.files.notification.NotificationModule
import com.mercandalli.android.apps.files.developer.DeveloperModule
import com.mercandalli.android.apps.files.file_storage_stats.FileStorageStatsModule
import com.mercandalli.android.apps.files.theme.ThemeModule
import com.mercandalli.android.apps.files.version.VersionModule
import com.mercandalli.android.apps.files.hash.HashModule
import com.mercandalli.android.apps.files.main_thread.MainThreadModule
import com.mercandalli.android.apps.files.network.NetworkManager
import com.mercandalli.android.apps.files.product.ProductModule
import com.mercandalli.android.apps.files.remote_config.RemoteConfigModule
import com.mercandalli.android.apps.files.screen.ScreenModule
import com.mercandalli.android.apps.files.split_install.SplitInstallModule
import com.mercandalli.android.apps.files.toast.ToastModule
import com.mercandalli.android.apps.files.update.UpdateModule
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
    private val screenModule by lazy { ScreenModule(context) }

    private val audioManager by lazy { audioModuleInternal.createAudioManager() }
    private val audioQueueManager by lazy { audioModuleInternal.createAudioQueueManager(audioManager) }
    private val audioModuleInternal by lazy { AudioModule(fileSortManagerInternal) }
    private val developerManager by lazy { developerModule.createDeveloperManager() }
    private val dialogManager by lazy { DialogModule(context).createDialogManager() }
    private val fileManager by lazy { fileModule.createFileManager() }
    private val fileChildrenManager by lazy { fileModule.createFileChildrenManager() }
    private val fileMediaScannerInternal by lazy { fileModule.getMediaScanner() }
    private val fileOpenManager by lazy { fileModule.createFileOpenManager() }
    private val fileDeleteManager by lazy { fileModule.createFileDeleteManager() }
    private val fileCopyCutManager by lazy { fileModule.createFileCopyCutManager() }
    private val fileCreatorManager by lazy { fileModule.createFileCreatorManager() }
    private val fileOnlineManager by lazy { FileOnlineGraph.getFileOnlineManager() }
    private val fileOnlineChildrenManager by lazy { FileOnlineGraph.getFileOnlineChildrenManager() }
    private val fileOnlineCopyCutManager by lazy { FileOnlineGraph.getFileOnlineCopyCutManager() }
    private val fileOnlineCreatorManager by lazy { FileOnlineGraph.getFileOnlineCreatorManager() }
    private val fileOnlineDeleteManagerInternal by lazy { FileOnlineGraph.getFileOnlineDeleteManager() }
    private val fileOnlineDownloadManagerInternal by lazy { FileOnlineGraph.getFileOnlineDownloadManager() }
    private val fileOnlineLoginManagerInternal by lazy { FileOnlineGraph.getFileOnlineLoginManager() }
    private val fileOnlineRenameManagerInternal by lazy { FileOnlineGraph.getFileOnlineRenameManager() }
    private val fileOnlineShareManagerInternal by lazy { FileOnlineGraph.getFileOnlineShareManager() }
    private val fileOnlineSizeManagerInternal by lazy { FileOnlineGraph.getFileOnlineSizeManager() }
    private val fileOnlineUploadManagerInternal by lazy { FileOnlineGraph.getFileOnlineUploadManager() }
    private val fileShareManagerInternal by lazy { fileModule.createFileShareManager() }
    private val fileStorageStatsManagerInternal by lazy { FileStorageStatsModule().createFileStorageStatsManager() }
    private val fileRenameManagerInternal by lazy { fileModule.createFileRenameManager() }
    private val fileSizeManagerInternal by lazy { fileModule.createFileSizeManager() }
    private val fileSortManagerInternal by lazy { fileModule.createFileSortManager() }
    private val hashManagerInternal by lazy { HashModule(context).createHashManager() }
    private val mainThreadPostInternal by lazy { MainThreadModule().createMainThreadPost() }
    private val network by lazy { networkModule.createNetwork() }
    private val noteManagerInternal by lazy { noteModule.createNoteManager() }
    private val notificationModuleInternal by lazy { NotificationModule(context, audioManager) }
    private val notificationAudioManagerInternal by lazy { notificationModuleInternal.createNotificationAudioManager() }
    private val okHttpClientLazy by lazy { networkModule.createOkHttpClientLazy() }
    private val productManagerInternal by lazy { ProductModule(context).createProductManager() }
    private val remoteConfigInternal by lazy { RemoteConfigModule().createRemoteConfig() }
    private val screenManagerInternal by lazy { screenModule.createScreenManager() }
    private val splitInstallManagerInternal by lazy { SplitInstallModule(context).createSplitInstallManager() }
    private val themeManagerInternal by lazy { ThemeModule(context).createThemeManager() }
    private val toastManagerInternal by lazy { ToastModule(context).createToastManager() }
    private val updateManagerInternal by lazy { UpdateModule(context).createUpdateManager() }
    private val versionManager by lazy { VersionModule(context).createVersionManager() }

    private fun createPermissionRequestAddOn() = object : PermissionRequestAddOn {
        override fun requestStoragePermission() {
            screenManagerInternal.startPermission()
        }
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var graph: ApplicationGraph? = null

        fun getAudioManager() = graph!!.audioManager
        fun getAudioQueueManager() = graph!!.audioQueueManager
        fun getDeveloperManager() = graph!!.developerManager
        fun getDialogManager() = graph!!.dialogManager
        fun getFileManager() = graph!!.fileManager
        fun getFileModule() = graph!!.fileModule
        fun getFileChildrenManager() = graph!!.fileChildrenManager
        fun getFileOpenManager() = graph!!.fileOpenManager
        fun getFileDeleteManager() = graph!!.fileDeleteManager
        fun getFileCopyCutManager() = graph!!.fileCopyCutManager
        fun getFileCreatorManager() = graph!!.fileCreatorManager
        fun getFileOnlineManager() = graph!!.fileOnlineManager
        fun getFileOnlineChildrenManager() = graph!!.fileOnlineChildrenManager
        fun getFileOnlineCopyCutManager() = graph!!.fileOnlineCopyCutManager
        fun getFileOnlineCreatorManager() = graph!!.fileOnlineCreatorManager
        fun getFileOnlineDeleteManager() = graph!!.fileOnlineDeleteManagerInternal
        fun getFileOnlineDownloadManager() = graph!!.fileOnlineDownloadManagerInternal
        fun getFileOnlineLoginManager() = graph!!.fileOnlineLoginManagerInternal
        fun getFileOnlineRenameManager() = graph!!.fileOnlineRenameManagerInternal
        fun getFileOnlineShareManager() = graph!!.fileOnlineShareManagerInternal
        fun getFileOnlineSizeManager() = graph!!.fileOnlineSizeManagerInternal
        fun getFileOnlineUploadManager() = graph!!.fileOnlineUploadManagerInternal
        fun getFileShareManager() = graph!!.fileShareManagerInternal
        fun getFileStorageStatsManager() = graph!!.fileStorageStatsManagerInternal
        fun getFileRenameManager() = graph!!.fileRenameManagerInternal
        fun getFileSizeManager() = graph!!.fileSizeManagerInternal
        fun getFileSortManager() = graph!!.fileSortManagerInternal
        fun getMainThreadPost() = graph!!.mainThreadPostInternal
        fun getNetwork() = graph!!.network
        fun getHashManager() = graph!!.hashManagerInternal
        fun getNoteManager() = graph!!.noteManagerInternal
        fun getNotificationAudioManager() = graph!!.notificationAudioManagerInternal
        fun getOkHttpClientLazy() = graph!!.okHttpClientLazy
        fun getProductManager() = graph!!.productManagerInternal
        fun getRemoteConfig() = graph!!.remoteConfigInternal
        fun getScreenManager() = graph!!.screenManagerInternal
        fun getSplitInstallManager() = graph!!.splitInstallManagerInternal
        fun getThemeManager() = graph!!.themeManagerInternal
        fun getToastManager() = graph!!.toastManagerInternal
        fun getUpdateManager() = graph!!.updateManagerInternal
        fun getVersionManager() = graph!!.versionManager

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
                object : NetworkManager.DownloadProgressListener {
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
                object : NetworkManager.UploadProgressListener {
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
