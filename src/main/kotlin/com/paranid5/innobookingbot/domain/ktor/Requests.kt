package com.paranid5.innobookingbot.domain.ktor

import com.paranid5.innobookingbot.data.*
import com.paranid5.innobookingbot.domain.ktor.extensions.mapWithErrorStatus
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.datetime.Instant

// TODO: Real API
private const val SERVER_URL = "http://0.0.0.0:8080"

suspend fun HttpClient.getRoomsAsync() = coroutineScope {
    async(Dispatchers.IO) {
        get("$SERVER_URL/rooms").mapWithErrorStatus { body<List<Room>>() }
    }
}

suspend fun HttpClient.getFreeRoomsAsync(period: BookTimePeriod) = coroutineScope {
    async(Dispatchers.IO) {
        post("$SERVER_URL/rooms/free") {
            contentType(ContentType.Application.Json)
            setBody(period)
        }.mapWithErrorStatus { body<List<Room>>() }
    }
}

suspend fun HttpClient.bookRoomAsync(roomId: String, bookRequest: BookRequest) = coroutineScope {
    async(Dispatchers.IO) {
        post("$SERVER_URL/rooms/$roomId/book") {
            contentType(ContentType.Application.Json)
            setBody(bookRequest)
        }.mapWithErrorStatus { body<BookResponse>() }
    }
}

suspend fun HttpClient.bookFilter(start: Instant, end: Instant) = coroutineScope {
    async(Dispatchers.IO) {
        post("$SERVER_URL/bookings/query") {
            contentType(ContentType.Application.Json)
            setBody(BookQueryRequest(start, end))
        }.mapWithErrorStatus { body<List<BookResponse>>() }
    }
}

suspend fun HttpClient.cancelBookingAsync(bookId: String) = coroutineScope {
    async(Dispatchers.IO) {
        delete("$SERVER_URL/bookings/$bookId").status
    }
}