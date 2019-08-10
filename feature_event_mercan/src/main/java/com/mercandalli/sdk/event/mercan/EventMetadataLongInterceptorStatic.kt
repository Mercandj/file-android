package com.mercandalli.sdk.event.mercan

class EventMetadataLongInterceptorStatic(
    private val key: String,
    private val value: Long
) : EventMetadataLongInterceptor {

    override fun getKey(): String {
        return key
    }

    override fun getValue(): Long {
        return value
    }
}
