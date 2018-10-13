package com.mercandalli.android.apps.files.file_column_detail

import com.mercandalli.android.apps.files.audio.AudioManager
import com.mercandalli.android.apps.files.audio.AudioQueueManager
import com.mercandalli.android.apps.files.file.FileTest
import com.mercandalli.android.apps.files.theme.ThemeManager
import com.mercandalli.android.apps.files.toast.ToastManager
import com.mercandalli.sdk.files.api.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class FileColumnDetailPresenterTest {

    @Mock
    private lateinit var screen: FileColumnDetailContract.Screen
    @Mock
    private lateinit var audioManager: AudioManager
    @Mock
    private lateinit var audioQueueManager: AudioQueueManager
    @Mock
    private lateinit var fileOpenManager: FileOpenManager
    @Mock
    private lateinit var fileDeleteManager: FileDeleteManager
    @Mock
    private lateinit var fileRenameManager: FileRenameManager
    @Mock
    private lateinit var fileShareManager: FileShareManager
    @Mock
    private lateinit var fileCopyCutManager: FileCopyCutManager
    @Mock
    private lateinit var themeManager: ThemeManager
    @Mock
    private lateinit var toastManager: ToastManager
    private val playStringRes: Int = 42
    private val pauseStringRes: Int = 43
    private val deleteFailedTextRes: Int = 44

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun onFileChangedDisplayFileName() {
        // Given
        val presenter = createInstanceToTest()
        val file = FileTest.createFakeFile()

        // When
        presenter.onFileChanged(file)

        // Then
        verify(screen)!!.setTitle(file.name)
    }

    private fun createInstanceToTest(): FileColumnDetailPresenter {
        return FileColumnDetailPresenter(
                screen,
                audioManager,
                audioQueueManager,
                fileOpenManager,
                fileDeleteManager,
                fileCopyCutManager,
                fileRenameManager,
                fileShareManager,
                themeManager,
                toastManager,
                playStringRes,
                pauseStringRes,
                deleteFailedTextRes
        )
    }
}