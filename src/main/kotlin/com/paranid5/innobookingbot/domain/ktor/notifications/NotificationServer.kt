package com.paranid5.innobookingbot.domain.ktor.notifications

import com.github.kotlintelegrambot.Bot
import com.paranid5.innobookingbot.domain.ktor.notifications.plugins.configureHTTP
import com.paranid5.innobookingbot.domain.ktor.notifications.plugins.configureRouting
import io.ktor.client.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.*

internal fun CoroutineScope.NotificationServer(
    bot: Bot,
    ktorClient: HttpClient,
    bookEndNotificationTasks: MutableMap<String, Job>
) = launch(Dispatchers.IO) {
    val env = System.getenv()
    val port = env["PORT"]!!.toInt()

    embeddedServer(Netty, port = port, host = "0.0.0.0") {
        module(bot, ktorClient, bookEndNotificationTasks)
    }.start(wait = true)
}

fun Application.module(
    bot: Bot,
    ktorClient: HttpClient,
    bookEndNotificationTasks: MutableMap<String, Job>
) {
    configureHTTP()
    configureRouting(bot, ktorClient, bookEndNotificationTasks)
}