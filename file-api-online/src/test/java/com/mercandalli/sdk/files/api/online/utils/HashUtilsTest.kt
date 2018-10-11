package com.mercandalli.sdk.files.api.online.utils

import org.junit.Assert
import org.junit.Test

class HashUtilsTest {

    @Test
    fun sha1Hello() {
        // Given
        val value = "hello"

        // When
        val sha1 = HashUtils.sha1(value)

        // Then
        Assert.assertEquals("aaf4c61ddcc5e8a2dabede0f3b482cd9aea9434d", sha1)
    }

    @Test
    fun sha1() {
        // Given
        val value = ""

        // When
        val sha1 = HashUtils.sha1(value)

        // Then
        Assert.assertEquals("da39a3ee5e6b4b0d3255bfef95601890afd80709", sha1)
    }
}