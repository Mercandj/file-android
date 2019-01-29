package com.mercandalli.sdk.files.api.online

import java.util.Date

data class FileOnlineAuthentication(
    val login: String,
    val passwordSha1: String
) {

    fun createToken() = fileOnlineTokenCreator.createToken(login, passwordSha1)

    fun createTokens() = fileOnlineTokenCreator.createTokens(login, passwordSha1, 8)

    companion object {

        private val fileOnlineTokenCreator: FileOnlineTokenCreator by lazy {
            val addOn = object : FileOnlineTokenCreatorImpl.AddOn {
                override fun getCurrentDate() = Date()
            }
            FileOnlineTokenCreatorImpl(addOn)
        }

        fun isLogged(token: String, fileOnlineAuthentications: List<FileOnlineAuthentication>): Boolean {
            for (fileOnlineAuthentication in fileOnlineAuthentications) {
                val logged = isLoggedWithToken(token, fileOnlineAuthentication)
                if (logged) {
                    return true
                }
            }
            return false
        }

        @Suppress("unused")
        private fun isLoggedWithToken(token: String, fileOnlineAuthentication: FileOnlineAuthentication) =
            fileOnlineAuthentication.createToken() == token

        @Suppress("unused")
        private fun isLoggedWithTokens(token: String, fileOnlineAuthentication: FileOnlineAuthentication) =
            fileOnlineAuthentication.createTokens().contains(token)
    }
}
