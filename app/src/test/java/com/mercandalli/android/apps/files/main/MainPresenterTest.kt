package com.mercandalli.android.apps.files.main

import com.mercandalli.android.apps.files.theme.Theme
import com.mercandalli.android.apps.files.theme.ThemeManager
import com.mercandalli.sdk.files.api.FileCopyCutManager
import com.mercandalli.sdk.files.api.FileCreatorManager
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class MainPresenterTest {

    @Mock
    private var screen: MainActivityContract.Screen? = null
    @Mock
    private var fileCreatorManager: FileCreatorManager? = null
    @Mock
    private var fileOnlineCreatorManager: FileCreatorManager? = null
    @Mock
    private var fileCopyCutManager: FileCopyCutManager? = null
    @Mock
    private var fileOnlineCopyCutManager: FileCopyCutManager? = null
    @Mock
    private var themeManager: ThemeManager? = null
    @Mock
    private var theme: Theme? = null
    @Mock
    private var mainActivityFileUiStorage: MainActivityFileUiStorage? = null
    private val rootPathLocal = "/0/"
    private val rootPathOnline = "/"

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        `when`(themeManager!!.getTheme()).thenReturn(theme)
    }

    @Test
    fun onFileSectionClickedShowsFileView() {
        // Given
        `when`(mainActivityFileUiStorage!!.getCurrentFileUi()).thenReturn(
                MainActivityFileUiStorage.SECTION_FILE_COLUMN)
        val presenter = createInstanceToTest()
        reset(screen)

        // When
        presenter.onFileSectionClicked()

        // Then
        verify(screen)!!.showFileColumnView()
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
                fileCreatorManager!!,
                fileOnlineCreatorManager!!,
                fileCopyCutManager!!,
                fileOnlineCopyCutManager!!,
                themeManager!!,
                mainActivityFileUiStorage!!,
                rootPathLocal,
                rootPathOnline
        )
    }
}