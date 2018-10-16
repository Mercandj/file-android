package com.mercandalli.sdk.files.api.online.utils

import com.mercandalli.sdk.files.api.online.FileOnlineTokenCreator
import com.mercandalli.sdk.files.api.online.FileOnlineTokenCreatorImpl
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.*

class FileOnlineTokenCreatorImplTest {

    @Mock
    private lateinit var addOn: FileOnlineTokenCreatorImpl.AddOn

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun createTokenWithFakeAuthentication() {
        // Given
        val date = Date()
        date.time = 123456789
        Mockito.`when`(addOn.getCurrentDate()).thenReturn(date)
        val tokenCreator = createInstanceToTest()

        // When
        val token = tokenCreator.createToken(
                "fake_login",
                "fake_password"
        )

        // Then
        Assert.assertEquals(
                "ZmFrZV9sb2dpbjpmYWNlMTRhOWQ4YzcyN2U3ZTBkMjhmOTkxYTQ1ZWY5MTQxNmQwMzli",
                token
        )
    }

    private fun createInstanceToTest(): FileOnlineTokenCreator = FileOnlineTokenCreatorImpl(
            addOn
    )
}