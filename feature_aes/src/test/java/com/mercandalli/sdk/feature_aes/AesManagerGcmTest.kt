package com.mercandalli.sdk.feature_aes

import org.junit.Test
import java.io.File

class AesManagerGcmTest : AesManagerTest() {

    private val initializationVector12ByteArray = createByteArray(
        0x2b,
        0x7e,
        0x15,
        0x16,
        0x28,
        0xae,
        0xd2,
        0xa6,
        0xab,
        0xf7,
        0x15,
        0x88,
        0x09,
        0xcf,
        0x4f,
        0x3c
    )

    @Test
    fun encrypt() {
        // Given
        val aesManager = AesModule().createAesManager()

        // When
        val aesCrypter = aesManager.getAesCrypter(
            AesOpMode.CRYPT,
            AesMode.GCM,
            AesPadding.PKCS5,
            key16ByteArray,
            initializationVector12ByteArray
        )
        val output = aesCrypter.crypt(getClearReference().readBytes())
        val outputReferenceFile = getUnclearReference()
        val outputFile = File(getFileRoot().parentFile, "output_clear_encrypt_gcm.aes")
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
            AesMode.GCM,
            AesPadding.PKCS5,
            key16ByteArray,
            initializationVector12ByteArray
        )
        val output = aesCrypter.crypt(getUnclearReference().readBytes())
        val clearReferenceFile = getClearReference()
        val outputFile = File(getFileRoot().parentFile, "output_clear_decrypt_gcm.png")
        outputFile.delete()
        outputFile.writeBytes(output)

        // Then
        areEquals(
            clearReferenceFile,
            outputFile
        )
    }

    @Test
    fun encryptByPart() {
        // Given
        val aesManager = AesModule().createAesManager()

        // When
        val messageBytes = getClearReference().readBytes()
        val messageChunkedBytes = messageBytes.toMutableList().chunked(4096)
        val outputFile = File(getFileRoot().parentFile, "output_unclear_encryptByPart_gcm.aes")
        outputFile.delete()

        val aesCrypter = aesManager.getAesCrypter(
            AesOpMode.CRYPT,
            AesMode.GCM,
            AesPadding.PKCS5,
            key16ByteArray,
            initializationVector12ByteArray
        )
        val output = aesCrypter.cryptListListByte(messageChunkedBytes)
        outputFile.writeBytes(output)
        val outputReferenceFile = getUnclearReference()

        // Then
        areEquals(
            outputReferenceFile,
            outputFile
        )
    }

    private fun getUnclearReference(): File {
        return File(getFileRoot().parentFile, "unclear_gcm_reference.aes")
    }
}
