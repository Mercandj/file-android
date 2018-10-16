package com.mercandalli.sdk.files.api.online.utils

import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.experimental.and

/**
 * Static methods for dealing with hash.
 */
object HashUtils {

    /**
     * Hash a [String] with the sha1 method.
     *
     * @param text The input [String].
     * @return The sha1 hash.
     */
    fun sha1(text: String?): String? {
        if (text == null) {
            return null
        }
        try {
            val messageDigest = MessageDigest.getInstance("SHA-1")
            messageDigest.update(text.toByteArray(charset("iso-8859-1")), 0, text.length)
            return convertToHex(messageDigest.digest())
        } catch (e: NoSuchAlgorithmException) {
        } catch (e: UnsupportedEncodingException) {
        }
        return null
    }

    /**
     * Hash a [String] with the sha1 method.
     *
     * @param text The input [String].
     * @param time Do "time" hash consecutively.
     * @return The sha1 hash.
     */
    fun sha1(text: String?, time: Int): String? {
        if (text == null) {
            return null
        }
        var result = text
        val messageDigest: MessageDigest
        try {
            messageDigest = MessageDigest.getInstance("SHA-1")
        } catch (e: NoSuchAlgorithmException) {
            return null
        }

        for (i in 0 until time) {
            result = hash(messageDigest, result)
        }
        return result
    }

    /**
     * Hash a [String] with the sha256 method.
     *
     * @param text The input [String].
     * @return The sha1 hash.
     */
    fun sha256(text: String?): String? {
        if (text == null) {
            return null
        }
        try {
            val messageDigest = MessageDigest.getInstance("SHA-256")
            messageDigest.update(text.toByteArray(charset("iso-8859-1")), 0, text.length)
            return convertToHex(messageDigest.digest())
        } catch (e: NoSuchAlgorithmException) {
        } catch (e: UnsupportedEncodingException) {
        }

        return null
    }

    fun sha256(text: String?, time: Int): String? {
        if (text == null) {
            return null
        }
        var result = text
        val messageDigest: MessageDigest
        try {
            messageDigest = MessageDigest.getInstance("SHA-256")
        } catch (e: NoSuchAlgorithmException) {
            return null
        }

        for (i in 0 until time) {
            result = hash(messageDigest, result)
        }
        return result
    }

    private fun hash(
            messageDigest: MessageDigest,
            text: String?): String? {
        if (text == null) {
            return null
        }
        try {
            messageDigest.update(text.toByteArray(charset("iso-8859-1")), 0, text.length)
        } catch (e: UnsupportedEncodingException) {
            return null
        }

        return convertToHex(messageDigest.digest())
    }

    private fun convertToHex(data: ByteArray): String {
        val buf = StringBuilder()
        for (e in data.indices) {
            val b = (data[e] and 0xFF.toByte()).toInt()
            var halfByte = (b ushr 4) and 0x0F
            var twoHalf = 0
            do {
                val char = if (halfByte in 0..9) '0'.toInt() + halfByte
                else 'a'.toInt() + (halfByte - 10)
                buf.append(char.toChar())
                halfByte = b and 0x0F
            } while (twoHalf++ < 1)
        }
        return buf.toString()
    }
}

