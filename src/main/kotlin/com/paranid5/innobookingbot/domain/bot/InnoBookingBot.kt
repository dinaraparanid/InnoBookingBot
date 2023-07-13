package com.paranid5.innobookingbot.domain.bot

import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import io.github.cdimascio.dotenv.dotenv
import io.ktor.client.*

fun InnoBookingBot(ktorClient: HttpClient) = bot {
    token = dotenv()["BOT_TOKEN"]

    dispatch {
        configureCommands(ktorClient)
    }
}