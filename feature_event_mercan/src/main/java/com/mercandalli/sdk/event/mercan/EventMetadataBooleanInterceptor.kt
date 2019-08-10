package com.mercandalli.sdk.event.mercan

interface EventMetadataBooleanInterceptor {

    fun getKey(): String

    fun getValue(): Boolean
}
