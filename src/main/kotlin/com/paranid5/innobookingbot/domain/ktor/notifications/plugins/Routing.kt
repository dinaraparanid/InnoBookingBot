package com.paranid5.innobookingbot.domain.ktor.notifications.plugins

import com.github.kotlintelegrambot.Bot
import com.paranid5.innobookingbot.domain.bot.fetchNotifications
import io.ktor.client.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Job

fun Application.configureRouting(
    bot: Bot,
    ktorClient: HttpClient,
    bookEndNotificationTasks: MutableMap<String, Job>
) {
    routing {
        get {
            println("New booking received from WebApp")
            fetchNotifications(bot, ktorClient, bookEndNotificationTasks)
        }
    }
}