@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.settings_theme

import com.mercandalli.android.apps.files.theme.LightTheme
import com.mercandalli.android.apps.files.theme.ThemeManager
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class SettingsThemePresenterTest {

    @Mock
    private lateinit var screen: SettingsThemeContract.Screen
    @Mock
    private lateinit var themeManager: ThemeManager
    private val theme = LightTheme()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun onAttachedSetTextPrimaryColorRes() {
        // Given
        val userAction = createInstanceToTest()

        // When
        userAction.onAttached()

        // Then
        screen.setTextPrimaryColorRes(theme.textPrimaryColorRes)
    }

    @Test
    fun onAttachedSetDarkThemeCheckBoxTrue() {
        // Given
        val isDarkTheme = true
        Mockito.`when`(themeManager.isDarkEnable()).thenReturn(isDarkTheme)
        val userAction = createInstanceToTest()

        // When
        userAction.onAttached()

        // Then
        screen.setDarkThemeCheckBox(isDarkTheme)
    }

    @Test
    fun onAttachedSetDarkThemeCheckBoxFalse() {
        // Given
        val isDarkTheme = false
        Mockito.`when`(themeManager.isDarkEnable()).thenReturn(isDarkTheme)
        val userAction = createInstanceToTest()

        // When
        userAction.onAttached()

        // Then
        screen.setDarkThemeCheckBox(isDarkTheme)
    }

    private fun createInstanceToTest(): SettingsThemeContract.UserAction {
        Mockito.`when`(themeManager.getTheme()).thenReturn(theme)
        return SettingsThemePresenter(
                screen,
                themeManager
        )
    }
}
