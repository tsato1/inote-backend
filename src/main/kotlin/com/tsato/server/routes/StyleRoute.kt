package com.tsato.server.routes

import com.tsato.server.respondCss
import io.ktor.application.*
import io.ktor.routing.*
import kotlinx.css.*

fun Route.styleRoute() {
    route("/static/css/styles.css") {
        get {
            call.respondCss {
                p {
                    color = Color.green
                }
                h3 {
                    color = Color.black
                    backgroundColor = Color.aqua
                    fontSize = 100.px
                }
            }
        }
    }
}