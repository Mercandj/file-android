package com.mercandalli.android.sdk.files.api.internal.file_children

import com.mercandalli.sdk.files.api.FileChildrenResult

internal interface FileChildrenResultLoader {

    fun loadFileChildrenSync(
        parentPath: String
    ): FileChildrenResult
}
