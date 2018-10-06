package com.mercandalli.android.apps.files.main

import android.annotation.SuppressLint
import android.content.Context
import com.mercandalli.android.apps.files.audio.AudioModule
import com.mercandalli.android.apps.files.dialog.DialogModule
import com.mercandalli.android.apps.files.network.NetworkModule
import com.mercandalli.android.apps.files.note.NoteModule
import com.mercandalli.android.apps.files.notification.NotificationModule
import com.mercandalli.android.apps.files.permission.PermissionActivity
import com.mercandalli.android.apps.files.settings.SettingsModule
import com.mercandalli.android.apps.files.theme.ThemeModule
import com.mercandalli.android.apps.files.version.VersionModule
import com.mercandalli.android.apps.files.hash.HashModule
import com.mercandalli.android.sdk.files.api.FileModule
import com.mercandalli.android.sdk.files.api.PermissionRequestAddOn
import com.mercandalli.android.sdk.files.api.online.FileOnlineApiNetwork
import com.mercandalli.android.sdk.files.api.online.FileOnlineGraph

class ApplicationGraph(
        private val context: Context
) {
    private val fileModule by lazy { FileModule(context, createPermissionRequestAddOn()) }
    private val networkModule by lazy { NetworkModule() }
    private val noteModule by lazy { NoteModule(context) }

    private val audioManagerInternal by lazy { audioModuleInternal.createAudioManager() }
    private val audioQueueManagerInternal by lazy { audioModuleInternal.createAudioQueueManager(audioManagerInternal) }
    private val audioModuleInternal by lazy { AudioModule(fileSortManagerInternal) }
    private val dialogManagerInternal by lazy { DialogModule(context).createDialogManager() }
    private val fileManagerInternal by lazy { fileModule.createFileManager() }
    private val fileOpenManagerInternal by lazy { fileModule.createFileOpenManager() }
    private val fileDeleteManagerInternal by lazy { fileModule.createFileDeleteManager() }
    private val fileCopyCutManagerInternal by lazy { fileModule.createFileCopyCutManager() }
    private val fileCreatorManagerInternal by lazy { fileModule.createFileCreatorManager() }
    private val fileOnlineManagerInternal by lazy { FileOnlineGraph.getFileOnlineManager() }
    private val fileShareManagerInternal by lazy { fileModule.createFileShareManager() }
    private val fileRenameManagerInternal by lazy { fileModule.createFileRenameManager() }
    private val fileSortManagerInternal by lazy { fileModule.createFileSortManager() }
    private val hashManagerInternal by lazy { HashModule(context).createHashManager() }
    private val network = networkModule.createNetwork()
    private val noteManagerInternal by lazy { noteModule.createNoteManager() }
    private val notificationModuleInternal by lazy { NotificationModule(context, audioManagerInternal) }
    private val notificationAudioManagerInternal by lazy { notificationModuleInternal.createNotificationAudioManager() }
    private val okHttpClientLazy = networkModule.createOkHttpClientLazy()
    private val settingsManagerInternal by lazy { SettingsModule().createSettingsManager() }
    private val themeManagerInternal by lazy { ThemeModule().createThemeManager(context) }
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
        fun getDialogManager() = graph!!.dialogManagerInternal
        fun getFileManager() = graph!!.fileManagerInternal
        fun getFileOpenManager() = graph!!.fileOpenManagerInternal
        fun getFileDeleteManager() = graph!!.fileDeleteManagerInternal
        fun getFileCopyCutManager() = graph!!.fileCopyCutManagerInternal
        fun getFileCreatorManager() = graph!!.fileCreatorManagerInternal
        fun getFileShareManager() = graph!!.fileShareManagerInternal
        fun getFileRenameManager() = graph!!.fileRenameManagerInternal
        fun getFileSortManager() = graph!!.fileSortManagerInternal
        fun getNetwork() = graph!!.network
        fun getHashManager() = graph!!.hashManagerInternal
        fun getNoteManager() = graph!!.noteManagerInternal
        fun getNotificationAudioManager() = graph!!.notificationAudioManagerInternal
        fun getOkHttpClientLazy() = graph!!.okHttpClientLazy
        fun getSettingsManager() = graph!!.settingsManagerInternal
        fun getThemeManager() = graph!!.themeManagerInternal
        fun getVersionManager() = graph!!.versionManagerInternal

        fun init(context: Context) {
            if (graph == null) {
                graph = ApplicationGraph(context.applicationContext)
                val fileOnlineApiNetwork = createFileOnlineApiNetwork()
                FileOnlineGraph.init(fileOnlineApiNetwork)
            }
        }

        private fun createFileOnlineApiNetwork() = object : FileOnlineApiNetwork {
            override fun requestSync(url: String, headers: Map<String, String>) =
                    getNetwork().requestSync(url, headers)
        }
    }
}