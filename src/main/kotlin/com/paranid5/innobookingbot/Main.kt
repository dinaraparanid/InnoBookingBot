package com.paranid5.innobookingbot

import com.paranid5.innobookingbot.data.firebase.configureFirebase
import com.paranid5.innobookingbot.domain.bot.InnoBookingBot
import com.paranid5.innobookingbot.domain.bot.fetchNotifications
import com.paranid5.innobookingbot.domain.ktor.KtorClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.ConcurrentHashMap

fun main() = runBlocking {
    configureFirebase()

    val ktorClient = KtorClient()
    var bookEndNotificationTasks = ConcurrentHashMap<String, Job>()

    InnoBookingBot(ktorClient, bookEndNotificationTasks).run {
        bookEndNotificationTasks = fetchNotifications(ktorClient)
        startPolling()
    }
}