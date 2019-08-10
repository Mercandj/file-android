package com.mercandalli.sdk.base64.internal

import com.mercandalli.sdk.base64.Base64Manager
import java.io.ByteArrayOutputStream

class Base64ManagerImpl : Base64Manager {

    override fun encodeBase64(clear: ByteArray): ByteArray {
        return clear.encodeBase64()
    }

    override fun encodeBase64ToString(clear: String): String {
        return clear.encodeBase64ToString()
    }

    override fun encodeBase64ToString(clear: ByteArray): String {
        return clear.encodeBase64ToString()
    }

    override fun encodeBase64ToByteArray(clear: String): ByteArray {
        return clear.encodeBase64ToByteArray()
    }

    override fun decodeBase64(unclear: String): String {
        return unclear.decodeBase64()
    }

    override fun decodeBase64ToString(unclear: ByteArray): String {
        return unclear.decodeBase64ToString()
    }

    override fun decodeBase64ToByteArray(unclear: String): ByteArray {
        return unclear.decodeBase64ToByteArray()
    }

    companion object {

        private fun String.encodeBase64ToString(): String =
            String(this.toByteArray().encodeBase64())

        private fun String.encodeBase64ToByteArray(): ByteArray = this.toByteArray().encodeBase64()
        private fun ByteArray.encodeBase64ToString(): String = String(this.encodeBase64())

        private fun String.decodeBase64(): String = String(this.toByteArray().decodeBase64())
        private fun String.decodeBase64ToByteArray(): ByteArray = this.toByteArray().decodeBase64()
        private fun ByteArray.decodeBase64ToString(): String = String(this.decodeBase64())

        private fun ByteArray.encodeBase64(): ByteArray {
            val table = (CharRange('A', 'Z') + CharRange('a', 'z') + CharRange(
                '0',
                '9'
            ) + '+' + '/').toCharArray()
            val output = ByteArrayOutputStream()
            var padding = 0
            var position = 0
            while (position < this.size) {
                var b = this[position].toInt() and 0xFF shl 16 and 0xFFFFFF
                if (position + 1 < this.size) b =
                    b or (this[position + 1].toInt() and 0xFF shl 8) else padding++
                if (position + 2 < this.size) b =
                    b or (this[position + 2].toInt() and 0xFF) else padding++
                for (i in 0 until 4 - padding) {
                    val c = b and 0xFC0000 shr 18
                    output.write(table[c].toInt())
                    b = b shl 6
                }
                position += 3
            }
            for (i in 0 until padding) {
                output.write('='.toInt())
            }
            return output.toByteArray()
        }

        private fun ByteArray.decodeBase64(): ByteArray {
            val table = intArrayOf(
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                62,
                -1,
                -1,
                -1,
                63,
                52,
                53,
                54,
                55,
                56,
                57,
                58,
                59,
                60,
                61,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                0,
                1,
                2,
                3,
                4,
                5,
                6,
                7,
                8,
                9,
                10,
                11,
                12,
                13,
                14,
                15,
                16,
                17,
                18,
                19,
                20,
                21,
                22,
                23,
                24,
                25,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                26,
                27,
                28,
                29,
                30,
                31,
                32,
                33,
                34,
                35,
                36,
                37,
                38,
                39,
                40,
                41,
                42,
                43,
                44,
                45,
                46,
                47,
                48,
                49,
                50,
                51,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1
            )

            val output = ByteArrayOutputStream()
            var position = 0
            while (position < this.size) {
                var b: Int
                if (table[this[position].toInt()] != -1) {
                    b = table[this[position].toInt()] and 0xFF shl 18
                } else {
                    position++
                    continue
                }
                var count = 0
                if (position + 1 < this.size && table[this[position + 1].toInt()] != -1) {
                    b = b or (table[this[position + 1].toInt()] and 0xFF shl 12)
                    count++
                }
                if (position + 2 < this.size && table[this[position + 2].toInt()] != -1) {
                    b = b or (table[this[position + 2].toInt()] and 0xFF shl 6)
                    count++
                }
                if (position + 3 < this.size && table[this[position + 3].toInt()] != -1) {
                    b = b or (table[this[position + 3].toInt()] and 0xFF)
                    count++
                }
                while (count > 0) {
                    val c = b and 0xFF0000 shr 16
                    output.write(c.toChar().toInt())
                    b = b shl 8
                    count--
                }
                position += 4
            }
            return output.toByteArray()
        }
    }
}
