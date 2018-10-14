package com.mercandalli.server.files.authorization

import io.ktor.http.Headers

interface AuthorizationManager {

    fun isAuthorized(headers: Headers): Boolean
}