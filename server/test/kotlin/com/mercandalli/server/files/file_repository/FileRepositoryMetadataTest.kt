package com.mercandalli.server.files.file_repository

import com.mercandalli.sdk.files.api.File
import com.mercandalli.sdk.files.api.FileTest
import org.junit.Assert
import org.junit.Test

class FileRepositoryMetadataTest {

    @Test
    fun renameFile() {
        // Given
        val files = ArrayList<File>()
        files.add(
                File.create(
                        "id-file",
                        "/index.html",
                        "/",
                        false,
                        "index.html",
                        0L,
                        0L
                )
        )
        val filesMap = HashMap<String, File>()
        for (file in files) {
            filesMap[file.path] = file
        }
        val fileRepositoryMetadata = FileRepositoryMetadata(filesMap)

        // When
        val fileRepositoryMetadataRenamed = FileRepositoryMetadata.rename(
                fileRepositoryMetadata,
                "/index.html",
                "my-web super website.html"
        )

        // Then
        val renamedFiles = fileRepositoryMetadataRenamed.getFiles()
        Assert.assertEquals(1, renamedFiles.size)
        val renamedFile = renamedFiles["/my-web super website.html"]
        Assert.assertNotNull(renamedFile)
        FileTest.areEquals(
                File.create(
                        "id-file",
                        "/my-web super website.html",
                        "/",
                        false,
                        "my-web super website.html",
                        0L,
                        0L
                ),
                renamedFile!!
        )
    }

    @Test
    fun renameFolder() {
        // Given
        val files = ArrayList<File>()
        files.add(
                File.create(
                        "id-folder",
                        "/coucou",
                        "/",
                        true,
                        "coucou",
                        0L,
                        0L
                )
        )
        files.add(
                File.create(
                        "id-file",
                        "/coucou/index.html",
                        "/coucou",
                        false,
                        "index.html",
                        0L,
                        0L
                )
        )
        val filesMap = HashMap<String, File>()
        for (file in files) {
            filesMap[file.path] = file
        }
        val fileRepositoryMetadata = FileRepositoryMetadata(filesMap)

        // When
        val fileRepositoryMetadataRenamed = FileRepositoryMetadata.rename(
                fileRepositoryMetadata,
                "/coucou",
                "toto"
        )

        // Then
        val renamedFiles = fileRepositoryMetadataRenamed.getFiles()
        Assert.assertEquals(2, renamedFiles.size)
        for (renamedFile in renamedFiles.values) {
            if (renamedFile.directory) {
                FileTest.areEquals(
                        File.create(
                                "id-folder",
                                "/toto",
                                "/",
                                true,
                                "toto",
                                0L,
                                0L
                        ),
                        renamedFile
                )
            } else {
                FileTest.areEquals(
                        File.create(
                                "id-file",
                                "/toto/index.html",
                                "/toto",
                                false,
                                "index.html",
                                0L,
                                0L
                        ),
                        renamedFile
                )
            }
        }
    }

    @Test
    fun renameFileInsideFolder() {
        // Given
        val files = ArrayList<File>()
        files.add(
                File.create(
                        "id-folder",
                        "/coucou",
                        "/",
                        true,
                        "coucou",
                        0L,
                        0L
                )
        )
        files.add(
                File.create(
                        "id-file",
                        "/coucou/index.html",
                        "/coucou",
                        false,
                        "index.html",
                        0L,
                        0L
                )
        )
        val filesMap = HashMap<String, File>()
        for (file in files) {
            filesMap[file.path] = file
        }
        val fileRepositoryMetadata = FileRepositoryMetadata(filesMap)

        // When
        val fileRepositoryMetadataRenamed = FileRepositoryMetadata.rename(
                fileRepositoryMetadata,
                "/coucou/index.html",
                "indextest.html"
        )

        // Then
        val renamedFiles = fileRepositoryMetadataRenamed.getFiles()
        Assert.assertEquals(2, renamedFiles.size)
        for (renamedFile in renamedFiles.values) {
            if (renamedFile.directory) {
                FileTest.areEquals(
                        File.create(
                                "id-folder",
                                "/coucou",
                                "/",
                                true,
                                "coucou",
                                0L,
                                0L
                        ),
                        renamedFile
                )
            } else {
                FileTest.areEquals(
                        File.create(
                                "id-file",
                                "/coucou/indextest.html",
                                "/coucou",
                                false,
                                "indextest.html",
                                0L,
                                0L
                        ),
                        renamedFile
                )
            }
        }
    }
}