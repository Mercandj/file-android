package com.mercandalli.server.files.authorization

import com.mercandalli.server.files.main.ApplicationGraph

class AuthorizationModule {

    fun createAuthorizationManager(): AuthorizationManager {
        val logManager = ApplicationGraph.getLogManager()
        val fileOnlineAuthentications = ApplicationGraph.getFileOnlineAuthentications()
        return AuthorizationManagerImpl(
            logManager,
            fileOnlineAuthentications
        )
    }
}
