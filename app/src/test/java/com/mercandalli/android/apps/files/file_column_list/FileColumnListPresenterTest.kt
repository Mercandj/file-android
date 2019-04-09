@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.file_column_list

import com.mercandalli.android.apps.files.file_provider.FileTest
import com.mercandalli.android.apps.files.theme.ThemeManager
import com.mercandalli.sdk.files.api.File
import com.mercandalli.sdk.files.api.FileChildrenResult
import com.mercandalli.sdk.files.api.FileChildrenManager
import com.mercandalli.sdk.files.api.FileSortManager
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class FileColumnListPresenterTest {

    @Mock
    private val screen: FileColumnListContract.Screen? = null
    @Mock
    private val fileChildrenManager: FileChildrenManager? = null
    @Mock
    private val fileSortManager: FileSortManager? = null
    @Mock
    private val themeManager: ThemeManager? = null

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
        Mockito.`when`(fileChildrenManager!!.getFileChildren(path)).thenReturn(
            FileChildrenResult.createLoaded(path, files))
        Mockito.`when`(fileSortManager!!.sort(files)).thenReturn(files)
        val presenter = createInstanceToTest()

        // When
        presenter.onPathChanged(path)

        // Then
        Mockito.verify(screen)!!.showFiles(files)
    }

    private fun createInstanceToTest(
        currentPath: String = "/path"
    ): FileColumnListPresenter {
        return FileColumnListPresenter(
            screen!!,
            fileChildrenManager!!,
            fileSortManager!!,
            themeManager!!,
            currentPath
        )
    }
}
