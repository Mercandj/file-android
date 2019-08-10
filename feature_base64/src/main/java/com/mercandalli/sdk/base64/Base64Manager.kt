package com.mercandalli.sdk.base64

interface Base64Manager {

    fun encodeBase64(clear: ByteArray): ByteArray

    fun encodeBase64ToString(clear: String): String

    fun encodeBase64ToString(clear: ByteArray): String

    fun encodeBase64ToByteArray(clear: String): ByteArray

    fun decodeBase64(unclear: String): String

    fun decodeBase64ToString(unclear: ByteArray): String

    fun decodeBase64ToByteArray(unclear: String): ByteArray
}
