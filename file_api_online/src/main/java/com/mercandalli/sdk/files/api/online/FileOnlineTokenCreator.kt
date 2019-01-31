package com.mercandalli.sdk.files.api.online

interface FileOnlineTokenCreator {

    fun createToken(login: String, passwordSha1: String): String

    fun createTokens(login: String, passwordSha1: String, minutesRange: Int): List<String>
}
