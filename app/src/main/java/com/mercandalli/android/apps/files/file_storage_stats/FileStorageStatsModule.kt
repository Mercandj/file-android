@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.file_storage_stats

class FileStorageStatsModule {

    fun createFileStorageStatsManager(): FileStorageStatsManager {
        return FileStorageStatsManagerImpl()
    }
}
