package com.paranid5.innobookingbot.domain.bot

import arrow.core.Either
import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.handlers.CommandHandlerEnvironment
import com.github.kotlintelegrambot.dispatcher.text
import com.github.kotlintelegrambot.entities.ChatId
import com.paranid5.innobookingbot.data.BookRequest
import com.paranid5.innobookingbot.data.BookResponse
import com.paranid5.innobookingbot.data.BookTimePeriod
import com.paranid5.innobookingbot.data.Room
import com.paranid5.innobookingbot.data.extensions.*
import com.paranid5.innobookingbot.data.firebase.addNewUserAsync
import com.paranid5.innobookingbot.data.firebase.isUserSignedIn
import com.paranid5.innobookingbot.data.firebase.outlookEmail
import com.paranid5.innobookingbot.data.firebase.sendLoginEmail
import com.paranid5.innobookingbot.domain.ktor.*
import io.ktor.client.*
import io.ktor.http.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.datetime.Instant
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

private const val START_REQUEST = "start"
private const val SIGN_IN_REQUEST = "sign_in"
private const val ROOMS_REQUEST = "rooms"
private const val FREE_REQUEST = "free"
private const val MINE_REQUEST = "mine"
private const val BOOK_REQUEST = "book"
private const val BOOK_FILTER_REQUEST = "book_filter"
private const val CANCEL_REQUEST = "cancel"
private const val RULES_REQUEST = "rules"

private val scope = CoroutineScope(Dispatchers.IO)

private val innoEmailRegex = Regex("[a-z]+\\.[a-z]+@innopolis\\.university")

private inline val newMessageChannel
    get() = Channel<String>(
        capacity = 10000,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

fun Dispatcher.configureCommands() {
    val messageChannels = ConcurrentHashMap<ChatId, Channel<String>>()
    val inputControls = ConcurrentHashMap<ChatId, AtomicBoolean>()
    val usersToTasks = ConcurrentHashMap<ChatId, Job?>()

    configureStartCommand()
    configureSignInCommand(messageChannels, inputControls, usersToTasks)

    text {
        update.consume()
        println("Received: $text from: ${message.from?.username}")

        if (inputControls.getOrPut(chatId, ::AtomicBoolean).get())
            messageChannels.getOrPut(chatId, ::newMessageChannel).send(text)
    }
}

private suspend inline fun CommandHandlerEnvironment.launchNotificationHandling(
    bookEndNotificationTasks: MutableMap<String, Job>,
    id: String,
    bookRequest: BookRequest
) = coroutineScope {
    val task = launch(Dispatchers.IO) {
        val delayTime = maxOf(bookRequest.end - 5.minutes - currentLocalTime, Duration.ZERO)
        println(delayTime)
        delay(delayTime)

        sendBookEndSoon(bookRequest.title)
        bookEndNotificationTasks.remove(id)
    }

    bookEndNotificationTasks[id] = task
    task
}

private fun Dispatcher.configureStartCommand() =
    command(START_REQUEST) {
        update.consume()

        sendMessage(
            """
                Hello, ${message.chat.firstName}! I am Inno Booking Bot!
                I can help you to book available study rooms in Innopolis University.

                To start booking, please, /sign_in with your Innopolis (Outlook) email.
                To book a room, click on 'Book room' button below.
            """.trimIndent()
        )
    }

@OptIn(ExperimentalCoroutinesApi::class)
private fun Dispatcher.configureSignInCommand(
    messageChannels: MutableMap<ChatId, Channel<String>>,
    inputControls: MutableMap<ChatId, AtomicBoolean>,
    usersToTasks: MutableMap<ChatId, Job?>
) = command(SIGN_IN_REQUEST) {
    update.consume()

    usersToTasks[chatId]?.cancel(CancellationException("New command received"))

    usersToTasks[chatId] = scope.launch(Dispatchers.IO) {
        if (telegramId.isUserSignedIn) {
            sendAlreadySignedInError()
            return@launch
        }

        messageChannels[chatId]?.let { chan ->
            while (!chan.isEmpty)
                chan.receive()
        }

        val inputController = inputControls.getOrPut(chatId, ::AtomicBoolean).apply { set(true) }

        val email = getEmailOrSendError(chatId, messageChannels) ?: run {
            inputController.set(false)
            return@launch
        }

        val correctAuthCode = sendLoginEmail(name = message.from!!.firstName, email = email)

        messageChannels[chatId]?.let { chan ->
            while (!chan.isEmpty)
                chan.receive()
        }

        val inputAuthCode = getEmailAuthCodeOrSendError(chatId, messageChannels) ?: run {
            inputController.set(false)
            return@launch
        }

        if (inputAuthCode != correctAuthCode) {
            sendError("Incorrect auth code. Please, try again")
            inputController.set(false)
            return@launch
        }

        addNewUserAsync(telegramId, email)
        sendMessage("You have successfully confirmed your email! Now you have an access to bot's functionality")
    }
}

@Deprecated("Available in WebApp")
private fun Dispatcher.configureRoomsCommand(ktorClient: HttpClient) =
    command(ROOMS_REQUEST) {
        update.consume()

        scope.launch(Dispatchers.IO) {
            if (!telegramId.isUserSignedIn) {
                sendNotSignedInError()
                return@launch
            }

            when (val result = ktorClient.getRoomsAsync().await()) {
                is Either.Left -> sendRooms(result.value)
                is Either.Right -> sendError(result.value)
            }
        }
    }

@Deprecated("Available in WebApp")
private fun Dispatcher.configureFreeRoomsCommand(
    ktorClient: HttpClient,
    messageChannels: MutableMap<ChatId, Channel<String>>,
    inputControls: MutableMap<ChatId, AtomicBoolean>
) = command(FREE_REQUEST) {
    update.consume()

    scope.launch(Dispatchers.IO) {
        if (!telegramId.isUserSignedIn) {
            sendNotSignedInError()
            return@launch
        }

        val inputController = inputControls.getOrPut(chatId, ::AtomicBoolean).apply { set(true) }

        val (start, end) = getBookTimePeriodsOrSendError(chatId, messageChannels) ?: run {
            inputController.set(false)
            return@launch
        }

        inputController.set(false)

        when (val result = ktorClient.getFreeRoomsAsync(BookTimePeriod(start, end)).await()) {
            is Either.Left -> sendRooms(result.value)
            is Either.Right -> sendError(result.value)
        }
    }
}

@Deprecated("Available in WebApp")
private fun Dispatcher.configureMineRequest(ktorClient: HttpClient) =
    command(MINE_REQUEST) {
        update.consume()

        if (!telegramId.isUserSignedIn) {
            sendNotSignedInError()
            return@command
        }

        when (
            val result = ktorClient
                .getMineBooksAsync(telegramId.outlookEmail)
                .await()
        ) {
            is Either.Left -> sendBookings(result.value)
            is Either.Right -> sendError(result.value)
        }
    }

@Deprecated("Available in WebApp")
private fun Dispatcher.configureBookRequest(
    ktorClient: HttpClient,
    messageChannels: MutableMap<ChatId, Channel<String>>,
    inputControls: MutableMap<ChatId, AtomicBoolean>,
    bookEndNotificationTasks: MutableMap<String, Job>
) = command(BOOK_REQUEST) {
    update.consume()

    scope.launch(Dispatchers.IO) {
        if (!telegramId.isUserSignedIn) {
            sendNotSignedInError()
            return@launch
        }

        val inputController = inputControls.getOrPut(chatId, ::AtomicBoolean).apply { set(true) }

        val title = getBookTitleOrSendError(chatId, messageChannels) ?: run {
            inputController.set(false)
            return@launch
        }

        val email = telegramId.outlookEmail

        val (start, end) = getBookTimePeriodsOrSendError(chatId, messageChannels) ?: run {
            inputController.set(false)
            return@launch
        }

        val roomId = getBookRoomId(chatId, messageChannels)
        inputController.set(false)

        val bookRequest = BookRequest(title, start, end, email)

        when (
            val result = ktorClient
                .bookRoomAsync(roomId, bookRequest)
                .await()
        ) {
            is Either.Left -> {
                sendBookingResponse(bookingResponse = result.value)

                launchNotificationHandling(
                    bookEndNotificationTasks,
                    result.value.id,
                    bookRequest
                )
            }

            is Either.Right -> sendError(result.value)
        }
    }
}

@Deprecated("Available in WebApp")
private fun Dispatcher.configureQueryRequest(
    ktorClient: HttpClient,
    messageChannels: MutableMap<ChatId, Channel<String>>,
    inputControls: MutableMap<ChatId, AtomicBoolean>
) = command(BOOK_FILTER_REQUEST) {
    update.consume()

    scope.launch(Dispatchers.IO) {
        if (!telegramId.isUserSignedIn) {
            sendNotSignedInError()
            return@launch
        }

        val inputController = inputControls.getOrPut(chatId, ::AtomicBoolean).apply { set(true) }

        val (start, end) = getBookTimePeriodsOrSendError(chatId, messageChannels) ?: run {
            inputController.set(false)
            return@launch
        }

        inputController.set(false)

        when (val result = ktorClient.bookFilter(start, end).await()) {
            is Either.Left -> sendBookings(result.value)
            is Either.Right -> sendError(result.value)
        }
    }
}

@Deprecated("Available in WebApp")
private fun Dispatcher.configureCancelRequest(
    ktorClient: HttpClient,
    messageChannels: MutableMap<ChatId, Channel<String>>,
    inputControls: MutableMap<ChatId, AtomicBoolean>,
    bookEndNotificationTasks: MutableMap<String, Job>
) = command(CANCEL_REQUEST) {
    update.consume()

    scope.launch(Dispatchers.IO) {
        if (!telegramId.isUserSignedIn) {
            sendNotSignedInError()
            return@launch
        }

        val inputController = inputControls.getOrPut(chatId, ::AtomicBoolean).apply { set(true) }
        val bookId = getBookId(chatId, messageChannels)
        val cancelStatus = ktorClient.cancelBookingAsync(bookId).await()
        inputController.set(false)

        sendMessage(
            when {
                cancelStatus.isSuccess() -> {
                    bookEndNotificationTasks.remove(bookId)
                    "Booking $bookId is successfully canceled"
                }

                else -> "Error ${cancelStatus.value}: ${cancelStatus.description}"
            }
        )
    }
}

@Deprecated("Available in WebApp")
private fun Dispatcher.configureRulesCommand() =
    command(RULES_REQUEST) {
        update.consume()

        sendMessage(
            """
                1. A student can book room only from 19 (7 PM) till 8 (8 AM)
                2. Students cannot book room on the first floor
                3. Please, note and respect the fact that your booking can be removed by administrators for some important events or classes (you will be informed in this case)
            """.trimIndent()
        )
    }

fun CommandHandlerEnvironment.sendMessage(text: String) =
    bot.sendMessage(chatId, text)

fun CommandHandlerEnvironment.sendRooms(rooms: List<Room>) =
    sendMessage(rooms.joinedString)

fun CommandHandlerEnvironment.sendBookingResponse(bookingResponse: BookResponse) =
    sendMessage(bookingResponse.successfulBookingMessage)

fun CommandHandlerEnvironment.sendBookings(bookings: List<BookResponse>) =
    sendMessage(bookings.takeIf { it.isNotEmpty() }?.joinedToMessage ?: "No bookings found")

private fun CommandHandlerEnvironment.sendBookTitleRequest() =
    sendMessage("Please, specify title for the event")

private fun CommandHandlerEnvironment.sendEmailRequestForBooking() =
    sendMessage("Please, specify your Innopolis email (e.g. i.ivanov@innopolis.univeristy)")

private fun CommandHandlerEnvironment.sendStartBookTimePeriodRequest() =
    sendMessage(
        """
            Please, specify the starting time period in the next format:
            YYYY-MM-DD;HH:MM
        """.trimIndent()
    )

private fun CommandHandlerEnvironment.sendEndBookTimePeriodRequest() =
    sendMessage(
        """
            Please, specify the ending time period in the next format:
            YYYY-MM-DD;HH:MM
        """.trimIndent()
    )

private fun CommandHandlerEnvironment.sendBookRoomIdRequest() =
    sendMessage("Please, specify room's id for the event")

private fun CommandHandlerEnvironment.sendBookIdRequest() =
    sendMessage("Please, specify book's id")

private fun CommandHandlerEnvironment.sendEmailAuthCodeRequest() =
    sendMessage("To verify your identity, email to your Outlook email was sent. Please, provide auth code to finish login")

private fun CommandHandlerEnvironment.sendError(message: String) =
    sendMessage("Error: $message")

private fun CommandHandlerEnvironment.sendAPIError() =
    sendError("API token was not provided, is invalid or has been expired")

private fun CommandHandlerEnvironment.sendValidationError() =
    sendError("Validation error")

private fun CommandHandlerEnvironment.sendCannotBookError() =
    sendError("This room cannot be booked by you during this time period")

private fun CommandHandlerEnvironment.sendNotSignedInError() =
    sendError("You have not confirmed your email. Please, use /sign_in first")

private fun CommandHandlerEnvironment.sendBookEndSoon(bookTitle: String) =
    sendMessage("Remainder: your booking `$bookTitle` is about to end in five minutes")

private fun CommandHandlerEnvironment.sendAlreadySignedInError() =
    sendError("You are already signed in")

private fun CommandHandlerEnvironment.sendError(statusCode: HttpStatusCode) = when (statusCode) {
    HttpStatusCode.BadRequest -> sendCannotBookError()
    HttpStatusCode.Unauthorized -> sendAPIError()
    HttpStatusCode.UnprocessableEntity -> sendValidationError()
    else -> sendError(statusCode.description)
}

private suspend inline fun getNextMessage(
    chatId: ChatId,
    messageChannels: MutableMap<ChatId, Channel<String>>,
) = messageChannels.getOrPut(chatId, ::newMessageChannel).receive()

private suspend inline fun getBookTime(
    chatId: ChatId,
    messageChannels: MutableMap<ChatId, Channel<String>>
) = getNextMessage(chatId, messageChannels).toInstantOrNull()

private suspend inline fun CommandHandlerEnvironment.getBookTitleOrSendError(
    chatId: ChatId,
    messageChannels: MutableMap<ChatId, Channel<String>>
): String? {
    sendBookTitleRequest()

    val title = getNextMessage(chatId, messageChannels).takeIf(String::isNotEmpty)

    if (title == null)
        sendError("Title cannot be empty")

    return title
}

private suspend inline fun CommandHandlerEnvironment.getEmailOrSendError(
    chatId: ChatId,
    messageChannels: MutableMap<ChatId, Channel<String>>
): String? {
    sendEmailRequestForBooking()

    val email = getNextMessage(chatId, messageChannels).takeIf { it.matches(innoEmailRegex) }

    if (email == null)
        sendError("Incorrect email input. Please, try again")

    return email
}

private suspend inline fun CommandHandlerEnvironment.getBookTimeOrSendError(
    chatId: ChatId,
    messageChannels: MutableMap<ChatId, Channel<String>>,
    sendBookTimePeriodRequest: CommandHandlerEnvironment.() -> Unit
): Instant? {
    sendBookTimePeriodRequest()

    val time = getBookTime(chatId, messageChannels)

    if (time == null)
        sendError("Time parsing error. Please, try again")

    return time
}

private suspend inline fun CommandHandlerEnvironment.getBookTimePeriodsOrSendError(
    chatId: ChatId,
    messageChannels: MutableMap<ChatId, Channel<String>>
): Pair<Instant, Instant>? {
    val (start, end) = arrayOf(
        this::sendStartBookTimePeriodRequest,
        this::sendEndBookTimePeriodRequest
    ).map { sendBookingTimePeriodRequest ->
        getBookTimeOrSendError(chatId, messageChannels) { sendBookingTimePeriodRequest() } ?: return null
    }

    return start to end
}

private suspend inline fun CommandHandlerEnvironment.getEmailAuthCodeOrSendError(
    chatId: ChatId,
    messageChannels: MutableMap<ChatId, Channel<String>>
): Int? {
    val code = getEmailAuthCode(chatId, messageChannels)

    if (code == null)
        sendError("Input parsing error. Please, try again")

    return code
}

private suspend inline fun CommandHandlerEnvironment.getBookRoomId(
    chatId: ChatId,
    messageChannels: MutableMap<ChatId, Channel<String>>
): String {
    sendBookRoomIdRequest()
    return getNextMessage(chatId, messageChannels)
}

private suspend inline fun CommandHandlerEnvironment.getBookId(
    chatId: ChatId,
    messageChannels: MutableMap<ChatId, Channel<String>>
): String {
    sendBookIdRequest()
    return getNextMessage(chatId, messageChannels)
}

private suspend inline fun CommandHandlerEnvironment.getEmailAuthCode(
    chatId: ChatId,
    messageChannels: MutableMap<ChatId, Channel<String>>
): Int? {
    sendEmailAuthCodeRequest()
    return getNextMessage(chatId, messageChannels).toIntOrNull()
}