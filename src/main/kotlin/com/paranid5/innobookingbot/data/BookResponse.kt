package com.paranid5.innobookingbot.data

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookResponse(
    val id: String,
    val title: String,
    val start: Instant,
    val end: Instant,
    val room: Room,
    @SerialName("owner_email") val ownerEmail: String
)
