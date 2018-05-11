package com.mercandalli.android.apps.files.file

import com.mercandalli.sdk.files.api.File

class FileTest {

    companion object {

        @JvmStatic
        fun createFakeFile(): File {
            return File(
                    "id",
                    "path",
                    "parentPath",
                    false,
                    "name",
                    42
            )
        }
    }
}