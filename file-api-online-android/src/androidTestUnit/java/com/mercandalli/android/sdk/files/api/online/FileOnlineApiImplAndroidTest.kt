package com.mercandalli.android.sdk.files.api.online

import org.junit.Test

class FileOnlineApiImplAndroidTest {

    @Test
    fun get() {
        // Given
        val fileOnlineApiNetwork = FileOnlineApiNetworkAndroidTest.createFileOnlineApiNetwork()
        val fileOnlineLoginManager = FileOnlineLoginManagerImplAndroidTest.createFake()
        val fileOnlineApi = FileOnlineApiImpl(
                fileOnlineApiNetwork,
                fileOnlineLoginManager
        )

        // When
        val getJson = fileOnlineApi.get()

        // Then
        //Assert.assertNotNull(getJson)
    }
}