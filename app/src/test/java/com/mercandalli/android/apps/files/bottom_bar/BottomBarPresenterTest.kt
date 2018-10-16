package com.mercandalli.android.apps.files.bottom_bar

import com.mercandalli.android.apps.files.developer.DeveloperManager
import com.mercandalli.android.apps.files.theme.ThemeManager
import com.mercandalli.sdk.files.api.online.FileOnlineLoginManager
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.never
import org.mockito.MockitoAnnotations

class BottomBarPresenterTest {

    @Mock
    private lateinit var screen: BottomBarContract.Screen
    @Mock
    private lateinit var themeManager: ThemeManager
    @Mock
    private lateinit var developerManager: DeveloperManager
    @Mock
    private lateinit var fileOnlineLoginManager: FileOnlineLoginManager
    private val selectedColor: Int = 42
    private val notSelectedColor: Int = 43

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun createPresenterDoesNotNotifyListener() {
        // When
        createInstanceToTest()

        // Then
        verify(screen, never())!!.notifyListenerFileClicked()
    }

    @Test
    fun onFileClickedNotifyListener() {
        // Given
        val presenter = createInstanceToTest()

        // When
        presenter.onFileClicked()

        // Then
        verify(screen)!!.notifyListenerFileClicked()
    }

    @Test
    fun onNoteClickedNotifyListener() {
        // Given
        val presenter = createInstanceToTest()

        // When
        presenter.onNoteClicked()

        // Then
        verify(screen)!!.notifyListenerNoteClicked()
    }

    @Test
    fun onSettingsClickedNotifyListener() {
        // Given
        val presenter = createInstanceToTest()

        // When
        presenter.onSettingsClicked()

        // Then
        verify(screen)!!.notifyListenerSettingsClicked()
    }

    private fun createInstanceToTest(): BottomBarPresenter {
        return BottomBarPresenter(
                screen,
                themeManager,
                developerManager,
                fileOnlineLoginManager,
                selectedColor,
                notSelectedColor
        )
    }
}