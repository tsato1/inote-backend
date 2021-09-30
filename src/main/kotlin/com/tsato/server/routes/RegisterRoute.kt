package com.tsato.server.routes

import com.tsato.server.data.collections.User
import com.tsato.server.data.registerUser
import com.tsato.server.data.requests.AccountRequest
import com.tsato.server.data.responses.SimpleResponse
import com.tsato.server.data.userExists
import com.tsato.server.security.getHashWithSalt
import io.ktor.application.*
import io.ktor.features.ContentTransformationException
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.registerRoute() {
    route("/register") {
        post { // this post block is coroutine
            val request = try {
                call.receive<AccountRequest>() // parse the incoming request json into AccountRequest type
            }
            catch (e: ContentTransformationException) {
                // call is the current request
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            val userExists = userExists(request.email)
            if (!userExists) {
                if (registerUser(User(request.email, getHashWithSalt(request.password)))) {
                    call.respond(OK, SimpleResponse(true, "Successfully created account"))
                }
                else {
                    call.respond(OK, SimpleResponse(false, "An unknown error occurred"))
                }
            }
            else {
                call.respond(OK, SimpleResponse(false, "The user already exists"))
            }
        }
    }
}