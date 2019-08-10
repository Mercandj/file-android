package com.mercandalli.sdk.event.mercan

interface EventMetadataStringInterceptor {

    fun getKey(): String

    fun getValue(): String
}
