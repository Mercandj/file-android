package com.mercandalli.android.apps.files.note

import com.mercandalli.android.apps.files.theme.ThemeManager
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class NotePresenterTest {

    @Mock
    private val screen: NoteContract.Screen? = null
    @Mock
    private val noteManager: NoteManager? = null
    @Mock
    private val themeManager: ThemeManager? = null

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun onDeleteClickedShowDeleteConfirmation() {
        // Given
        val presenter = createInstanceToTest()

        // When
        presenter.onDeleteClicked()

        // Then
        verify(screen)!!.showDeleteConfirmation()
        verify(noteManager, never())!!.delete()
    }

    private fun createInstanceToTest(): NotePresenter {
        return NotePresenter(
                screen!!,
                noteManager!!,
                themeManager!!
        )
    }
}