package com.paranid5.innobookingbot

import com.paranid5.innobookingbot.data.firebase.configureFirebase
import com.paranid5.innobookingbot.domain.bot.InnoBookingBot
import com.paranid5.innobookingbot.domain.ktor.KtorClient

fun main() {
    configureFirebase()
    InnoBookingBot(KtorClient()).startPolling()
}