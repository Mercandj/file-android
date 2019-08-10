package com.mercandalli.sdk.feature_aes

import org.junit.Assert
import java.io.File

open class AesManagerTest {

    protected val key16ByteArray = createByteArray(
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

    protected fun areEquals(
        outputReferenceFile: File,
        outputFile: File
    ) {
        val outputReferenceBytes = outputReferenceFile.readBytes()
        val outputBytes = outputFile.readBytes()
        for (i in 0 until outputReferenceBytes.size) {
            if (outputReferenceBytes[i] != outputBytes[i]) {
                Assert.fail(
                    "outputFile: ${outputFile.absolutePath}\n" +
                        "Error at $i"
                )
            }
        }
    }

    protected fun createByteArray(
        vararg ints: Int
    ) = ByteArray(ints.size) { pos -> ints[pos].toByte() }

    protected fun createByteArray(
        bytes: List<Byte>
    ): ByteArray {
        val result = ByteArray(bytes.size)
        for (i in 0 until bytes.size) {
            result[i] = bytes[i]
        }
        return result
    }

    protected fun getFileRoot(): File {
        val classLoader = javaClass.classLoader
        val resource = classLoader!!.getResource("root.txt")
        val resourcePath = resource.path
        return File(resourcePath)
    }

    protected fun getClearReference(): File {
        return File(getFileRoot().parentFile, "clear_reference.png")
    }
}
