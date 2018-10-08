package com.mercandalli.server.files.file_repository

class FileRepositoryModule(
        private val rootPath: String
) {

    fun createFileRepository(): FileRepository {
        val repositoryFolderFile = java.io.File(rootPath, "file-repository")
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