package com.paranid5.innobookingbot.domain.bot

import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import io.ktor.client.*

fun InnoBookingBot(ktorClient: HttpClient) = bot {
    token = BOT_TOKEN

    dispatch {
        configureCommands(ktorClient)
    }
}