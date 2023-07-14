package com.paranid5.innobookingbot.domain.bot

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.paranid5.innobookingbot.data.extensions.currentLocalTime
import com.paranid5.innobookingbot.data.firebase.allUsersAsync
import com.paranid5.innobookingbot.data.firebase.emailsToTgIds
import com.paranid5.innobookingbot.domain.ktor.bookFilter
import io.ktor.client.*
import kotlinx.coroutines.*
import kotlinx.datetime.Instant
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.minutes

internal suspend inline fun Bot.fetchNotifications(ktorClient: HttpClient): Map<String, Job> = coroutineScope {
    val bookEndNotificationTasks = ConcurrentHashMap<String, Job>()
    val start = currentLocalTime
    val end = currentLocalTime + 7.days
    val emailsToTgIds = allUsersAsync.get().documents.emailsToTgIds

    ktorClient
        .bookFilter(start, end)
        .await()
        .mapLeft { bookings ->
            bookEndNotificationTasks.putAll(
                bookings.mapNotNull { (id, title, _, bookEnd, _, ownerEmail) ->
                    emailsToTgIds[ownerEmail]
                        ?.let(String::toLongOrNull)
                        ?.let(ChatId::fromId)
                        ?.let {
                            launchNotificationHandling(
                                bookEndNotificationTasks = bookEndNotificationTasks,
                                chatId = it,
                                id = id,
                                bookEnd = bookEnd,
                                bookTitle = title
                            )
                        }
                        ?.let { id to it }
                }
            )
        }

    bookEndNotificationTasks
}

private suspend inline fun Bot.launchNotificationHandling(
    bookEndNotificationTasks: MutableMap<String, Job>,
    chatId: ChatId,
    id: String,
    bookEnd: Instant,
    bookTitle: String
) = coroutineScope {
    val task = launch(Dispatchers.IO) {
        val delayTime = maxOf(bookEnd - 5.minutes - currentLocalTime, Duration.ZERO)
        println("Delay before $bookTitle ($bookEnd): $delayTime")
        delay(delayTime)

        sendBookEndSoon(chatId, bookTitle)
        bookEndNotificationTasks.remove(id)
    }

    bookEndNotificationTasks[id] = task
    task
}

private fun Bot.sendBookEndSoon(chatId: ChatId, bookTitle: String) =
    sendMessage(chatId, "Remainder: your booking `$bookTitle` is about to end in five minutes")