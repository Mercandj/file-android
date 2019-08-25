package com.mercandalli.server.files.server

import com.mercandalli.server.files.main.ApplicationGraph
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.features.origin
import io.ktor.http.ContentType
import io.ktor.util.pipeline.PipelineContext
import io.ktor.request.uri
import io.ktor.response.respondText
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

object ServerStatus {

    suspend fun PipelineContext<Unit, ApplicationCall>.respondStatus() {
        val rootPath = ApplicationGraph.getRootPath()
        val timeManager = ApplicationGraph.getTimeManager()
        val lastCommitDate = "git log -1 --format=%cd".runCommand(File(rootPath))
        call.application.environment.log.debug("/status")
        val uri = call.request.uri
        val responseJson = JSONObject()
        responseJson.put("running", true)
        responseJson.put("uri", uri)
        responseJson.put("origin_host", call.request.origin.host)
        responseJson.put("id_address", call.request.local.remoteHost)
        responseJson.put("root_server_path", rootPath)
        responseJson.put("last_commit_date", lastCommitDate)
        responseJson.put("time", timeManager.getTimeString())
        responseJson.put("time_file_name", timeManager.getTimeFileNameString())
        responseJson.put("time_millis", timeManager.getTimeMillis())
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
