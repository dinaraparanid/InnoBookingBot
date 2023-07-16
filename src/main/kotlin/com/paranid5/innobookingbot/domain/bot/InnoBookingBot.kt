package com.paranid5.innobookingbot.domain.bot

import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch

fun InnoBookingBot() = bot {
    val env = System.getenv()
    token = env["BOT_TOKEN"]!!
    dispatch { configureCommands() }
}