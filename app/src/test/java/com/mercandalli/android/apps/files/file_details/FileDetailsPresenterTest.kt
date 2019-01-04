@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.file_details

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class FileDetailsPresenterTest {

    private val path = "/storage/download"

    @Mock
    private lateinit var screen: FileDetailsContract.Screen

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun onCreateSetPathText() {
        // Given
        val presenter = createInstanceToTest()

        // When
        presenter.onCreate(path)

        // Then
        Mockito.verify(screen).setPathText(path)
    }

    private fun createInstanceToTest(): FileDetailsPresenter {
        return FileDetailsPresenter(
            screen
        )
    }
}
