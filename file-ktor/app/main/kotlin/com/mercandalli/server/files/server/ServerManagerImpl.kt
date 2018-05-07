package com.mercandalli.server.files.server

import com.mercandalli.server.files.file.FileGetHandler
import com.mercandalli.server.files.main.ApplicationGraph
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.content.default
import io.ktor.content.files
import io.ktor.content.static
import io.ktor.content.staticRootFolder
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import java.io.File

class ServerManagerImpl(
        private val rootServerPath: String,
        private val fileGetHandler: FileGetHandler
) : ServerManager {

    private var server: NettyApplicationEngine

    init {
        val module: Application.() -> Unit = {
            routing {
                get("/status") {
                    call.respondText("OK", ContentType.Text.Plain)
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
                    files("server-static")
                    default("server-static/index.html")
                }
            }
        }
        server = embeddedServer(
                Netty,
                port = 8080,
                module = module
        )
    }

    override fun start() {
        server.start(wait = true)
    }
}