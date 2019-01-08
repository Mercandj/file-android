@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.settings_storage

import com.mercandalli.android.apps.files.file_storage_stats.FileStorageStatsManager
import com.mercandalli.android.apps.files.screen.ScreenManager
import com.mercandalli.android.apps.files.theme.ThemeManager
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class SettingsStoragePresenterTest {

    @Mock
    private lateinit var screen: SettingsStorageContract.Screen
    @Mock
    private lateinit var themeManager: ThemeManager
    @Mock
    private lateinit var fileStorageStatsManager: FileStorageStatsManager
    @Mock
    private lateinit var screenManager: ScreenManager

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun onStorageLocalRowClicked() {
        // Given
        val presenter = createInstanceToTest()

        // When
        presenter.onStorageLocalRowClicked()

        // Then
        Mockito.verify(screenManager).startSystemSettingsStorage()
    }

    private fun createInstanceToTest(): SettingsStorageContract.UserAction {
        return SettingsStoragePresenter(
            screen,
            themeManager,
            fileStorageStatsManager,
            screenManager
        )
    }
}
