package com.mercandalli.sdk.feature_aes

enum class AesPadding {

    /**
     * Input message should be: size % 16 == 0
     */
    NO,

    /**
     * PKCS5 padding will always add a full block pad when the ciphertext is exactly block size regardless of implementation
     */
    PKCS5
}
