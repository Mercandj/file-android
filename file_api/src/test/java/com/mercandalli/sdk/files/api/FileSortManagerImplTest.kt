package com.mercandalli.sdk.files.api

import org.junit.Assert
import org.junit.Test

class FileSortManagerImplTest {

    @Test
    fun sort() {
        // Given
        val file1 = FileTest.createFakeByName("Abc")
        val file2 = FileTest.createFakeByName("Bcd")
        val file3 = FileTest.createFakeByName("a")
        val files = listOf(
            file1,
            file2,
            file3
        )
        val sortedFilesReference = arrayOf(
            file3,
            file1,
            file2
        )
        val fileSortManager = FileSortManagerImpl()

        // When
        val sortedFiles = fileSortManager.sort(files)

        // Then
        Assert.assertEquals(sortedFilesReference.size, sortedFiles.size)
        for (i in 0 until sortedFiles.size) {
            Assert.assertEquals(sortedFilesReference[i].name, sortedFiles[i].name)
        }
    }
}
