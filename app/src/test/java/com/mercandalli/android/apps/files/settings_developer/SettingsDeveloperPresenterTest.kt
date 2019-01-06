@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.settings_developer

import com.mercandalli.android.apps.files.developer.DeveloperManager
import com.mercandalli.android.apps.files.dialog.DialogManager
import com.mercandalli.android.apps.files.theme.ThemeManager
import com.mercandalli.sdk.files.api.online.FileOnlineLoginManager
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class SettingsDeveloperPresenterTest {

    @Mock
    private lateinit var screen: SettingsDeveloperContract.Screen
    @Mock
    private lateinit var themeManager: ThemeManager
    @Mock
    private lateinit var developerManager: DeveloperManager
    @Mock
    private lateinit var fileOnlineLoginManager: FileOnlineLoginManager
    @Mock
    private lateinit var dialogManager: DialogManager
    @Mock
    private lateinit var addOn: SettingsDeveloperPresenter.AddOn

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun onActivationRowClicked() {
        // Given
        val presenter = createInstanceToTest()

        // When
        presenter.onActivationRowClicked()

        // Then
        Mockito.verify(developerManager).setDeveloperMode(false)
    }

    private fun createInstanceToTest(): SettingsDeveloperContract.UserAction {
        return SettingsDeveloperPresenter(
            screen,
            themeManager,
            developerManager,
            fileOnlineLoginManager,
            dialogManager,
            addOn
        )
    }
}
