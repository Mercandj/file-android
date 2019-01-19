@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.file_details

import com.mercandalli.sdk.files.api.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class FileDetailsPresenterTest {

    private val path = "/storage/download"

    @Mock
    private lateinit var screen: FileDetailsContract.Screen
    @Mock
    private lateinit var fileManager: FileManager
    @Mock
    private lateinit var fileChildrenManager: FileChildrenManager
    @Mock
    private lateinit var fileShareManager: FileShareManager
    @Mock
    private lateinit var fileSizeManager: FileSizeManager

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun onCreateSetPathText() {
        // Given
        Mockito.`when`(fileManager.getFile(path)).thenReturn(FileResult.createLoading(path))
        Mockito.`when`(fileSizeManager.getSize(path)).thenReturn(FileSizeResult.createLoading(path))
        val presenter = createInstanceToTest()

        // When
        presenter.onCreate(path)

        // Then
        Mockito.verify(screen).setPathText(path)
    }

    private fun createInstanceToTest(): FileDetailsPresenter {
        return FileDetailsPresenter(
            screen,
            fileManager,
            fileChildrenManager,
            fileShareManager,
            fileSizeManager
        )
    }
}
