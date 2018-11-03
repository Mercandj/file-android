@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.server.files.file_handler

import com.mercandalli.server.files.authorization.AuthorizationManager
import com.mercandalli.server.files.file_repository.FileRepository
import com.mercandalli.server.files.log.LogManager
import io.ktor.http.EmptyHeaders
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class FileHandlerGetImplTest {

    @Mock
    private lateinit var fileRepository: FileRepository
    @Mock
    private lateinit var logManager: LogManager
    @Mock
    private lateinit var authorizationManager: AuthorizationManager

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun getLogCall() {
        // Given
        val fileHandlerGet = createInstanceToTest()

        // When
        fileHandlerGet.get(EmptyHeaders)

        // Then
        Mockito.verify(logManager).d("FileHandlerGet", "get()")
    }

    private fun createInstanceToTest(): FileHandlerGet {
        return FileHandlerGetImpl(
            fileRepository,
            logManager,
            authorizationManager
        )
    }
}
