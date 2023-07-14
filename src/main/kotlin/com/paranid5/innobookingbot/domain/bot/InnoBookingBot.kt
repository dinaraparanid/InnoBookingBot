package com.paranid5.innobookingbot.domain.bot

import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import io.github.cdimascio.dotenv.dotenv
import io.ktor.client.*
import kotlinx.coroutines.Job

fun InnoBookingBot(ktorClient: HttpClient, bookEndNotificationTasks: MutableMap<String, Job>) = bot {
    token = System.getenv("BOT_TOKEN")

    dispatch {
        configureCommands(ktorClient, bookEndNotificationTasks)
    }
}