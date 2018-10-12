package com.mercandalli.server.files.file_repository

import com.mercandalli.server.files.main.ApplicationGraph

class FileRepositoryModule(
        private val rootPath: String
) {

    fun createFileRepository(): FileRepository {
        val fileOnlineAuthentications = ApplicationGraph.getFileOnlineAuthentications()
        val login = if (fileOnlineAuthentications.size == 1) {
            fileOnlineAuthentications[0].login
        } else {
            "default"
        }
        val folderName = login.toLowerCase()
        val repositoryFolderFile = java.io.File(rootPath, "file-repository/$folderName")
        if (!repositoryFolderFile.exists()) {
            repositoryFolderFile.mkdirs()
        }
        val repositoryMetadataJsonFile = java.io.File(repositoryFolderFile, "file-repository-metadata.json")
        if (!repositoryMetadataJsonFile.exists()) {
            repositoryMetadataJsonFile.createNewFile()
            val json = FileRepositoryMetadata.createEmptyJson()
            repositoryMetadataJsonFile.writeText(json)
        }
        val folderContainer = java.io.File(repositoryFolderFile, "file-repository-container")
        if (!folderContainer.exists()) {
            folderContainer.mkdirs()
        }
        return FileRepositoryImpl(
                repositoryMetadataJsonFile,
                folderContainer
        )
    }
}