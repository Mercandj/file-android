package com.mercandalli.sdk.feature_aes

interface AesManager {

    /**
     * @param initializationVector Should not be null for GCM. Should be null for ECB
     */
    fun getAesCrypter(
        opMode: AesOpMode,
        mode: AesMode,
        padding: AesPadding,
        key: ByteArray,
        initializationVector: ByteArray? = null
    ): AesCrypter

    fun generateKey(
        size: AesKeySize
    ): ByteArray

    /**
     * NEVER REUSE THIS IV WITH SAME KEY
     */
    fun generateInitializationVector(
        size: AesInitializationVectorSize
    ): ByteArray
}
