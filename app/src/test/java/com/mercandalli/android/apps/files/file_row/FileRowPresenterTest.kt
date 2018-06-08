package com.mercandalli.android.apps.files.file_row

import com.mercandalli.android.apps.files.audio.AudioManager
import com.mercandalli.android.apps.files.file.FileTest
import com.mercandalli.android.apps.files.theme.ThemeManager
import com.mercandalli.sdk.files.api.FileCopyCutManager
import com.mercandalli.sdk.files.api.FileDeleteManager
import com.mercandalli.sdk.files.api.FileRenameManager
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class FileRowPresenterTest {

    @Mock
    private var screen: FileRowContract.Screen? = null
    @Mock
    private var fileDeleteManager: FileDeleteManager? = null
    @Mock
    private var fileCopyCutManager: FileCopyCutManager? = null
    @Mock
    private var fileRenameManager: FileRenameManager? = null
    @Mock
    private var audioManager: AudioManager? = null
    @Mock
    private var themeManager: ThemeManager? = null
    private val drawableRightIconDirectoryDrawableRes: Int = 42
    private val drawableRightIconSoundDrawableRes: Int = 44
    private val selectedTextColorRes: Int = 45

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

    private fun createInstanceToTest(): FileRowPresenter {
        return FileRowPresenter(
                screen!!,
                fileDeleteManager!!,
                fileCopyCutManager!!,
                fileRenameManager!!,
                audioManager!!,
                themeManager!!,
                drawableRightIconDirectoryDrawableRes,
                drawableRightIconSoundDrawableRes,
                selectedTextColorRes
        )
    }
}