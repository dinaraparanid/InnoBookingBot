package com.paranid5.innobookingbot

import com.paranid5.innobookingbot.domain.bot.InnoBookingBot
import com.paranid5.innobookingbot.domain.ktor.KtorClient

fun main() = InnoBookingBot(KtorClient()).startPolling()