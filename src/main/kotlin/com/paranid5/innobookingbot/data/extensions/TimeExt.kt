package com.paranid5.innobookingbot.data.extensions

import kotlinx.datetime.*
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import kotlin.time.Duration.Companion.hours

inline val localZoneOffset: ZoneOffset
    get() = ZoneId.of(ZoneOffset.systemDefault().id).rules.getOffset(java.time.Instant.now())

inline val localTimeZone
    get() = TimeZone.currentSystemDefault()

fun Instant.toJavaLocalDateTime() = LocalDateTime.ofInstant(toJavaInstant(), localZoneOffset)

fun LocalDateTime.toKotlinInstant() = toInstant(localZoneOffset).toKotlinInstant()

inline val currentLocalTime
    get() = Clock.System.now().plus(3.hours)