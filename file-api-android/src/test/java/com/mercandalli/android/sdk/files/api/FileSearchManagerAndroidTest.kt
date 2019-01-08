package com.mercandalli.android.sdk.files.api

import org.junit.Assert
import org.junit.Test

class FileSearchManagerAndroidTest {

    @Test
    fun searchSync() {
        // When
        val classLoader = javaClass.classLoader
        val resource = classLoader!!.getResource("file.html")
        val resourcePath = resource.path
        val rootPath = java.io.File(resourcePath).parentFile.absolutePath
        val paths = FileSearchManagerAndroid.searchSync("toto", rootPath)

        // Then
        for (path in paths) {
            if (path.contains("toto.json")) {
                return
            }
            if (path.contains("file.html")) {
                Assert.fail()
            }
        }
        val ioFile = java.io.File(rootPath)
        Assert.fail("Fail on root path: ${ioFile.absolutePath}\n$paths")
    }
}
