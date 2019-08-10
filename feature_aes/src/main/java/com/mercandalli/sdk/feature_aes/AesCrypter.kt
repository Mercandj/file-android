package com.mercandalli.sdk.feature_aes

import java.io.ByteArrayOutputStream
import javax.crypto.Cipher
import javax.crypto.CipherOutputStream

class AesCrypter(
    private val cipher: Cipher
) {

    fun crypt(message: ByteArray): ByteArray {
        return cipher.doFinal(
            message
        )
    }

    fun cryptListListByte(messageChunks: List<List<Byte>>): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val cipherOutputStream = CipherOutputStream(
            byteArrayOutputStream,
            cipher
        )
        cipherOutputStream.use {
            for (messageChunk in messageChunks) {
                cipherOutputStream.write(createByteArray(messageChunk))
            }
        }
        return byteArrayOutputStream.toByteArray()
    }

    fun crypt(messageChunks: List<ByteArray>): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val cipherOutputStream = CipherOutputStream(
            byteArrayOutputStream,
            cipher
        )
        cipherOutputStream.use {
            for (messageChunk in messageChunks) {
                cipherOutputStream.write(messageChunk)
            }
        }
        return byteArrayOutputStream.toByteArray()
    }

    private fun createByteArray(bytes: List<Byte>): ByteArray {
        val result = ByteArray(bytes.size)
        for (i in 0 until bytes.size) {
            result[i] = bytes[i]
        }
        return result
    }
}
