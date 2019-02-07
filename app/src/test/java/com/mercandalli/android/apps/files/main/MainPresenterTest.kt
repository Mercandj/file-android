package com.mercandalli.android.apps.files.main

import com.mercandalli.android.apps.files.developer.DeveloperManager
import com.mercandalli.android.apps.files.remote_config.RemoteConfig
import com.mercandalli.android.apps.files.screen.ScreenManager
import com.mercandalli.android.apps.files.split_install.SplitInstallManager
import com.mercandalli.android.apps.files.theme.Theme
import com.mercandalli.android.apps.files.theme.ThemeManager
import com.mercandalli.android.apps.files.toast.ToastManager
import com.mercandalli.android.apps.files.update.UpdateManager
import com.mercandalli.sdk.files.api.FileCopyCutManager
import com.mercandalli.sdk.files.api.FileCreatorManager
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.reset
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class MainPresenterTest {

    @Mock
    private lateinit var screen: MainActivityContract.Screen
    @Mock
    private lateinit var fileCreatorManager: FileCreatorManager
    @Mock
    private lateinit var fileOnlineCreatorManager: FileCreatorManager
    @Mock
    private lateinit var fileCopyCutManager: FileCopyCutManager
    @Mock
    private lateinit var fileOnlineCopyCutManager: FileCopyCutManager
    @Mock
    private lateinit var developerManager: DeveloperManager
    @Mock
    private lateinit var mainActivityFileUiStorage: MainActivityFileUiStorage
    @Mock
    private lateinit var mainActivitySectionStorage: MainActivitySectionStorage
    @Mock
    private lateinit var remoteConfig: RemoteConfig
    @Mock
    private lateinit var screenManager: ScreenManager
    @Mock
    private lateinit var splitInstallManager: SplitInstallManager
    @Mock
    private lateinit var themeManager: ThemeManager
    @Mock
    private lateinit var toastManager: ToastManager
    @Mock
    private lateinit var updateManager: UpdateManager
    @Mock
    private lateinit var theme: Theme
    private val rootPathLocal = "/0/"
    private val rootPathOnline = "/"

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        `when`(themeManager.getTheme()).thenReturn(theme)
    }

    @Test
    fun onFileSectionClickedShowsFileView() {
        // Given
        `when`(mainActivityFileUiStorage.getCurrentFileUi()).thenReturn(
            MainActivityFileUiStorage.SECTION_FILE_COLUMN)
        val presenter = createInstanceToTest()
        reset(screen)

        // When
        presenter.onFileSectionClicked()

        // Then
        verify(screen).showFileColumnView()
    }

    @Test
    fun onNoteSectionClickedShowsNoteView() {
        // Given
        val presenter = createInstanceToTest()

        // When
        presenter.onNoteSectionClicked()

        // Then
        verify(screen).showNoteView()
    }

    @Test
    fun onSettingsSectionClickedShowsSettingsView() {
        // Given
        val presenter = createInstanceToTest()

        // When
        presenter.onSettingsSectionClicked()

        // Then
        verify(screen).showSettingsView()
    }

    private fun createInstanceToTest(): MainActivityPresenter {
        return MainActivityPresenter(
            screen,
            fileCreatorManager,
            fileOnlineCreatorManager,
            fileCopyCutManager,
            fileOnlineCopyCutManager,
            developerManager,
            mainActivityFileUiStorage,
            mainActivitySectionStorage,
            remoteConfig,
            screenManager,
            splitInstallManager,
            themeManager,
            toastManager,
            updateManager,
            rootPathLocal,
            rootPathOnline
        )
    }
}
