package com.paranid5.innobookingbot.data.extensions

import kotlinx.datetime.Instant

fun String.toInstant(): Instant {
    println(this)
    val (dateStr, timeStr) = split(";")
    return Instant.parse("${dateStr}T$timeStr:00Z")
}

fun String.toInstantOrNull() = runCatching(this::toInstant).getOrNull()