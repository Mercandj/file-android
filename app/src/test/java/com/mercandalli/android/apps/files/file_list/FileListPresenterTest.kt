package com.mercandalli.android.apps.files.file_list

import com.mercandalli.android.apps.files.file.FileTest
import com.mercandalli.sdk.files.api.File
import com.mercandalli.sdk.files.api.FileChildrenResult
import com.mercandalli.sdk.files.api.FileManager
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class FileListPresenterTest {

    @Mock
    private val screen: FileListContract.Screen? = null
    @Mock
    private val fileManager: FileManager? = null

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun onPathChangedDisplayLoadedFiles() {
        // Given
        val path = "/new-path"
        val files = ArrayList<File>()
        files.add(FileTest.createFakeFile())
        Mockito.`when`(fileManager!!.getFileChildren(path)).thenReturn(
                FileChildrenResult.createLoaded(path, files))
        val presenter = createInstanceToTest()

        // When
        presenter.onPathChanged(path)

        // Then
        Mockito.verify(screen)!!.showFiles(files)
    }

    private fun createInstanceToTest(
            currentPath: String = "/path"
    ): FileListPresenter {
        return FileListPresenter(
                screen!!,
                fileManager!!,
                currentPath
        )
    }

}