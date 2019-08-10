package com.mercandalli.sdk.feature_aes.internal

import com.mercandalli.sdk.feature_aes.AesManager
import com.mercandalli.sdk.feature_aes.AesOpMode
import com.mercandalli.sdk.feature_aes.AesPadding
import com.mercandalli.sdk.feature_aes.AesMode
import com.mercandalli.sdk.feature_aes.AesCrypter
import com.mercandalli.sdk.feature_aes.AesKeySize
import com.mercandalli.sdk.feature_aes.AesInitializationVectorSize
import java.lang.StringBuilder
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import javax.crypto.KeyGenerator
import javax.crypto.spec.GCMParameterSpec

class AesManagerImpl : AesManager {

    // https://proandroiddev.com/security-best-practices-symmetric-encryption-with-aes-in-java-7616beaaade9
    // https://gist.github.com/netkiller/8167ff2397320c38c946
    // https://crypto.stackexchange.com/questions/9043/what-is-the-difference-between-pkcs5-padding-and-pkcs7-padding
    override fun getAesCrypter(
        opMode: AesOpMode,
        mode: AesMode,
        padding: AesPadding,
        key: ByteArray,
        initializationVector: ByteArray?
    ): AesCrypter {
        val cipher = configureCipher(
            opMode,
            mode,
            padding,
            key,
            initializationVector
        )
        return AesCrypter(
            cipher
        )
    }

    override fun generateKey(
        size: AesKeySize
    ): ByteArray {
        val keyGen = KeyGenerator.getInstance("AES")
        keyGen.init(256)
        val secretKey = keyGen.generateKey()
        return secretKey.encoded
    }

    override fun generateInitializationVector(
        size: AesInitializationVectorSize
    ): ByteArray {
        val secureRandom = SecureRandom()
        val iv = ByteArray(12)
        secureRandom.nextBytes(iv)
        return iv
    }

    private fun configureCipher(
        opMode: AesOpMode,
        mode: AesMode,
        padding: AesPadding,
        key: ByteArray,
        initializationVector: ByteArray?
    ): Cipher {
        val secretKeySpec = SecretKeySpec(key, "AES")
        val transformationBuilder = StringBuilder("AES/")
        transformationBuilder.append(
            when (mode) {
                AesMode.ECB -> "ECB/"
                AesMode.GCM -> "GCM/"
            }
        )
        transformationBuilder.append(
            when (padding) {
                AesPadding.NO -> "NoPadding"
                AesPadding.PKCS5 -> "PKCS5Padding"
            }
        )
        val cipher = Cipher.getInstance(transformationBuilder.toString())
        val encryptMode = when (opMode) {
            AesOpMode.CRYPT -> Cipher.ENCRYPT_MODE
            AesOpMode.DECRYPT -> Cipher.DECRYPT_MODE
        }
        if (mode == AesMode.GCM) {
            val parameterSpec = GCMParameterSpec(128, initializationVector!!)
            cipher.init(encryptMode, secretKeySpec, parameterSpec)
        } else {
            cipher.init(encryptMode, secretKeySpec)
        }
        return cipher
    }
}
