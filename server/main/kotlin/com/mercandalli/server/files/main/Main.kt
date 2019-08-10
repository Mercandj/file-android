package com.mercandalli.server.files.main

import com.mercandalli.sdk.files.api.online.FileOnlineAuthentication
import com.mercandalli.server.files.main_argument.MainArgument
import com.mercandalli.server.files.window.MainFrame
import java.io.File
import java.lang.StringBuilder
import java.net.URLDecoder

fun main(args: Array<String>) {
    if (args.isNotEmpty()) {
        println("Arg should be empty. Found ${args[0]}")
        return
    }
    val tag = "Main"
    val rootPath = extractRootPath()
    val projectFolder = File(rootPath).parentFile
    val configFile = File(projectFolder, "server_config/server_config.json")
    val mainArgument = MainArgument.fromJsonFile(configFile)
    val fileOnlineAuthentications = listOf(FileOnlineAuthentication(
        mainArgument.getFileOnlineAuthenticationLogin(),
        mainArgument.getFileOnlineAuthenticationPasswordSha1()
    ))
    val pullSubRepositoryShellFile = createPullSubRepositoryShellFile(rootPath)

    ApplicationGraph.initialize(
        mainArgument,
        rootPath,
        pullSubRepositoryShellFile,
        fileOnlineAuthentications
    )

    val logManager = ApplicationGraph.getLogManager()
    logManager.d(tag, "Welcome to file server")
    logManager.d(tag, "Root: $rootPath")

    if (args.contains("-ui")) {
        MainFrame.start()
    } else {
        val serverManager = ApplicationGraph.getServerManager()
        serverManager.start()
    }
}

private fun extractRootPath(): String {
    val path = ApplicationGraph::class.java.protectionDomain.codeSource.location.path
    val decodedPath = URLDecoder.decode(path, "UTF-8")
    return File(decodedPath).parentFile.absolutePath
}

private fun createPullSubRepositoryShellFile(rootPath: String): File {
    val pullSubRepositoryShellFile = File(rootPath, "pull-sub-repository.sh")
    if (pullSubRepositoryShellFile.exists()) {
        pullSubRepositoryShellFile.delete()
    }
    pullSubRepositoryShellFile.createNewFile()
    pullSubRepositoryShellFile.writeText(createPullSubRepositoryShellContent(
        listOf(
            "$rootPath/static",
            "$rootPath/static/1418",
            "$rootPath/static/timothe",
            "$rootPath/static/mvp"
        )
    ))
    return pullSubRepositoryShellFile
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
