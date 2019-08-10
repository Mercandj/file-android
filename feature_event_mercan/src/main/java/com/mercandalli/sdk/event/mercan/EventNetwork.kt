package com.mercandalli.sdk.event.mercan

interface EventNetwork {

    fun postAsync(
        url: String,
        headers: Map<String, String>,
        body: String
    )
}
