package com.mercandalli.android.apps.files.file_provider

import com.mercandalli.sdk.files.api.File

class FileTest {

    companion object {

        @JvmStatic
        fun createFakeFile(): File {
            return File.create(
                "id",
                "path",
                "parentPath",
                false,
                "name",
                42,
                4242
            )
        }
    }
}
