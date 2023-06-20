package com.paranid5.innobookingbot.data.extensions

import com.paranid5.innobookingbot.data.BookResponse

inline val BookResponse.message
    get() = """
        Booking id: $id;
        Event title: $title
        Start: $start
        End: $end
        Room: ${room.name}
    """.trimIndent()

inline val BookResponse.successfulBookingMessage
    get() = "Successfully booked!\n\n$message"

inline val List<BookResponse>.joinedToMessage
    get() = mapIndexed { ind, bookResponse ->
        """
---------- #${ind + 1} ----------
${bookResponse.message}""".trimIndent()
    }.joinToString(separator = "\n\n")
