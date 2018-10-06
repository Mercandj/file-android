package com.mercandalli.android.sdk.files.api.online

interface FileOnlineLoginManager {

    fun set(login: String, password: String)

    fun hasToken(): Boolean

    fun createToken(): String
}