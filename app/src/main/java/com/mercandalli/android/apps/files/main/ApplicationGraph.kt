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
import com.mercandalli.android.apps.files.ram_stats.RamStatsModule
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

    private val audioManager by lazy { audioModule.createAudioManager() }
    private val audioQueueManager by lazy { audioModule.createAudioQueueManager(audioManager) }
    private val audioModule by lazy { AudioModule(fileSortManager) }
    private val developerManager by lazy { developerModule.createDeveloperManager() }
    private val dialogManager by lazy { DialogModule(context).createDialogManager() }
    private val fileManager by lazy { fileModule.createFileManager() }
    private val fileChildrenManager by lazy { fileModule.createFileChildrenManager() }
    private val fileMediaScanner by lazy { fileModule.getMediaScanner() }
    private val fileOpenManager by lazy { fileModule.createFileOpenManager() }
    private val fileDeleteManager by lazy { fileModule.createFileDeleteManager() }
    private val fileCopyCutManager by lazy { fileModule.createFileCopyCutManager() }
    private val fileCreatorManager by lazy { fileModule.createFileCreatorManager() }
    private val fileOnlineManager by lazy { FileOnlineGraph.getFileOnlineManager() }
    private val fileOnlineChildrenManager by lazy { FileOnlineGraph.getFileOnlineChildrenManager() }
    private val fileOnlineCopyCutManager by lazy { FileOnlineGraph.getFileOnlineCopyCutManager() }
    private val fileOnlineCreatorManager by lazy { FileOnlineGraph.getFileOnlineCreatorManager() }
    private val fileOnlineDeleteManager by lazy { FileOnlineGraph.getFileOnlineDeleteManager() }
    private val fileOnlineDownloadManager by lazy { FileOnlineGraph.getFileOnlineDownloadManager() }
    private val fileOnlineLoginManager by lazy { FileOnlineGraph.getFileOnlineLoginManager() }
    private val fileOnlineRenameManager by lazy { FileOnlineGraph.getFileOnlineRenameManager() }
    private val fileOnlineShareManager by lazy { FileOnlineGraph.getFileOnlineShareManager() }
    private val fileOnlineSizeManager by lazy { FileOnlineGraph.getFileOnlineSizeManager() }
    private val fileOnlineUploadManager by lazy { FileOnlineGraph.getFileOnlineUploadManager() }
    private val fileShareManager by lazy { fileModule.createFileShareManager() }
    private val fileStorageStatsManager by lazy { FileStorageStatsModule().createFileStorageStatsManager() }
    private val fileRenameManager by lazy { fileModule.createFileRenameManager() }
    private val fileSizeManager by lazy { fileModule.createFileSizeManager() }
    private val fileSortManager by lazy { fileModule.createFileSortManager() }
    private val hashManager by lazy { HashModule(context).createHashManager() }
    private val mainThreadPost by lazy { MainThreadModule().createMainThreadPost() }
    private val networkManager by lazy { networkModule.createNetworkManager() }
    private val noteManager by lazy { noteModule.createNoteManager() }
    private val notificationModule by lazy { NotificationModule(context, audioManager) }
    private val notificationAudioManager by lazy { notificationModule.createNotificationAudioManager() }
    private val okHttpClientLazy by lazy { networkModule.createOkHttpClientLazy() }
    private val productManager by lazy { ProductModule(context).createProductManager() }
    private val ramStatsManager by lazy { RamStatsModule(context).createRamStatsManager() }
    private val remoteConfig by lazy { RemoteConfigModule().createRemoteConfig() }
    private val screenManager by lazy { screenModule.createScreenManager() }
    private val splitInstallManager by lazy { SplitInstallModule(context).createSplitInstallManager() }
    private val themeManager by lazy { ThemeModule(context).createThemeManager() }
    private val toastManager by lazy { ToastModule(context).createToastManager() }
    private val updateManager by lazy { UpdateModule(context).createUpdateManager() }
    private val versionManager by lazy { VersionModule(context).createVersionManager() }

    private fun createPermissionRequestAddOn() = object : PermissionRequestAddOn {
        override fun requestStoragePermission() {
            screenManager.startPermission()
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
        fun getFileOnlineDeleteManager() = graph!!.fileOnlineDeleteManager
        fun getFileOnlineDownloadManager() = graph!!.fileOnlineDownloadManager
        fun getFileOnlineLoginManager() = graph!!.fileOnlineLoginManager
        fun getFileOnlineRenameManager() = graph!!.fileOnlineRenameManager
        fun getFileOnlineShareManager() = graph!!.fileOnlineShareManager
        fun getFileOnlineSizeManager() = graph!!.fileOnlineSizeManager
        fun getFileOnlineUploadManager() = graph!!.fileOnlineUploadManager
        fun getFileShareManager() = graph!!.fileShareManager
        fun getFileStorageStatsManager() = graph!!.fileStorageStatsManager
        fun getFileRenameManager() = graph!!.fileRenameManager
        fun getFileSizeManager() = graph!!.fileSizeManager
        fun getFileSortManager() = graph!!.fileSortManager
        fun getMainThreadPost() = graph!!.mainThreadPost
        fun getNetworkManager() = graph!!.networkManager
        fun getHashManager() = graph!!.hashManager
        fun getNoteManager() = graph!!.noteManager
        fun getNotificationAudioManager() = graph!!.notificationAudioManager
        fun getOkHttpClientLazy() = graph!!.okHttpClientLazy
        fun getProductManager() = graph!!.productManager
        fun getRamStatsManager() = graph!!.ramStatsManager
        fun getRemoteConfig() = graph!!.remoteConfig
        fun getScreenManager() = graph!!.screenManager
        fun getSplitInstallManager() = graph!!.splitInstallManager
        fun getThemeManager() = graph!!.themeManager
        fun getToastManager() = graph!!.toastManager
        fun getUpdateManager() = graph!!.updateManager
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
                    graph!!.fileMediaScanner
                )
            }
        }

        private fun createFileOnlineApiNetwork() = object : FileOnlineApiNetwork {

            override fun getSync(
                url: String,
                headers: Map<String, String>
            ) = getNetworkManager().getSync(
                url,
                headers
            )

            override fun postSync(
                url: String,
                headers: Map<String, String>,
                jsonObject: JSONObject
            ) = getNetworkManager().postSync(
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
            ) = getNetworkManager().postDownloadSync(
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
            ) = getNetworkManager().postUploadSync(
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
            ) = getNetworkManager().deleteSync(
                url,
                headers,
                jsonObject
            )
        }
    }
}
