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
    fun sha1HelloTwoTimes() {
        // Given
        val value = "hello"

        // When
        val sha1 = HashUtils.sha1(value, 2)

        // Then
        Assert.assertEquals("9cf5caf6c36f5cccde8c73fad8894c958f4983da", sha1)
    }

    @Test
    fun sha1Empty() {
        // Given
        val value = ""

        // When
        val sha1 = HashUtils.sha1(value)

        // Then
        Assert.assertEquals("da39a3ee5e6b4b0d3255bfef95601890afd80709", sha1)
    }

    @Test
    fun sha256Hello() {
        // Given
        val value = "hello"

        // When
        val sha1 = HashUtils.sha256(value)

        // Then
        Assert.assertEquals("2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824", sha1)
    }

    @Test
    fun sha256HelloTwoTimes() {
        // Given
        val value = "hello"

        // When
        val sha1 = HashUtils.sha256(value, 2)

        // Then
        Assert.assertEquals("d7914fe546b684688bb95f4f888a92dfc680603a75f23eb823658031fff766d9", sha1)
    }

    @Test
    fun sha256Empty() {
        // Given
        val value = ""

        // When
        val sha1 = HashUtils.sha256(value)

        // Then
        Assert.assertEquals("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", sha1)
    }
}
