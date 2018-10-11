package com.mercandalli.sdk.files.api.online

data class FileOnlineAuthentication(
        val login: String,
        val passwordSha1: String
) {

    fun createToken() = FileOnlineTokenCreator.createToken(login, passwordSha1)

    companion object {

        fun isLogged(token: String, fileOnlineAuthentications: List<FileOnlineAuthentication>): Boolean {
            for (fileOnlineAuthentication in fileOnlineAuthentications) {
                val logged = isLogged(token, fileOnlineAuthentication)
                if (logged) {
                    return true
                }
            }
            return false
        }

        private fun isLogged(token: String, fileOnlineAuthentication: FileOnlineAuthentication) =
                fileOnlineAuthentication.createToken() == token
    }
}