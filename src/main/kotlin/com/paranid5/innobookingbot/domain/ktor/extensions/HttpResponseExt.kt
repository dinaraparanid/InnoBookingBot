package com.paranid5.innobookingbot.domain.ktor.extensions

import arrow.core.Either
import io.ktor.client.statement.*
import io.ktor.http.*

inline fun <L, R> HttpResponse.map(
    onSuccess: HttpResponse.() -> L,
    onError: HttpResponse.() -> R
) = when {
    status.isSuccess() -> Either.Left(onSuccess(this))
    else -> Either.Right(onError(this))
}

inline fun <T> HttpResponse.mapWithErrorStatus(onSuccess: HttpResponse.() -> T) = map(onSuccess) { status }