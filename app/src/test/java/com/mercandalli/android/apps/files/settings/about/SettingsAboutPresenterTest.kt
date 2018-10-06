package com.mercandalli.android.apps.files.settings.about

import com.mercandalli.android.apps.files.dialog.DialogManager
import com.mercandalli.android.apps.files.settings.SettingsManager
import com.mercandalli.android.apps.files.theme.ThemeManager
import com.mercandalli.android.apps.files.version.VersionManager
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class SettingsAboutPresenterTest {

    @Mock
    private val screen: SettingsAboutContract.Screen? = null
    @Mock
    private val versionManager: VersionManager? = null
    @Mock
    private val themeManager: ThemeManager? = null
    @Mock
    private val dialogManager: DialogManager? = null
    @Mock
    private val settingsManager: SettingsManager? = null

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun onRateClickedOpenFileSpaceUrl() {
        // Given
        val presenter = createInstanceToTest()

        // When
        presenter.onRateClicked()

        // Then
        Mockito.verify(screen)!!.openUrl(SettingsAboutPresenter.PLAY_STORE_URL_FILESPACE)
    }

    @Test
    fun onTeamAppsClickedOpenTeamMercanUrl() {
        // Given
        val presenter = createInstanceToTest()

        // When
        presenter.onTeamAppsClicked()

        // Then
        Mockito.verify(screen)!!.openUrl(SettingsAboutPresenter.PLAY_STORE_URL_TEAM_MERCAN)
    }

    @Test
    fun createSettingsPresenterShowVersionName() {
        // Given
        val versionName = "1.00.42"
        Mockito.`when`(versionManager!!.getVersionName()).thenReturn(versionName)

        // When
        createInstanceToTest()

        // Then
        Mockito.verify(screen)!!.showVersionName(versionName)
    }

    private fun createInstanceToTest(): SettingsAboutPresenter {
        return SettingsAboutPresenter(
                screen!!,
                versionManager!!,
                themeManager!!,
                dialogManager!!,
                settingsManager!!
        )
    }
}