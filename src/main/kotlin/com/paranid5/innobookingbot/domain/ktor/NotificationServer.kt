package com.paranid5.innobookingbot.domain.ktor

import com.github.kotlintelegrambot.Bot
import com.paranid5.innobookingbot.domain.bot.fetchNotifications
import io.ktor.client.*
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope

internal suspend inline fun NotificationServer(
    bot: Bot,
    ktorClient: HttpClient,
    bookEndNotificationTasks: MutableMap<String, Job>
) = coroutineScope {
    val server = aSocket(ActorSelectorManager(Dispatchers.IO)).tcp()
        .bind("https://innobookingbot-production.up.railway.app", 1337)

    server.accept().use { socket ->
        while (true) {
            if (socket.openReadChannel().readByte() == byteArrayOf(57).first()) {
                println("New booking received from WebApp")
                bot.fetchNotifications(ktorClient, bookEndNotificationTasks)
            }
        }
    }
}