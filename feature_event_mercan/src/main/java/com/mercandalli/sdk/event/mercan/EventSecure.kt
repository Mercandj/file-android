package com.mercandalli.sdk.event.mercan

interface EventSecure {

    fun crypt(message: String): String

    fun decrypt(message: String): String
}
