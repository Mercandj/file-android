package com.mercandalli.server.files.server

import com.mercandalli.server.files.file.FileGetHandler
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.content.*
import io.ktor.features.StatusPages
import io.ktor.features.origin
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.withCharset
import io.ktor.request.uri
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import org.json.JSONObject
import java.io.File

class ServerManagerImpl(
        private val rootServerPath: String,
        private val fileGetHandler: FileGetHandler
) : ServerManager {

    private var server: NettyApplicationEngine

    init {
        val module: Application.() -> Unit = {
            install(StatusPages) {
                exception<Throwable> {
                    call.respond(HttpStatusCode.InternalServerError)
                }
                status(HttpStatusCode.NotFound) {
                    val statusCode = it.value
                    val statusDescription = it.description
                    val responseJson = JSONObject()
                    val uri = call.request.uri
                    responseJson.put("uri", uri)
                    responseJson.put("status_code", statusCode)
                    responseJson.put("status_description", statusDescription)
                    val message = TextContent(
                            responseJson.toString(),
                            ContentType.Text.Plain.withCharset(Charsets.UTF_8),
                            it
                    )
                    call.respond(
                            message
                    )
                }
            }
            routing {
                get("/status") {
                    val uri = call.request.uri
                    val origin = call.request.origin
                    val responseJson = JSONObject()
                    responseJson.put("running", true)
                    responseJson.put("uri", uri)
                    responseJson.put("origin", origin)
                    responseJson.put("root_server_path", rootServerPath)
                    call.respondText(responseJson.toString(), ContentType.Text.Plain)
                }
                get("/file-api/file/{id}") {
                    val id = call.parameters["id"]
                    val response = fileGetHandler.get(id!!)
                    call.respondText(response)
                }
                post("/file-api/file") {
                    call.respondText("Not implemented")
                }
                static {
                    staticRootFolder = File(rootServerPath)
                    files("static")
                    default("static/index.html")
                }
            }
        }
        // http://ktor.io/servers/configuration.html
        server = embeddedServer(
                Netty,
                port = 80,
                module = module,
                host = "0.0.0.0"
        )
    }

    override fun start() {
        server.start(wait = true)
    }
}