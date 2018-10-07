package com.mercandalli.server.files.main

import java.io.File
import java.net.URLDecoder

fun main(args: Array<String>) {
    val tag = "Main"

    val path = ApplicationGraph::class.java.protectionDomain.codeSource.location.path
    val decodedPath = URLDecoder.decode(path, "UTF-8")
    val rootPath = File(decodedPath).parentFile.absolutePath

    ApplicationGraph.initialize(rootPath)

    val logManager = ApplicationGraph.getLogManager()
    logManager.d(tag, "Welcome to file server")
    logManager.d(tag, "Root: $rootPath")

    val shellManager = ApplicationGraph.getShellManager()
    shellManager.execute("ls", {})
    val serverManager = ApplicationGraph.getServerManager()
    serverManager.start()
}