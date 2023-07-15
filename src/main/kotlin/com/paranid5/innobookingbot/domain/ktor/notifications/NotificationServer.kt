package com.paranid5.innobookingbot.domain.ktor.notifications

import com.github.kotlintelegrambot.Bot
import com.paranid5.innobookingbot.domain.bot.fetchNotifications
import com.paranid5.innobookingbot.domain.ktor.notifications.plugins.configureHTTP
import com.paranid5.innobookingbot.domain.ktor.notifications.plugins.configureRouting
import io.ktor.client.*
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.utils.io.*
import kotlinx.coroutines.*

@Deprecated("Switched to HTTPS Ktor version")
internal suspend inline fun CoroutineScope.HuiNotificationServer(
    bot: Bot,
    ktorClient: HttpClient,
    bookEndNotificationTasks: MutableMap<String, Job>
) = launch(Dispatchers.IO) {
    aSocket(ActorSelectorManager(Dispatchers.IO)).tcp()
        .bind("0.0.0.0", 1337)
        .accept().use { socket ->
            while (true) {
                if (socket.openReadChannel().readUTF8Line() == "asda") {
                    println("New booking received from WebApp")
                    fetchNotifications(bot, ktorClient, bookEndNotificationTasks)
                }
            }
        }
}

internal suspend inline fun NotificationServer(
    bot: Bot,
    ktorClient: HttpClient,
    bookEndNotificationTasks: MutableMap<String, Job>
) = coroutineScope {
    embeddedServer(Netty, port = 1337, host = "0.0.0.0") {
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