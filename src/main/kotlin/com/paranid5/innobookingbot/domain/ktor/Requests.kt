package com.paranid5.innobookingbot.domain.ktor

import com.paranid5.innobookingbot.data.*
import com.paranid5.innobookingbot.domain.ktor.extensions.mapWithErrorStatus
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.days

private const val SERVER_URL = "https://innobooking-fake-api.onrender.com"

internal suspend inline fun HttpClient.getRoomsAsync() = coroutineScope {
    async(Dispatchers.IO) {
        get("$SERVER_URL/rooms").mapWithErrorStatus { body<List<Room>>() }
    }
}

internal suspend inline fun HttpClient.getFreeRoomsAsync(period: BookTimePeriod) = coroutineScope {
    async(Dispatchers.IO) {
        post("$SERVER_URL/rooms/free") {
            setBody(Json.encodeToString(period))
        }.mapWithErrorStatus { body<List<Room>>() }
    }
}

internal suspend inline fun HttpClient.getMineBooksAsync(email: String) = coroutineScope {
    async(Dispatchers.IO) {
        post("$SERVER_URL/bookings/query") {
            val now = Clock.System.now()

            setBody(
                Json.encodeToString(
                    BookQueryRequest(
                        startedAtOrAfter = now,
                        endedAtOrAfter = now + 3.days,
                        ownerEmailIn = listOf(email)
                    )
                )
            )
        }.mapWithErrorStatus {
            Json.decodeFromString<List<BookResponse>>(bodyAsText())
        }
    }
}

suspend fun HttpClient.bookRoomAsync(roomId: String, bookRequest: BookRequest) = coroutineScope {
    async(Dispatchers.IO) {
        post("$SERVER_URL/rooms/$roomId/book") {
            setBody(Json.encodeToString(bookRequest))
        }.mapWithErrorStatus { body<BookResponse>() }
    }
}

suspend fun HttpClient.bookFilter(start: Instant, end: Instant) = coroutineScope {
    async(Dispatchers.IO) {
        post("$SERVER_URL/bookings/query") {
            setBody(Json.encodeToString(BookQueryRequest(start, end)))
        }.mapWithErrorStatus { body<List<BookResponse>>() }
    }
}

suspend fun HttpClient.cancelBookingAsync(bookId: String) = coroutineScope {
    async(Dispatchers.IO) {
        delete("$SERVER_URL/bookings/$bookId").status
    }
}