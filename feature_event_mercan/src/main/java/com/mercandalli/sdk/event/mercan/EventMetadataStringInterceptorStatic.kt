package com.mercandalli.sdk.event.mercan

class EventMetadataStringInterceptorStatic(
    private val key: String,
    private val value: String
) : EventMetadataStringInterceptor {

    override fun getKey(): String {
        return key
    }

    override fun getValue(): String {
        return value
    }
}
