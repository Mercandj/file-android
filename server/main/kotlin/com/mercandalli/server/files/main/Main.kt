package com.mercandalli.server.files.main

import com.mercandalli.server.files.window.MainFrame
import java.io.File
import java.lang.StringBuilder
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

    //val shellManager = ApplicationGraph.getShellManager()
    //shellManager.execute("ls", {})
    //val serverManager = ApplicationGraph.getServerManager()
    //serverManager.start()

    val pullSubRepositoryShellFile = java.io.File(rootPath, "pull-sub-repository.sh")
    if (pullSubRepositoryShellFile.exists()) {
        pullSubRepositoryShellFile.delete()
    }
    pullSubRepositoryShellFile.createNewFile()
    pullSubRepositoryShellFile.writeText(createPullSubRepositoryShellContent(
            listOf(
                    "$rootPath/static",
                    "$rootPath/static/1418",
                    "$rootPath/static/timothe"
            )
    ))

    MainFrame.start(pullSubRepositoryShellFile)
}

private fun createPullSubRepositoryShellContent(paths: List<String>): String {
    val stringBuilder = StringBuilder()
    for (path in paths) {
        stringBuilder.append("pushd $path\n")
        stringBuilder.append("  git pull\n")
        stringBuilder.append("popd\n")
    }
    return stringBuilder.toString()
}

