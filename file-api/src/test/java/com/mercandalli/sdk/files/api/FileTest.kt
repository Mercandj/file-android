package com.mercandalli.sdk.files.api

import org.junit.Assert
import org.junit.Test

class FileTest {

    @Test
    fun parseSerializeFileToJson() {
        // Given
        val file = File(
                "id",
                "path",
                "parentPath",
                false,
                "name",
                42,
                4242)

        // When
        val json = File.toJson(file)
        val fileFromJson = File.fromJson(json)

        // Then
        areEquals(file, fileFromJson)
    }

    companion object {

        @JvmStatic
        fun areEquals(fileReference: File, file: File) {
            Assert.assertEquals(fileReference.id, file.id)
            Assert.assertEquals(fileReference.path, file.path)
            Assert.assertEquals(fileReference.parentPath, file.parentPath)
            Assert.assertEquals(fileReference.directory, file.directory)
            Assert.assertEquals(fileReference.name, file.name)
            Assert.assertEquals(fileReference.length, file.length)
            Assert.assertEquals(fileReference.lastModified, file.lastModified)
        }
    }
}