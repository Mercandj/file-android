package com.mercandalli.android.sdk.files.api.online

import com.mercandalli.sdk.files.api.MediaScanner
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class MediaScannerOnlineAndroidTest {

    @Mock
    private lateinit var listener: MediaScanner.RefreshListener

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun refreshNotifyListener() {
        // Given
        val path = "/path"
        val mediaScanner = createInstanceToTest()
        mediaScanner.addListener(listener)

        // When
        mediaScanner.refresh(path)

        // Then
        Mockito.verify(listener).onContentChanged(path)
    }

    private fun createInstanceToTest(): MediaScanner {
        return MediaScannerOnlineAndroid()
    }
}