package com.paranid5.innobookingbot.domain.ktor.notifications.plugins

import com.github.kotlintelegrambot.Bot
import com.paranid5.innobookingbot.domain.bot.fetchNotifications
import io.ktor.client.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Job

fun Application.configureRouting(
    bot: Bot,
    ktorClient: HttpClient,
    bookEndNotificationTasks: MutableMap<String, Job>
) {
    routing {
        post {
            println("New booking received from WebApp")
            fetchNotifications(bot, ktorClient, bookEndNotificationTasks)
            call.respond(HttpStatusCode.OK)
        }
    }
}