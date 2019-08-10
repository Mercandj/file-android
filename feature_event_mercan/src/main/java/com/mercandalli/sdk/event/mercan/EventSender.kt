package com.mercandalli.sdk.event.mercan

interface EventSender {

    fun send(key: String, value: String)

    fun addEventMetadataBooleanInterceptor(interceptor: EventMetadataBooleanInterceptor)

    fun addEventMetadataLongInterceptor(interceptor: EventMetadataLongInterceptor)

    fun addEventMetadataStringInterceptor(interceptor: EventMetadataStringInterceptor)
}
