package com.paranid5.innobookingbot

import com.paranid5.innobookingbot.data.firebase.configureFirebase
import com.paranid5.innobookingbot.domain.bot.InnoBookingBot
import com.paranid5.innobookingbot.domain.bot.fetchNotifications
import com.paranid5.innobookingbot.domain.ktor.KtorClient
import com.paranid5.innobookingbot.domain.ktor.notifications.NotificationServer
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import java.util.concurrent.ConcurrentHashMap

fun main() = runBlocking {
    configureFirebase()

    val ktorClient = KtorClient()
    val bookEndNotificationTasks = ConcurrentHashMap<String, Job>()

    InnoBookingBot().run {
        fetchNotifications(bot = this, ktorClient, bookEndNotificationTasks)
        NotificationServer(bot = this, ktorClient, bookEndNotificationTasks)
        startPolling()
    }
}