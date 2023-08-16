package com.paranid5.innobookingbot.data.extensions

import com.paranid5.innobookingbot.data.BookResponse
import com.paranid5.innobookingbot.data.lang.Language

fun BookResponse.getMessage(lang: Language) = """
        ${lang.bookId}: $id
        ${lang.eventTitle}: $title
        ${lang.start}: $start
        ${lang.end}: $end
        ${lang.room}: ${room.name}
    """.trimIndent()

fun BookResponse.getSuccessfulBookingMessage(lang: Language) =
    "${lang.successfullyBooked}\n\n${getMessage(lang)}"

inline val List<BookResponse>.joinedToMessage
    get() = mapIndexed { ind, bookResponse ->
        """
---------- #${ind + 1} ----------
${bookResponse.getMessage(Language.English)}""".trimIndent()
    }.joinToString(separator = "\n\n")
