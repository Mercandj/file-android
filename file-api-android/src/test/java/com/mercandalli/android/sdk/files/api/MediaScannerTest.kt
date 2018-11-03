package com.mercandalli.android.sdk.files.api

import com.mercandalli.sdk.files.api.MediaScanner
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class MediaScannerTest {

    @Mock
    private lateinit var addOn: MediaScannerAndroid.AddOn

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun refresh() {
        // Given
        val path = "/path"
        val mediaScanner = createInstanceToTest()

        // When
        mediaScanner.refresh(path)

        // Then
        Mockito.verify(addOn).refreshSystemMediaScanDataBase(path)
    }

    private fun createInstanceToTest(): MediaScanner {
        return MediaScannerAndroid(
                addOn
        )
    }
}
