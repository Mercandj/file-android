package com.mercandalli.android.apps.files.file_detail

import com.mercandalli.android.apps.files.audio.AudioManager
import com.mercandalli.android.apps.files.audio.AudioQueueManager
import com.mercandalli.android.apps.files.file.FileTest
import com.mercandalli.sdk.files.api.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class FileDetailPresenterTest {

    @Mock
    private val screen: FileDetailContract.Screen? = null
    @Mock
    private val audioManager: AudioManager? = null
    @Mock
    private val audioQueueManager: AudioQueueManager? = null
    @Mock
    private val fileOpenManager: FileOpenManager? = null
    @Mock
    private val fileDeleteManager: FileDeleteManager? = null
    @Mock
    private val fileRenameManager: FileRenameManager? = null
    @Mock
    private val fileShareManager: FileShareManager? = null
    @Mock
    private val fileCopyCutManager: FileCopyCutManager? = null
    private val playStringRes: Int = 42
    private val pauseStringRes: Int = 43

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

    private fun createInstanceToTest(): FileDetailPresenter {
        return FileDetailPresenter(
                screen!!,
                audioManager!!,
                audioQueueManager!!,
                fileOpenManager!!,
                fileDeleteManager!!,
                fileCopyCutManager!!,
                fileRenameManager!!,
                fileShareManager!!,
                playStringRes,
                pauseStringRes
        )
    }
}