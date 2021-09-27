package com.tsato.server.routes

import com.tsato.server.data.isPasswordValid
import com.tsato.server.data.requests.AccountRequest
import com.tsato.server.data.responses.SimpleResponse
import io.ktor.application.*
import io.ktor.features.ContentTransformationException
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.loginRoute() {
    route("/login") {
        post {
            val request = try {
                call.receive<AccountRequest>() // parse the incoming request json into AccountRequest type
            }
            catch (e: ContentTransformationException) {
                // call is the current request
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            val isValidPassword = isPasswordValid(request.email, request.password)
            if (isValidPassword) {
                call.respond(OK, SimpleResponse(true, "Login successful."))
            }
            else {
                call.respond(OK, SimpleResponse(false, "The email or password is incorrect."))
            }
        }
    }
}