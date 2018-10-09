package com.mercandalli.server.files.server

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.content.TextContent
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.withCharset
import io.ktor.pipeline.PipelineContext
import io.ktor.request.uri
import io.ktor.response.respond
import org.json.JSONObject

object ServerNotFound{

    suspend fun PipelineContext<Unit, ApplicationCall>.respondNotFound(httpStatusCode: HttpStatusCode) {
        val statusCode = httpStatusCode.value
        val statusDescription = httpStatusCode.description
        val responseJson = JSONObject()
        val uri = call.request.uri
        responseJson.put("uri", uri)
        responseJson.put("status_code", statusCode)
        responseJson.put("status_description", statusDescription)
        val message = TextContent(
                responseJson.toString(),
                ContentType.Text.Plain.withCharset(Charsets.UTF_8),
                httpStatusCode
        )
        call.respond(
                message
        )
    }
}