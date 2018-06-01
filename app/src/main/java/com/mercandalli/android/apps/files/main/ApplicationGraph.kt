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
import com.mercandalli.android.apps.files.version.VersionManager
import com.mercandalli.android.apps.files.version.VersionModule
import com.mercandalli.android.sdk.files.api.FileModule
import com.mercandalli.android.sdk.files.api.PermissionRequestAddOn
import com.mercandalli.sdk.files.api.*

class ApplicationGraph(
        private val context: Context
) {

    private lateinit var audioManager: AudioManager
    private lateinit var audioQueueManager: AudioQueueManager
    private lateinit var audioModule: AudioModule
    private lateinit var fileManager: FileManager
    private lateinit var fileOpenManager: FileOpenManager
    private lateinit var fileDeleteManager: FileDeleteManager
    private lateinit var fileCopyCutManager: FileCopyCutManager
    private lateinit var fileCreatorManager: FileCreatorManager
    private lateinit var fileRenameManager: FileRenameManager
    private lateinit var fileSortManager: FileSortManager
    private lateinit var fileModule: FileModule
    private lateinit var noteManager: NoteManager
    private lateinit var notificationAudioManager: NotificationAudioManager
    private lateinit var settingsManager: SettingsManager
    private lateinit var versionManager: VersionManager

    private fun getAudioManagerInternal(): AudioManager {
        if (!::audioManager.isInitialized) {
            if (!::audioModule.isInitialized) {
                val fileSortManager = getFileSortManager()
                audioModule = AudioModule(
                        fileSortManager
                )
            }
            audioManager = audioModule.provideAudioManager()
        }
        return audioManager
    }

    private fun getAudioQueueManagerInternal(): AudioQueueManager {
        if (!::audioQueueManager.isInitialized) {
            if (!::audioModule.isInitialized) {
                val fileSortManager = getFileSortManager()
                audioModule = AudioModule(
                        fileSortManager
                )
            }
            val audioManager = getAudioManagerInternal()
            audioQueueManager = audioModule.provideAudioQueueManager(
                    audioManager
            )
        }
        return audioQueueManager
    }

    private fun getFileManagerInternal(): FileManager {
        if (!::fileManager.isInitialized) {
            if (!::fileModule.isInitialized) {
                fileModule = createFileModule()
            }
            fileManager = fileModule.provideFileManager()
        }
        return fileManager
    }

    private fun getFileOpenManagerInternal(): FileOpenManager {
        if (!::fileOpenManager.isInitialized) {
            if (!::fileModule.isInitialized) {
                fileModule = createFileModule()
            }
            fileOpenManager = fileModule.provideFileOpenManager()
        }
        return fileOpenManager
    }

    private fun getFileDeleteManagerInternal(): FileDeleteManager {
        if (!::fileDeleteManager.isInitialized) {
            if (!::fileModule.isInitialized) {
                fileModule = createFileModule()
            }
            fileDeleteManager = fileModule.provideFileDeleteManager()
        }
        return fileDeleteManager
    }

    private fun getFileCopyCutManagerInternal(): FileCopyCutManager {
        if (!::fileCopyCutManager.isInitialized) {
            if (!::fileModule.isInitialized) {
                fileModule = createFileModule()
            }
            fileCopyCutManager = fileModule.provideFileCopyCutManager()
        }
        return fileCopyCutManager
    }

    private fun getFileCreatorManagerInternal(): FileCreatorManager {
        if (!::fileCreatorManager.isInitialized) {
            if (!::fileModule.isInitialized) {
                fileModule = createFileModule()
            }
            fileCreatorManager = fileModule.provideFileCreatorManager()
        }
        return fileCreatorManager
    }

    private fun getFileRenameManagerInternal(): FileRenameManager {
        if (!::fileRenameManager.isInitialized) {
            if (!::fileModule.isInitialized) {
                fileModule = createFileModule()
            }
            fileRenameManager = fileModule.provideFileRenameManager()
        }
        return fileRenameManager
    }

    private fun getFileSortManagerInternal(): FileSortManager {
        if (!::fileSortManager.isInitialized) {
            if (!::fileModule.isInitialized) {
                fileModule = createFileModule()
            }
            fileSortManager = fileModule.provideFileSortManager()
        }
        return fileSortManager
    }

    private fun getNoteManagerInternal(): NoteManager {
        if (!::noteManager.isInitialized) {
            noteManager = NoteModule(context).provideNoteManager()
        }
        return noteManager
    }

    private fun getNotificationAudioManagerInternal(): NotificationAudioManager {
        if (!::notificationAudioManager.isInitialized) {
            val audioManager = getAudioManager()
            val notificationModule = NotificationModule(
                    context,
                    audioManager
            )
            notificationAudioManager = notificationModule.provideNotificationAudioManager()
        }
        return notificationAudioManager
    }

    private fun getSettingsManagerInternal(): SettingsManager {
        if (!::settingsManager.isInitialized) {
            settingsManager = SettingsModule().provideSettingsManager()
        }
        return settingsManager
    }

    private fun getVersionManagerInternal(): VersionManager {
        if (!::versionManager.isInitialized) {
            versionManager = VersionModule(context).provideVersionManager()
        }
        return versionManager
    }

    private fun createFileModule(): FileModule {
        val permissionRequestAddOn: PermissionRequestAddOn = object : PermissionRequestAddOn {
            override fun requestStoragePermission() {
                PermissionActivity.start(context)
            }
        }
        return FileModule(context, permissionRequestAddOn)
    }

    companion object {

        @JvmStatic
        @SuppressLint("StaticFieldLeak")
        private var graph: ApplicationGraph? = null

        @JvmStatic
        fun getAudioManager(): AudioManager {
            return graph!!.getAudioManagerInternal()
        }

        @JvmStatic
        fun getAudioQueueManager(): AudioQueueManager {
            return graph!!.getAudioQueueManagerInternal()
        }

        @JvmStatic
        fun getFileManager(): FileManager {
            return graph!!.getFileManagerInternal()
        }

        @JvmStatic
        fun getFileOpenManager(): FileOpenManager {
            return graph!!.getFileOpenManagerInternal()
        }

        @JvmStatic
        fun getFileDeleteManager(): FileDeleteManager {
            return graph!!.getFileDeleteManagerInternal()
        }

        @JvmStatic
        fun getFileCopyCutManager(): FileCopyCutManager {
            return graph!!.getFileCopyCutManagerInternal()
        }

        @JvmStatic
        fun getFileCreatorManager(): FileCreatorManager {
            return graph!!.getFileCreatorManagerInternal()
        }

        @JvmStatic
        fun getFileRenameManager(): FileRenameManager {
            return graph!!.getFileRenameManagerInternal()
        }

        @JvmStatic
        fun getFileSortManager(): FileSortManager {
            return graph!!.getFileSortManagerInternal()
        }

        @JvmStatic
        fun getNoteManager(): NoteManager {
            return graph!!.getNoteManagerInternal()
        }

        @JvmStatic
        fun getNotificationAudioManager(): NotificationAudioManager {
            return graph!!.getNotificationAudioManagerInternal()
        }

        @JvmStatic
        fun getSettingsManager(): SettingsManager {
            return graph!!.getSettingsManagerInternal()
        }

        @JvmStatic
        fun getVersionManager(): VersionManager {
            return graph!!.getVersionManagerInternal()
        }

        @JvmStatic
        fun init(context: Context) {
            if (graph == null) {
                graph = ApplicationGraph(context.applicationContext)
            }
        }
    }
}