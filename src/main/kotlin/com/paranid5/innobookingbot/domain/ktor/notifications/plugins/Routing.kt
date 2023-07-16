package com.paranid5.innobookingbot.domain.ktor.notifications.plugins

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.paranid5.innobookingbot.data.extensions.getMessage
import com.paranid5.innobookingbot.data.firebase.lang
import com.paranid5.innobookingbot.domain.bot.fetchNotifications
import com.paranid5.innobookingbot.domain.ktor.notifications.NotificationData
import io.ktor.client.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Job
import kotlinx.serialization.json.Json

fun Application.configureRouting(
    bot: Bot,
    ktorClient: HttpClient,
    bookEndNotificationTasks: MutableMap<String, Job>
) {
    routing {
        post("/notification") {
            val (tgIdStr, bookResponse) = Json.decodeFromString<NotificationData>(call.receiveText())
            val tgId = tgIdStr.toLong()

            bot.sendMessage(
                chatId = ChatId.fromId(tgId),
                text = bookResponse.getMessage(tgId.lang)
            )

            println("New booking received from WebApp")
            fetchNotifications(bot, ktorClient, bookEndNotificationTasks)
            call.respond(HttpStatusCode.OK)
        }
    }
}