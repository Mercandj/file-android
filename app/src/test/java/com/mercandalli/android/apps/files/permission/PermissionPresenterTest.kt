package com.mercandalli.android.apps.files.permission

import com.mercandalli.android.sdk.files.api.FileScopedStorageManager
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class PermissionPresenterTest {

    @Mock
    private lateinit var screen: PermissionContract.Screen
    @Mock
    private lateinit var fileScopedStorageManager: FileScopedStorageManager

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
        verify(screen).requestStoragePermission()
    }

    private fun createInstanceToTest(): PermissionPresenter {
        return PermissionPresenter(
            screen,
            fileScopedStorageManager
        )
    }
}
