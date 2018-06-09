package com.mercandalli.android.apps.files.file_column_detail

import com.mercandalli.android.apps.files.audio.AudioManager
import com.mercandalli.android.apps.files.audio.AudioQueueManager
import com.mercandalli.android.apps.files.file.FileTest
import com.mercandalli.sdk.files.api.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class FileColumnDetailPresenterTest {

    @Mock
    private val screen: FileColumnDetailContract.Screen? = null
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
                screen!!,
                audioManager!!,
                audioQueueManager!!,
                fileOpenManager!!,
                fileDeleteManager!!,
                fileCopyCutManager!!,
                fileRenameManager!!,
                fileShareManager!!,
                playStringRes,
                pauseStringRes,
                deleteFailedTextRes
        )
    }
}