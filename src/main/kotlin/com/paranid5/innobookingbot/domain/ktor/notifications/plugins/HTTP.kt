package com.paranid5.innobookingbot.domain.ktor.notifications.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.cors.routing.*

fun Application.configureHTTP() {
    install(CORS) {
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Options)
        allowHeader(HttpHeaders.Authorization)
        anyHost()
    }

    install(Compression) {
        gzip {
            priority = 1.0
        }

        deflate {
            priority = 10.0
            minimumSize(1024)
        }
    }
}