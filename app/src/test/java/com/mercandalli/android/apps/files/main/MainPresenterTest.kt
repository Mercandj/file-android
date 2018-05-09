package com.mercandalli.android.apps.files.main

import com.mercandalli.sdk.files.api.FileCreatorManager
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.reset
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class MainPresenterTest {

    @Mock
    private var screen: MainActivityContract.Screen? = null
    @Mock
    private var fileCreatorManager: FileCreatorManager? = null

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun onFileSectionClickedShowsFileView() {
        // Given
        val presenter = createInstanceToTest()
        reset(screen)

        // When
        presenter.onFileSectionClicked()

        // Then
        verify(screen)!!.showFileView()
    }

    @Test
    fun onNoteSectionClickedShowsNoteView() {
        // Given
        val presenter = createInstanceToTest()

        // When
        presenter.onNoteSectionClicked()

        // Then
        verify(screen)!!.showNoteView()
    }

    @Test
    fun onSettingsSectionClickedShowsSettingsView() {
        // Given
        val presenter = createInstanceToTest()

        // When
        presenter.onSettingsSectionClicked()

        // Then
        verify(screen)!!.showSettingsView()
    }

    private fun createInstanceToTest(): MainActivityPresenter {
        return MainActivityPresenter(
                screen!!,
                fileCreatorManager!!
        )
    }
}