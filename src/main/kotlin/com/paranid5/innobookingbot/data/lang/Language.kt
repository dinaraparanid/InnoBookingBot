package com.paranid5.innobookingbot.data.lang

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.decodeFromStream
import java.io.File

sealed interface Language {
    companion object {
        private const val GET_START = "get-start"
        private const val SPECIFY_EMAIL = "specify-email"
        private const val EMAIL_SENT = "email-sent"
        private const val EMAIL_CONFIRMED = "email-confirmed"
        private const val ERROR = "error"
        private const val ALREADY_SIGNED_IN = "already-signed-in"
        private const val GET_REMAINDER_BOOK_END = "get-remainder-book-end"
        private const val INCORRECT_EMAIL = "incorrect-email"
        private const val INCORRECT_AUTH_CODE = "incorrect-auth-code"
        private const val CHOOSE_LANG = "choose-lang"
        private const val BOOK_ID = "book-id"
        private const val EVENT_TITLE = "event-title"
        private const val START = "start"
        private const val END = "end"
        private const val ROOM = "room"
        private const val SUCCESSFULLY_BOOKED = "successfully-booked"

        inline val english
            get() = "English"

        inline val russian
            get() = "Русский"
    }

    val data: Map<String, String>

    private inline val String.fromData
        get() = data[this]!!

    fun getStart(name: String) = GET_START.fromData.replace("{name}", name)

    val specifyEmail get() = SPECIFY_EMAIL.fromData

    val emailSent get() = EMAIL_SENT.fromData

    val emailConfirmed get() = EMAIL_CONFIRMED.fromData

    val error get() = ERROR.fromData

    val alreadySignedIn get() = ALREADY_SIGNED_IN.fromData

    fun getRemainderBookEnd(bookTitle: String) =
        GET_REMAINDER_BOOK_END.fromData.replace("{bookTitle}", bookTitle)

    val incorrectEmail get() = INCORRECT_EMAIL.fromData

    val incorrectAuthCode get() = INCORRECT_AUTH_CODE.fromData

    val chooseLang get() = CHOOSE_LANG.fromData

    val bookId get() = BOOK_ID.fromData

    val eventTitle get() = EVENT_TITLE.fromData

    val start get() = START.fromData

    val end get() = END.fromData

    val room get() = ROOM.fromData

    val successfullyBooked get() = SUCCESSFULLY_BOOKED.fromData

    data object English : Language {
        override val data by lazy {
            Yaml.default.decodeFromStream<Map<String, String>>(
                File("src/main/resources/lang/en.yaml").inputStream()
            )
        }

        override fun toString() = "en"
    }

    data object Russian : Language {
        override val data by lazy {
            Yaml.default.decodeFromStream<Map<String, String>>(
                File("src/main/resources/lang/ru.yaml").inputStream()
            )
        }

        override fun toString() = "ru"
    }
}