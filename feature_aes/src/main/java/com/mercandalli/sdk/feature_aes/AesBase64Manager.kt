package com.mercandalli.sdk.feature_aes

interface AesBase64Manager {

    /**
     * @param initializationVector Should not be null for GCM. Should be null for ECB
     */
    fun getAesCrypter(
        opMode: AesOpMode,
        mode: AesMode,
        padding: AesPadding,
        key: String,
        initializationVector: String? = null
    ): AesBase64Crypter

    fun generateKey(
        size: AesKeySize
    ): String

    fun generateInitializationVector(
        size: AesInitializationVectorSize
    ): String
}
