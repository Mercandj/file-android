import com.mercandalli.server.files.main.ApplicationGraph
import io.ktor.application.*
import io.ktor.content.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import java.io.File

fun main(args: Array<String>) {
    val module: Application.() -> Unit = {
        routing {
            get("/status") {
                call.respondText("OK", ContentType.Text.Plain)
            }
            get("/file-api/file/{id}") {
                val id = call.parameters["id"]
                val response = ApplicationGraph.getFileGetHandler().get(id!!)
                call.respondText(response)
            }
            post("/file-api/file") {
                call.respondText("Not implemented")
            }
            static("1418") {
                staticRootFolder = File("/Users/jonathan/Documents")
                files("Portfolio")
                default("Portfolio/index.html")
            }
            static {
                staticRootFolder = File("/Users/jonathan/Documents")
                files("Portfolio")
                default("Portfolio/index.html")
            }
        }
    }
    val server = embeddedServer(
            Netty,
            port = 8080,
            module = module
    )
    server.start(wait = true)
}