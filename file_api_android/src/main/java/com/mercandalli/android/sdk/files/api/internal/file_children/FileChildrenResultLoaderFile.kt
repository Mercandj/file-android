package com.mercandalli.android.sdk.files.api.internal.file_children

import com.mercandalli.sdk.files.api.File
import com.mercandalli.sdk.files.api.FileChildrenResult

internal class FileChildrenResultLoaderFile : FileChildrenResultLoader {

    override fun loadFileChildrenSync(
        parentPath: String
    ): FileChildrenResult {
        val ioFile = java.io.File(parentPath)
        if (!ioFile.isDirectory) {
            return FileChildrenResult.createErrorNotFolder(parentPath)
        }
        val ioFiles = ioFile.listFiles()
        val files = ArrayList<File>()
        if (ioFiles != null) {
            for (ioFileLoop in ioFiles) {
                val file = File.create(ioFileLoop)
                files.add(file)
            }
        }
        return FileChildrenResult.createLoaded(parentPath, files)
    }
}
