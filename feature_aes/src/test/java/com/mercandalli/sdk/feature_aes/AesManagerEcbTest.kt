package com.mercandalli.sdk.feature_aes

import org.junit.Test
import java.io.File

class AesManagerEcbTest : AesManagerTest() {

    @Test
    fun encrypt() {
        // Given
        val aesManager = AesModule().createAesManager()

        // When
        val aesCrypter = aesManager.getAesCrypter(
            AesOpMode.CRYPT,
            AesMode.ECB,
            AesPadding.PKCS5,
            key16ByteArray
        )
        val output = aesCrypter.crypt(getClearReference().readBytes())
        val outputReferenceFile = getUnclearReference()
        val outputFile = File(getFileRoot().parentFile, "output_unclear_encrypt_ecb.aes")
        outputFile.delete()
        outputFile.writeBytes(output)

        // Then
        areEquals(
            outputReferenceFile,
            outputFile
        )
    }

    @Test
    fun decrypt() {
        // Given
        val aesManager = AesModule().createAesManager()

        // When
        val aesCrypter = aesManager.getAesCrypter(
            AesOpMode.DECRYPT,
            AesMode.ECB,
            AesPadding.PKCS5,
            key16ByteArray
        )
        val output = aesCrypter.crypt(getUnclearReference().readBytes())
        val clearReferenceFile = getClearReference()
        val outputFile = File(getFileRoot().parentFile, "output_clear_decrypt_ecb.png")
        outputFile.delete()
        outputFile.writeBytes(output)

        // Then
        areEquals(
            clearReferenceFile,
            outputFile
        )
    }

    /**
     * Note: Working only for ECB method
     */
    @Test
    fun encryptByPartManually() {
        // Given
        val aesManager = AesModule().createAesManager()

        // When
        val messageBytes = getClearReference().readBytes()
        val messageChunkedBytes = messageBytes.toMutableList().chunked(4096)
        val outputFile = File(getFileRoot().parentFile, "output_unclear_cryptByPartManually_ecb.aes")
        outputFile.delete()

        val bytes = ArrayList<Byte>()
        for (i in 0 until messageChunkedBytes.size) {
            val isLast = i == messageChunkedBytes.size - 1
            val aesCrypter = aesManager.getAesCrypter(
                AesOpMode.CRYPT,
                AesMode.ECB,
                if (isLast) {
                    AesPadding.PKCS5
                } else {
                    AesPadding.NO
                },
                key16ByteArray
            )
            val output = aesCrypter.crypt(createByteArray(messageChunkedBytes[i]))
            bytes.addAll(output.toMutableList())
        }
        outputFile.writeBytes(createByteArray(bytes))
        val outputReferenceFile = getUnclearReference()

        // Then
        areEquals(
            outputReferenceFile,
            outputFile
        )
    }

    private fun getUnclearReference(): File {
        return File(getFileRoot().parentFile, "unclear_ecb_reference.aes")
    }
}
