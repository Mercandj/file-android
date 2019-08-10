package com.mercandalli.sdk.base64

import com.mercandalli.sdk.base64.internal.Base64ManagerImpl

class Base64Module {

    fun createBase64Manager(): Base64Manager {
        return Base64ManagerImpl()
    }
}
