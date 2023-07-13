package com.paranid5.innobookingbot.data

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookRequest(
    val title: String,
    val start: Instant,
    val end: Instant,
    @SerialName("owner_email") val ownerEmail: String
) : Comparable<BookRequest> {
    override fun compareTo(other: BookRequest) = end.compareTo(other.end)
}
