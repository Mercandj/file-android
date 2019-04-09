package com.mercandalli.sdk.files.api

import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

/**
 * Memo: goal is to keep file class as light as possible.
 * <br />
 * Why? - For example we want to load a big list of files, we want to have the possibility
 * to expose a "cold" load of this list as fast as possible with the minimum data possible.
 * Sugar data like extension, name, size should be loaded afterward from the path.
 * <br />
 * So fields of this class should stay the minimum definition of a file. (for me, path and folder).
 * Only defined data required to navigate across files.
 */
data class File private constructor(

    /**
     * Unique uuid
     */
    val id: String,

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
    val name: String,

    /**
     * Returns the length of the file denoted by this abstract pathname.
     * The return value is unspecified if this pathname denotes a directory.
     */
    val length: Long,

    /**
     * Returns the time that the file denoted by this abstract pathname was
     * last modified.
     */
    val lastModified: Long
) {

    companion object {

        const val JSON_KEY_PATH = "path"
        const val JSON_KEY_NAME = "name"

        fun create(
            id: String,
            path: String,
            parentPath: String?,
            directory: Boolean,
            name: String,
            length: Long,
            lastModified: Long
        ): File {
            val cleanedPath = cleanPath(path)
            return File(
                id,
                cleanedPath,
                parentPath,
                directory,
                name,
                length,
                lastModified
            )
        }

        fun create(
            parentPath: String,
            name: String
        ): File {
            val directory = !name.contains(".")
            return create(
                UUID.randomUUID().toString(),
                "$parentPath/$name",
                "$parentPath/",
                directory,
                name,
                0,
                0
            )
        }

        fun create(javaFile: java.io.File): File {
            val id = javaFile.absolutePath
            val path = javaFile.absolutePath
            val name = javaFile.name
            val directory = javaFile.isDirectory
            val javaParentPath = javaFile.parent
            val parentPath = if (javaParentPath == "") null else javaParentPath
            val length = javaFile.length()
            val lastModified = javaFile.lastModified()
            return create(
                id,
                path,
                parentPath,
                directory,
                name,
                length,
                lastModified)
        }

        @JvmStatic
        fun fromJson(jsonObject: JSONObject): File {
            val id = jsonObject.getString("id")
            val path = jsonObject.getString(JSON_KEY_PATH)
            val parentPath = jsonObject.getString("parent_path")?.let {
                cleanPath(it)
            }
            val directory = jsonObject.getBoolean("directory")
            val name = jsonObject.getString(JSON_KEY_NAME)
            val length = jsonObject.getLong("length")
            val lastModified = jsonObject.getLong("last_modified")
            return create(
                id,
                path,
                parentPath,
                directory,
                name,
                length,
                lastModified
            )
        }

        @JvmStatic
        fun fromJson(jsonArray: JSONArray): List<File> {
            val files = ArrayList<File>()
            for (i in 0 until jsonArray.length()) {
                val fileJsonObject = jsonArray.getJSONObject(i)
                val file = fromJson(fileJsonObject)
                files.add(file)
            }
            return files
        }

        @JvmStatic
        fun toJson(file: File): JSONObject {
            val json = JSONObject()
            json.put("id", file.id)
            json.put(JSON_KEY_PATH, file.path)
            val parentPath = if (file.parentPath == null) {
                null
            } else {
                cleanPath(file.parentPath)
            }
            json.put("parent_path", parentPath)
            json.put("directory", file.directory)
            json.put(JSON_KEY_NAME, file.name)
            json.put("length", file.length)
            json.put("last_modified", file.lastModified)
            return json
        }

        @JvmStatic
        fun toJson(files: List<File>): JSONArray {
            val jsonArray = JSONArray()
            for (file in files) {
                val json = toJson(file)
                jsonArray.put(json)
            }
            return jsonArray
        }

        @JvmStatic
        fun toJson(files: MutableCollection<File>): JSONArray {
            val jsonArray = JSONArray()
            for (file in files) {
                val json = toJson(file)
                jsonArray.put(json)
            }
            return jsonArray
        }

        fun rename(file: File, name: String): File {
            val path = file.path
            val newPath = renamePathFromName(path, name)
            return File.create(
                file.id,
                newPath,
                file.parentPath,
                file.directory,
                name,
                file.length,
                file.lastModified
            )
        }

        fun renamePathFromName(path: String, name: String): String {
            val file = java.io.File(path)
            val parentFile = file.parentFile
            return if (parentFile == null) {
                path.replace(file.name, name)
            } else {
                java.io.File(parentFile.absolutePath, name).absolutePath
            }
        }

        fun setParent(file: File, pathDirectoryOutput: String): File {
            val pathDirectoryOutputCleaned = cleanPath(pathDirectoryOutput)
            val name = file.name
            return File.create(
                file.id,
                "$pathDirectoryOutputCleaned/$name",
                pathDirectoryOutputCleaned,
                file.directory,
                name,
                file.length,
                file.lastModified
            )
        }

        fun createFake(id: String) = File(
            id,
            "/Root",
            "/",
            true,
            "Root",
            0,
            4242
        )

        fun createFakeJson(id: String): String {
            val file = createFake(id)
            return toJson(file).toString()
        }

        fun cleanPath(path: String): String {
            if (path.startsWith("content://")) {
                return path
            }
            return java.io.File(path).absolutePath
        }
    }
}
