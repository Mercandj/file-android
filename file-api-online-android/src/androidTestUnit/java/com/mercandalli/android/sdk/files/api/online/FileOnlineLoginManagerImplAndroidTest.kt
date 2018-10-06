package com.mercandalli.android.sdk.files.api.online

import org.junit.Assert
import org.junit.Test

class FileOnlineLoginManagerImplAndroidTest {

    @Test
    fun getToken() {
        // Given
        val fileOnlineLoginManager = FileOnlineLoginManagerImpl()
        val login = "test"
        val password = "test"
        fileOnlineLoginManager.set(login, password)

        // When
        val token = fileOnlineLoginManager.createToken()

        // Then
        Assert.assertNotNull(token)
        Assert.assertEquals("", token)
    }

    companion object {

        fun createFake(): FileOnlineLoginManager {
            return FileOnlineLoginManagerImpl()
        }
    }
}