package com.mercandalli.android.apps.files.permission

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class PermissionPresenterTest {

    @Mock
    private val screen: PermissionContract.Screen? = null

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun onPermissionAllowClickedRequestsStoragePermission() {
        // Given
        val presenter = createInstanceToTest()

        // When
        presenter.onPermissionAllowClicked()

        // Then
        verify(screen)!!.requestStoragePermission()
    }

    private fun createInstanceToTest(): PermissionPresenter {
        return PermissionPresenter(
            screen!!
        )
    }
}
