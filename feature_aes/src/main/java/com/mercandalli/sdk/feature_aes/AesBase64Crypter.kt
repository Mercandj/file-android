package com.mercandalli.sdk.feature_aes

import com.mercandalli.sdk.base64.Base64Manager
import java.io.ByteArrayOutputStream
import javax.crypto.Cipher
import javax.crypto.CipherOutputStream

class AesBase64Crypter(
    private val aesCrypter: AesCrypter,
    private val base64Manager: Base64Manager
) {

    fun crypt(message: String): String {
        return base64Manager.encodeBase64ToString(
            aesCrypter.crypt(
                base64Manager.encodeBase64ToByteArray(message)
            )
        )
    }

    fun decrypt(message: String): String {
        return base64Manager.decodeBase64ToString(
            aesCrypter.crypt(
                base64Manager.decodeBase64ToByteArray(message)
            )
        )
    }
}
