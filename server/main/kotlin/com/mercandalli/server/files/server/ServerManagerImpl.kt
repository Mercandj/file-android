package com.mercandalli.server.files.server

import com.mercandalli.sdk.files.api.online.response_json.ServerResponse
import com.mercandalli.server.files.file_handler.FileHandlerDelete
import com.mercandalli.server.files.file_handler.FileHandlerGet
import com.mercandalli.server.files.file_handler.FileHandlerPost
import com.mercandalli.server.files.log.LogManager
import com.mercandalli.server.files.server.ServerNotFound.respondNotFound
import com.mercandalli.server.files.server.ServerStatus.respondStatus
import com.mercandalli.server.files.shell.ShellManager
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.application.ApplicationCallPipeline
import io.ktor.application.call
import io.ktor.auth.Authentication
import io.ktor.auth.UserIdPrincipal
import io.ktor.auth.authenticate
import io.ktor.auth.basic
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.http.content.default
import io.ktor.http.content.files
import io.ktor.http.content.static
import io.ktor.http.content.staticRootFolder
import io.ktor.request.receive
import io.ktor.request.receiveMultipart
import io.ktor.request.receiveText
import io.ktor.response.respond
import io.ktor.response.respondFile
import io.ktor.response.respondRedirect
import io.ktor.response.respondText
import io.ktor.routing.delete
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import java.io.File
import java.util.concurrent.TimeUnit

class ServerManagerImpl(
        private val rootServerPath: String,
        private val fileHandlerGet: FileHandlerGet,
        private val fileHandlerPost: FileHandlerPost,
        private val fileHandlerDelete: FileHandlerDelete,
        private val shellManager: ShellManager,
        private val logManager: LogManager,
        private val pullSubRepositoryShellFile: java.io.File
) : ServerManager {

    private val server: NettyApplicationEngine

    init {
        val module: Application.() -> Unit = createModule()
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

    override fun stop() {
        server.stop(1, 1, TimeUnit.SECONDS)
    }

    private fun createModule(): Application.() -> Unit {
        return {
            install(StatusPages) {
                exception<Throwable> {
                    call.respond(HttpStatusCode.InternalServerError)
                }
                status(HttpStatusCode.NotFound) {
                    respondNotFound(it)
                }
            }
            install(Authentication) {
                basic(name = "myauth1") {
                    realm = "Ktor Server"
                    validate { credentials ->
                        if (credentials.name == credentials.password) {
                            UserIdPrincipal(credentials.name)
                        } else {
                            null
                        }
                    }
                }
            }
            intercept(ApplicationCallPipeline.Call) {
                logManager.logRequest(TAG, call.request)
            }
            routing {
                authenticate("myauth1") {
                    get("/admin") {
                        // ...
                    }
                }
                get("/status") {
                    respondStatus()
                }
                get("/pull") { _ ->
                    val absolutePath = pullSubRepositoryShellFile.absolutePath
                    shellManager.execute("bash $absolutePath") {

                    }
                    val response = "Pull sub repository"
                    logManager.logResponse(TAG, call.request, response)
                    call.respondText(response)
                }
                get("/file-api/file") {
                    val headers = call.request.headers
                    val queryParameters = call.request.queryParameters
                    val parentPath = queryParameters["parent_path"]
                    val response = if (parentPath == null) {
                        fileHandlerGet.get(
                                headers
                        )
                    } else {
                        fileHandlerGet.getFromParent(
                                headers,
                                parentPath
                        )
                    }
                    logManager.logResponse(TAG, call.request, response)
                    call.respondText(response)
                }
                post("/file-api/file") {
                    val headers = call.request.headers
                    val body = call.receiveText()
                    val response = fileHandlerPost.create(
                            headers,
                            body
                    )
                    logManager.logResponse(TAG, call.request, response)
                    call.respondText(response)
                }
                delete("/file-api/file") {
                    val headers = call.request.headers
                    val body = call.receiveText()
                    val response = fileHandlerDelete.deleteFile(
                            headers,
                            body
                    )
                    logManager.logResponse(TAG, call.request, response)
                    call.respondText(response)
                }
                get("/file-api/file/size") {
                    val headers = call.request.headers
                    val queryParameters = call.request.queryParameters
                    val path = queryParameters["path"]
                    val response = fileHandlerGet.getSize(
                            headers,
                            path
                    )
                    logManager.logResponse(TAG, call.request, response)
                    call.respondText(response)
                }
                post("/file-api/file/download") {
                    val headers = call.request.headers
                    val body = call.receiveText()
                    val response = fileHandlerPost.download(
                            headers,
                            body
                    )
                    if (response == null) {
                        call.respondText(ServerResponse.create(
                                "Error",
                                false
                        ).toJsonString())
                        return@post
                    }
                    call.respondFile(response)
                }
                post("/file-api/file/upload") {
                    val headers = call.request.headers
                    val multipart = call.receiveMultipart()
                    val response = fileHandlerPost.upload(
                            headers,
                            multipart
                    )
                    logManager.logResponse(TAG, call.request, response)
                    call.respondText(response)
                }
                post("/file-api/file/rename") {
                    val headers = call.request.headers
                    val body = call.receiveText()
                    val response = fileHandlerPost.rename(
                            headers,
                            body
                    )
                    logManager.logResponse(TAG, call.request, response)
                    call.respondText(response)
                }
                post("/file-api/file/copy") {
                    val headers = call.request.headers
                    val body = call.receiveText()
                    val response = fileHandlerPost.copy(
                            headers,
                            body
                    )
                    logManager.logResponse(TAG, call.request, response)
                    call.respondText(response)
                }
                post("/file-api/file/cut") {
                    val headers = call.request.headers
                    val body = call.receiveText()
                    val response = fileHandlerPost.cut(
                            headers,
                            body
                    )
                    logManager.logResponse(TAG, call.request, response)
                    call.respondText(response)
                }
                get("/file-api/file/{id}") {
                    val headers = call.request.headers
                    val id = call.parameters["id"]
                    val response = fileHandlerGet.get(
                            headers,
                            id!!
                    )
                    logManager.logResponse(TAG, call.request, response)
                    call.respondText(response)
                }
                get("/timothe") {
                    call.respondRedirect("/timothe/index.html")
                }
                static("/timothe") {
                    staticRootFolder = File("$rootServerPath/static")
                    files("timothe")
                    default("timothe/index.html")
                }
                get("/1418") {
                    call.respondRedirect("/1418/index.html")
                }
                post("/1418/contact-us") {
                    val post = call.receive<Parameters>()
                    val firstName = post["first_name"]
                    val lastName = post["last_name"]
                    val email = post["email"]
                    val text = post["text"]
                    logManager.log1418ContactUs(
                            firstName,
                            lastName,
                            email,
                            text
                    )
                    call.respondText("Message envoyé")
                }
                static("/1418") {
                    staticRootFolder = File("$rootServerPath/static")
                    files("1418")
                    default("1418/index.html")
                }
                static {
                    staticRootFolder = File(rootServerPath)
                    files("static")
                    default("static/index.html")
                }
            }
        }
    }

    companion object {
        const val TAG = "ServerManager"
    }
}