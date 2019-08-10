package com.mercandalli.sdk.feature_aes

import org.junit.Test

class StringTest {

    @Test
    fun stringNotBijectiveWithByteArray() {
        // Given
        val keyByteArray = createByteArray(
            0x2b, 0x7e, 0x15, 0x16,
            0x28, 0xae, 0xd2, 0xa6,
            0xab, 0xf7, 0x15, 0x88,
            0x09, 0xcf, 0x4f, 0x3c
        )

        // When
        val string = String(keyByteArray)
        val extractedByteArray = string.toByteArray()

        // Then
        for (i in 0 until keyByteArray.size) {
            if (keyByteArray[i] != extractedByteArray[i]) {
                // System.out.println("Error at $i")
            }
        }
    }

    private fun createByteArray(vararg ints: Int) =
        ByteArray(ints.size) { pos -> ints[pos].toByte() }
}
