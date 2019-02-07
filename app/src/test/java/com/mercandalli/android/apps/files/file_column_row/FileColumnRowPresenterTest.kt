@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.file_column_row

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.mercandalli.android.apps.files.audio.AudioManager
import com.mercandalli.android.apps.files.file.FileTest
import com.mercandalli.android.apps.files.theme.DarkTheme
import com.mercandalli.android.apps.files.theme.Theme
import com.mercandalli.android.apps.files.theme.ThemeManager
import com.mercandalli.android.apps.files.toast.ToastManager
import com.mercandalli.sdk.files.api.FileCopyCutManager
import com.mercandalli.sdk.files.api.FileDeleteManager
import com.mercandalli.sdk.files.api.FileRenameManager
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class FileColumnRowPresenterTest {

    @Mock
    private lateinit var screen: FileColumnRowContract.Screen
    @Mock
    private lateinit var fileDeleteManager: FileDeleteManager
    @Mock
    private lateinit var fileCopyCutManager: FileCopyCutManager
    @Mock
    private lateinit var fileRenameManager: FileRenameManager
    @Mock
    private lateinit var audioManager: AudioManager
    @Mock
    private lateinit var themeManager: ThemeManager
    @Mock
    private lateinit var toastManager: ToastManager
    private var theme: Theme = DarkTheme()
    @DrawableRes
    private val drawableRightIconDirectoryDrawableRes: Int = 42
    @DrawableRes
    private val drawableRightIconSoundDrawableRes: Int = 44
    @ColorRes
    private val selectedTextColorRes: Int = 45
    @ColorRes
    private val selectedBackgroundColorRes: Int = 45

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun onCopyClickedCopyFile() {
        // Given
        val presenter = createInstanceToTest()
        val file = FileTest.createFakeFile()
        presenter.onFileChanged(file, "")

        // When
        presenter.onCopyClicked()

        // Then
        verify(fileCopyCutManager)!!.copy(file.path)
    }

    @Test
    fun onCutClickedCutFile() {
        // Given
        val presenter = createInstanceToTest()
        val file = FileTest.createFakeFile()
        presenter.onFileChanged(file, "")

        // When
        presenter.onCutClicked()

        // Then
        verify(fileCopyCutManager)!!.cut(file.path)
    }

    private fun createInstanceToTest(): FileColumnRowPresenter {
        Mockito.`when`(themeManager.getTheme()).thenReturn(theme)
        return FileColumnRowPresenter(
            screen,
            audioManager,
            fileDeleteManager,
            fileCopyCutManager,
            fileRenameManager,
            themeManager,
            toastManager,
            drawableRightIconDirectoryDrawableRes,
            drawableRightIconSoundDrawableRes,
            selectedTextColorRes,
            selectedBackgroundColorRes
        )
    }
}
