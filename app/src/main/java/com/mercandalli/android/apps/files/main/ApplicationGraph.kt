package com.mercandalli.android.apps.files.main

import android.annotation.SuppressLint
import android.content.Context
import com.mercandalli.android.apps.files.audio.AudioManager
import com.mercandalli.android.apps.files.audio.AudioModule
import com.mercandalli.android.apps.files.audio.AudioQueueManager
import com.mercandalli.android.apps.files.network.Network
import com.mercandalli.android.apps.files.network.NetworkModule
import com.mercandalli.android.apps.files.note.NoteManager
import com.mercandalli.android.apps.files.note.NoteModule
import com.mercandalli.android.apps.files.notification.NotificationAudioManager
import com.mercandalli.android.apps.files.notification.NotificationModule
import com.mercandalli.android.apps.files.permission.PermissionActivity
import com.mercandalli.android.apps.files.settings.SettingsManager
import com.mercandalli.android.apps.files.settings.SettingsModule
import com.mercandalli.android.apps.files.theme.ThemeManager
import com.mercandalli.android.apps.files.theme.ThemeModule
import com.mercandalli.android.apps.files.version.VersionManager
import com.mercandalli.android.apps.files.version.VersionModule
import com.mercandalli.android.sdk.files.api.FileModule
import com.mercandalli.android.sdk.files.api.PermissionRequestAddOn
import com.mercandalli.android.sdk.files.api.online.FileOnlineApiNetwork
import com.mercandalli.android.sdk.files.api.online.FileOnlineGraph
import com.mercandalli.sdk.files.api.*
import okhttp3.OkHttpClient

class ApplicationGraph(
        private val context: Context
) {
    private val fileModule by lazy { FileModule(context, createPermissionRequestAddOn()) }
    private val networkModule by lazy { NetworkModule() }
    private val noteModule by lazy { NoteModule(context) }

    private val audioManagerInternal by lazy { audioModuleInternal.createAudioManager() }
    private val audioQueueManagerInternal by lazy { audioModuleInternal.createAudioQueueManager(audioManagerInternal) }
    private val audioModuleInternal by lazy { AudioModule(fileSortManagerInternal) }
    private val fileManagerInternal by lazy { fileModule.createFileManager() }
    private val fileOpenManagerInternal by lazy { fileModule.createFileOpenManager() }
    private val fileDeleteManagerInternal by lazy { fileModule.createFileDeleteManager() }
    private val fileCopyCutManagerInternal by lazy { fileModule.createFileCopyCutManager() }
    private val fileCreatorManagerInternal by lazy { fileModule.createFileCreatorManager() }
    private val fileOnlineManagerInternal by lazy { FileOnlineGraph.getFileOnlineManager() }
    private val fileShareManagerInternal by lazy { fileModule.createFileShareManager() }
    private val fileRenameManagerInternal by lazy { fileModule.createFileRenameManager() }
    private val fileSortManagerInternal by lazy { fileModule.createFileSortManager() }
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

        @JvmStatic
        @SuppressLint("StaticFieldLeak")
        private var graph: ApplicationGraph? = null

        @JvmStatic
        fun getAudioManager(): AudioManager {
            return graph!!.audioManagerInternal
        }

        @JvmStatic
        fun getAudioQueueManager(): AudioQueueManager {
            return graph!!.audioQueueManagerInternal
        }

        @JvmStatic
        fun getFileManager(): FileManager {
            return graph!!.fileManagerInternal
        }

        @JvmStatic
        fun getFileOpenManager(): FileOpenManager {
            return graph!!.fileOpenManagerInternal
        }

        @JvmStatic
        fun getFileDeleteManager(): FileDeleteManager {
            return graph!!.fileDeleteManagerInternal
        }

        @JvmStatic
        fun getFileCopyCutManager(): FileCopyCutManager {
            return graph!!.fileCopyCutManagerInternal
        }

        @JvmStatic
        fun getFileCreatorManager(): FileCreatorManager {
            return graph!!.fileCreatorManagerInternal
        }

        @JvmStatic
        fun getFileShareManager(): FileShareManager {
            return graph!!.fileShareManagerInternal
        }

        @JvmStatic
        fun getFileRenameManager(): FileRenameManager {
            return graph!!.fileRenameManagerInternal
        }

        @JvmStatic
        fun getFileSortManager(): FileSortManager {
            return graph!!.fileSortManagerInternal
        }

        @JvmStatic
        fun getNetwork(): Network {
            return graph!!.network
        }

        @JvmStatic
        fun getNoteManager(): NoteManager {
            return graph!!.noteManagerInternal
        }

        @JvmStatic
        fun getNotificationAudioManager(): NotificationAudioManager {
            return graph!!.notificationAudioManagerInternal
        }

        @JvmStatic
        fun getOkHttpClientLazy(): Lazy<OkHttpClient> {
            return graph!!.okHttpClientLazy
        }

        @JvmStatic
        fun getSettingsManager(): SettingsManager {
            return graph!!.settingsManagerInternal
        }

        @JvmStatic
        fun getThemeManager(): ThemeManager {
            return graph!!.themeManagerInternal
        }

        @JvmStatic
        fun getVersionManager(): VersionManager {
            return graph!!.versionManagerInternal
        }

        @JvmStatic
        fun init(context: Context) {
            if (graph == null) {
                graph = ApplicationGraph(context.applicationContext)
                val fileOnlineApiNetwork = object : FileOnlineApiNetwork {
                    override fun requestSync(url: String, headers: Map<String, String>) = getNetwork().requestSync(url, headers)
                }
                FileOnlineGraph.init(fileOnlineApiNetwork)
            }
        }
    }
}