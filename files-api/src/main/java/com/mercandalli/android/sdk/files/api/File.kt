package com.mercandalli.android.sdk.files.api

/**
 * Memo: goal is to keep file class as light as possible. All extract data like name, size, ext...
 * should not be part of this model.
 * <br />
 * Why? - For example we want to load a big list of files, we want to have the possibility
 * to expose a "cold" load of this list as fast as possible with the minimum data possible.
 * Sugar data like extension, name, size should be loaded afterward from the path.
 * <br />
 * So fields of this class should stay the minimum definition of a file. (for me, path and folder).
 */
data class File(
        val path: String,
        val directory: Boolean
)