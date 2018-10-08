package com.mercandalli.server.files.file_repository

import com.mercandalli.sdk.files.api.File
import org.json.JSONObject

data class FileRepositoryMetadata(

        val files: HashMap<String, File>
) {

    companion object {

        fun fromJson(jsonObject: JSONObject): FileRepositoryMetadata {
            val filesJsonArray = jsonObject.getJSONArray("files")
            val filesList = File.fromJson(filesJsonArray)
            val files = HashMap<String, File>()
            for (file in filesList) {
                files[file.id] = file
            }
            return FileRepositoryMetadata(
                    files
            )
        }

        fun toJson(fileRepositoryMetadata: FileRepositoryMetadata): JSONObject {
            val json = JSONObject()
            val values = fileRepositoryMetadata.files.values
            val filesJsonArray = File.toJson(values)
            json.put("files", filesJsonArray)
            return json
        }

        fun createEmptyJson(): String {
            val files = HashMap<String, File>()
            val fileRepositoryMetadata = FileRepositoryMetadata(files)
            return toJson(fileRepositoryMetadata).toString()
        }
    }
}