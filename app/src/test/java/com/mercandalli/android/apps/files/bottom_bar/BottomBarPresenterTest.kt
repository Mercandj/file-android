package com.mercandalli.android.apps.files.bottom_bar

import com.mercandalli.android.apps.files.developer.DeveloperManager
import com.mercandalli.android.apps.files.theme.ThemeManager
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class BottomBarPresenterTest {

    @Mock
    private val screen: BottomBarContract.Screen? = null
    @Mock
    private val themeManager: ThemeManager? = null
    @Mock
    private val developerManager: DeveloperManager? = null
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
                screen!!,
                themeManager!!,
                developerManager!!,
                selectedColor,
                notSelectedColor
        )
    }
}