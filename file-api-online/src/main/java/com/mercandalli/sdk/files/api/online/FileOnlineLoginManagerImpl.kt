package com.mercandalli.sdk.files.api.online

import com.mercandalli.sdk.files.api.online.utils.HashUtils

internal class FileOnlineLoginManagerImpl : FileOnlineLoginManager {

    private var login: String? = null
    private var passwordSha1: String? = null

    override fun set(login: String, password: String) {
        this.login = login
        this.passwordSha1 = HashUtils.sha1(password)
    }

    override fun getLogin() = login

    override fun hasToken() = login != null && passwordSha1 != null

    override fun createToken() = FileOnlineTokenCreator.createToken(login!!, passwordSha1!!)
}