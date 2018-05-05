package com.mercandalli.sdk.files.api

/**
 * Memo: goal is to keep file class as light as possible. All extract data like size, ext...
 * should not be part of this model. See [FileMetadataManager].
 * <br />
 * Why? - For example we want to load a big list of files, we want to have the possibility
 * to expose a "cold" load of this list as fast as possible with the minimum data possible.
 * Sugar data like extension, name, size should be loaded afterward from the path.
 * <br />
 * So fields of this class should stay the minimum definition of a file. (for me, path and folder).
 * Only defined data required to navigate across files.
 */
data class File(

        /**
         * The file path (dom/tree representation)
         */
        val path: String,

        /**
         * The parent file path or null if no parent (root for example).
         */
        val parentPath: String?,

        /**
         * Is this file a file container (named folder/directory)
         */
        val directory: Boolean,

        /**
         * File name. If it's not a directory, contains extension.
         */
        val name: String
)