package com.tsato.server

import ch.qos.logback.core.html.CssBuilder
import com.tsato.server.data.isPasswordValid
import com.tsato.server.routes.loginRoute
import com.tsato.server.routes.noteRoute
import com.tsato.server.routes.registerRoute
import com.tsato.server.routes.styleRoute
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.routing.*
import io.ktor.gson.gson
import io.ktor.http.*
import io.ktor.response.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    // intercepts responses with additional info in the headers, such as date
    install(DefaultHeaders)
    // logs http requests and responses
    install(CallLogging)
    // specifies which content type the server should respond with
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }
    install(Authentication) {
        configureAuth()
    }
    // defines url endpoints to make rest api
    install(Routing) {
        styleRoute()
        registerRoute()
        loginRoute()
        noteRoute()
    }
}

private fun Authentication.Configuration.configureAuth() {
    basic {
        realm = "iNote Server"

        validate { credentials ->
            val email = credentials.name
            val password = credentials.password

            if (isPasswordValid(email, password)) {
                UserIdPrincipal(email)
            }
            else {
                null
            }
        }
    }
}

suspend inline fun ApplicationCall.respondCss(builder: kotlinx.css.CssBuilder.() -> Unit) {
    respondText(kotlinx.css.CssBuilder().apply(builder).toString(), ContentType.Text.CSS)
}
