package com.mercandalli.sdk.event.mercan

interface EventMetadataLongInterceptor {

    fun getKey(): String

    fun getValue(): Long
}
