package com.mercandalli.android.apps.files.main

import android.annotation.SuppressLint
import android.content.Context
import com.mercandalli.android.apps.files.audio.AudioManager
import com.mercandalli.android.apps.files.audio.AudioModule
import com.mercandalli.android.apps.files.audio.AudioQueueManager
import com.mercandalli.android.apps.files.audio.AudioQueueManagerImpl
import com.mercandalli.android.apps.files.note.NoteManager
import com.mercandalli.android.apps.files.note.NoteModule
import com.mercandalli.android.apps.files.notification.NotificationAudioManager
import com.mercandalli.android.apps.files.notification.NotificationAudioManagerImpl
import com.mercandalli.android.apps.files.notification.NotificationModule
import com.mercandalli.android.apps.files.permission.PermissionActivity
import com.mercandalli.android.apps.files.settings.SettingsManager
import com.mercandalli.android.apps.files.settings.SettingsManagerImpl
import com.mercandalli.android.apps.files.settings.SettingsModule
import com.mercandalli.android.apps.files.theme.ThemeManager
import com.mercandalli.android.apps.files.theme.ThemeModule
import com.mercandalli.android.apps.files.version.VersionManager
import com.mercandalli.android.apps.files.version.VersionModule
import com.mercandalli.android.sdk.files.api.FileModule
import com.mercandalli.android.sdk.files.api.PermissionRequestAddOn
import com.mercandalli.sdk.files.api.*

class ApplicationGraph(
        private val context: Context
) {

    private val audioManagerInternal: AudioManager by lazy {
        audioModuleInternal.provideAudioManager()
    }

    private val audioQueueManagerInternal: AudioQueueManager by lazy {
        audioModuleInternal.provideAudioQueueManager(
                audioManagerInternal
        )
    }

    private val audioModuleInternal: AudioModule by lazy {
        val fileSortManager = getFileSortManager()
        AudioModule(
                fileSortManager
        )
    }

    private val fileManagerInternal: FileManager by lazy {
        fileModuleInternal.provideFileManager()
    }

    private val fileOpenManagerInternal: FileOpenManager by lazy {
        fileModuleInternal.provideFileOpenManager()
    }

    private val fileDeleteManagerInternal: FileDeleteManager by lazy {
        fileModuleInternal.provideFileDeleteManager()
    }

    private val fileCopyCutManagerInternal: FileCopyCutManager by lazy {
        fileModuleInternal.provideFileCopyCutManager()
    }

    private val fileCreatorManagerInternal: FileCreatorManager by lazy {
        fileModuleInternal.provideFileCreatorManager()
    }

    private val fileShareManagerInternal: FileShareManager by lazy {
        fileModuleInternal.provideFileShareManager()
    }

    private val fileRenameManagerInternal: FileRenameManager by lazy {
        fileModuleInternal.provideFileRenameManager()
    }

    private val fileSortManagerInternal: FileSortManager by lazy {
        fileModuleInternal.provideFileSortManager()
    }

    private val fileModuleInternal: FileModule by lazy {
        val permissionRequestAddOn: PermissionRequestAddOn = object : PermissionRequestAddOn {
            override fun requestStoragePermission() {
                PermissionActivity.start(context)
            }
        }
        FileModule(context, permissionRequestAddOn)
    }

    private val noteManagerInternal: NoteManager by lazy {
        NoteModule(context).provideNoteManager()
    }

    private val notificationAudioManagerInternal: NotificationAudioManager by lazy {
        val audioManager = getAudioManager()
        val notificationModule = NotificationModule(
                context,
                audioManager
        )
        notificationModule.provideNotificationAudioManager()

    }

    private val settingsManagerInternal: SettingsManager by lazy {
        SettingsModule().provideSettingsManager()
    }

    private val themeManagerInternal: ThemeManager by lazy {
        ThemeModule().provideThemeManager(context)
    }

    private val versionManagerInternal: VersionManager by lazy {
        VersionModule(context).provideVersionManager()
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
        fun getNoteManager(): NoteManager {
            return graph!!.noteManagerInternal
        }

        @JvmStatic
        fun getNotificationAudioManager(): NotificationAudioManager {
            return graph!!.notificationAudioManagerInternal
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
            }
        }
    }
}