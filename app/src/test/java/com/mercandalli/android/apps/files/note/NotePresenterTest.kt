package com.mercandalli.android.apps.files.note

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
                noteManager!!
        )
    }
}