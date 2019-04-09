package com.mercandalli.android.sdk.files.api.internal.file_children

import com.mercandalli.sdk.files.api.FileChildrenResult

internal class FileChildrenResultLoaderImpl(
    private val fileChildrenResultLoaderFile: FileChildrenResultLoader,
    private val fileChildrenResultLoaderContentResolver: FileChildrenResultLoader
) : FileChildrenResultLoader {

    override fun loadFileChildrenSync(
        parentPath: String
    ): FileChildrenResult {
        return if (
            parentPath.startsWith("content://")
        ) {
            fileChildrenResultLoaderContentResolver.loadFileChildrenSync(
                parentPath
            )
        } else {
            fileChildrenResultLoaderFile.loadFileChildrenSync(
                parentPath
            )
        }
    }
}
