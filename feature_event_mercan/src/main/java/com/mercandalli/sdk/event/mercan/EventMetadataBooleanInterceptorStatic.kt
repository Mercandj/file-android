package com.mercandalli.sdk.event.mercan

class EventMetadataBooleanInterceptorStatic(
    private val key: String,
    private val value: Boolean
) : EventMetadataBooleanInterceptor {

    override fun getKey(): String {
        return key
    }

    override fun getValue(): Boolean {
        return value
    }
}
