package com.mercandalli.sdk.files.api.online

interface FileOnlineLoginRepository {

    fun save(key: String, value: String)

    fun load(key: String): String?
}
