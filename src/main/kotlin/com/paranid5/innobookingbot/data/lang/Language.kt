package com.paranid5.innobookingbot.data.lang

sealed interface Language {
    companion object {
        inline val english
            get() = "English"

        inline val russian
            get() = "Русский"
    }

    fun getStart(name: String): String
    val specifyEmail: String
    val emailSent: String
    val emailConfirmed: String
    val error: String
    val alreadySignedIn: String
    fun getRemainderBookEnd(bookTitle: String): String
    val incorrectEmail: String
    val incorrectAuthCode: String

    object English : Language {
        override fun getStart(name: String) = """
                Hello, $name! I am Inno Booking Bot!
                I can help you to book available study rooms in Innopolis University.

                To start booking, please, /sign_in with your Innopolis (Outlook) email.
                To change language, use /lang
                To book a room, click on 'Book room' button below.
            """.trimIndent()

        override val specifyEmail =
            "Please, specify your Innopolis email (e.g. i.ivanov@innopolis.univeristy)"

        override val emailSent =
            "To verify your identity, email to your Outlook email was sent. Please, provide auth code to finish login"

        override val emailConfirmed =
            "You have successfully confirmed your email! Now you have an access to bot's functionality"

        override val error = "Error"

        override val alreadySignedIn = "You are already signed in"

        override fun getRemainderBookEnd(bookTitle: String) =
            "Remainder: your booking `$bookTitle` is about to end in five minutes"

        override val incorrectEmail = "Incorrect email input. Please, try again"

        override val incorrectAuthCode = "Incorrect auth code. Please, /sign_in again"

        override fun toString() = "en"
    }

    object Russian : Language {
        override fun getStart(name: String) = """
                Привет, $name! Я Inno Booking Bot!
                Я умею бронировать свободные учебные аудитории в Университете Иннополис.

                Перед началом, пожалуйста, /sign_in со своей Outlook почтой Иннополиса.
                Чтобы сменить язык, используйте /lang
                Чтобы забронировать аудиторию, нажмите на кнопку 'Book room' снизу
            """.trimIndent()

        override val specifyEmail =
            "Пожалуйста, укажите свою почту Иннополиса (прим, i.ivanov@innopolis.univeristy)"

        override val emailSent =
            "Для удостоверения вашей личности, было отправлено письмо на почту. Пожалуйста, укажите код из письма, чтобы завершить авторизацию"

        override val emailConfirmed =
            "Вы успешно подтвердили вашу почту! Теперь у вас доступ к функционалу бота"

        override val error = "Ошибка"

        override val alreadySignedIn = "Вы уже вошли"

        override fun getRemainderBookEnd(bookTitle: String) =
            "Напоминание: ваша бронь `$bookTitle` закончится через 5 минут"

        override val incorrectEmail = "Некорректный ввод почты. Попробуйте ещё раз"

        override val incorrectAuthCode = "Некорректный код. Авторизуйтесь через /sign_in ещё раз"

        override fun toString() = "ru"
    }
}