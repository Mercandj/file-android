package com.mercandalli.android.apps.files.settings

import com.mercandalli.android.apps.files.version.VersionManager
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class SettingsPresenterTest {

    @Mock
    private val screen: SettingsContract.Screen? = null
    @Mock
    private val versionManager: VersionManager? = null

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
        Mockito.verify(screen)!!.openUrl(SettingsPresenter.PLAY_STORE_URL_FILESPACE)
    }

    @Test
    fun onTeamAppsClickedOpenTeamMercanUrl() {
        // Given
        val presenter = createInstanceToTest()

        // When
        presenter.onTeamAppsClicked()

        // Then
        Mockito.verify(screen)!!.openUrl(SettingsPresenter.PLAY_STORE_URL_TEAM_MERCAN)
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

    private fun createInstanceToTest(): SettingsPresenter {
        return SettingsPresenter(
                screen!!,
                versionManager!!
        )
    }
}