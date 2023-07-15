package com.paranid5.innobookingbot.domain.ktor

import com.github.kotlintelegrambot.Bot
import com.paranid5.innobookingbot.domain.bot.fetchNotifications
import io.ktor.client.*
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal suspend inline fun CoroutineScope.NotificationServer(
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