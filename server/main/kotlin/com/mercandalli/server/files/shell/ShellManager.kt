package com.mercandalli.server.files.shell

interface ShellManager {

    fun execute(command: String, block: (result: String) -> Unit)

    fun isWindows(): Boolean
}