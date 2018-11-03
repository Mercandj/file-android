package com.mercandalli.android.apps.files.version

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class VersionManagerImplTest {

    @Mock
    private val addOn: VersionManagerImpl.AddOn? = null

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun onPermissionAllowClickedRequestsStoragePermission() {
        // Given
        val versionName = "1.00.42"
        Mockito.`when`(addOn!!.getVersionName()).thenReturn(versionName)
        val manager = createInstanceToTest()

        // When
        val versionNameFromGetter = manager.getVersionName()

        // Then
        Assert.assertEquals(versionName, versionNameFromGetter)
    }

    private fun createInstanceToTest(): VersionManagerImpl {
        return VersionManagerImpl(
            addOn!!
        )
    }
}
