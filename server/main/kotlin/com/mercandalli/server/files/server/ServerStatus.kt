package com.mercandalli.server.files.server

import com.mercandalli.server.files.main.ApplicationGraph
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.features.origin
import io.ktor.http.ContentType
import io.ktor.pipeline.PipelineContext
import io.ktor.request.uri
import io.ktor.response.respondText
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

object ServerStatus {

    suspend fun PipelineContext<Unit, ApplicationCall>.respondStatus() {
        val rootPath = ApplicationGraph.getRootPath()
        val lastCommitDate = "git log -1 --format=%cd".runCommand(java.io.File(rootPath))
        call.application.environment.log.debug("/status")
        val uri = call.request.uri
        val origin = call.request.origin
        val responseJson = JSONObject()
        responseJson.put("running", true)
        responseJson.put("uri", uri)
        responseJson.put("origin", origin)
        responseJson.put("root_server_path", rootPath)
        responseJson.put("last_commit_date", lastCommitDate)
        call.respondText(responseJson.toString(), ContentType.Text.Plain)
    }

    private fun String.runCommand(workingDir: File): String {
        val process = ProcessBuilder(*split(" ").toTypedArray())
            .directory(workingDir)
            .start()
        val reader = BufferedReader(InputStreamReader(process.inputStream))
        return reader.readLine()
    }
}
