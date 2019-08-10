package com.mercandalli.sdk.feature_aes.internal

import com.mercandalli.sdk.base64.Base64Manager
import com.mercandalli.sdk.feature_aes.AesManager
import com.mercandalli.sdk.feature_aes.AesBase64Crypter
import com.mercandalli.sdk.feature_aes.AesBase64Manager
import com.mercandalli.sdk.feature_aes.AesOpMode
import com.mercandalli.sdk.feature_aes.AesMode
import com.mercandalli.sdk.feature_aes.AesKeySize
import com.mercandalli.sdk.feature_aes.AesInitializationVectorSize
import com.mercandalli.sdk.feature_aes.AesPadding

class AesBase64ManagerImpl(
    private val aesManager: AesManager,
    private val base64Manager: Base64Manager
) : AesBase64Manager {

    override fun getAesCrypter(
        opMode: AesOpMode,
        mode: AesMode,
        padding: AesPadding,
        key: String,
        initializationVector: String?
    ): AesBase64Crypter {
        val aesCrypter = aesManager.getAesCrypter(
            opMode,
            mode,
            padding,
            base64Manager.decodeBase64ToByteArray(key),
            if (initializationVector == null) {
                null
            } else {
                base64Manager.decodeBase64ToByteArray(initializationVector)
            }
        )
        return AesBase64Crypter(
            aesCrypter,
            base64Manager
        )
    }

    override fun generateKey(size: AesKeySize): String {
        return base64Manager.encodeBase64ToString(
            aesManager.generateKey(size)
        )
    }

    override fun generateInitializationVector(size: AesInitializationVectorSize): String {
        return base64Manager.encodeBase64ToString(
            aesManager.generateInitializationVector(size)
        )
    }
}
