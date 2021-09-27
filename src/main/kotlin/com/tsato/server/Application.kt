package com.tsato.server

import com.tsato.server.data.collections.User
import com.tsato.server.data.registerUser
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.routing.*
import io.ktor.gson.gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    // intercepts responses with additional info in the headers, such as date
    install(DefaultHeaders)
    // logs http requests and responses
    install(CallLogging)
    // defines url endpoints to make rest api
    install(Routing)
    // specifies which content type the server should respond with
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }

    CoroutineScope(Dispatchers.IO).launch {
        registerUser(
            User("test@test.com", "asdf")
        )
    }
}
