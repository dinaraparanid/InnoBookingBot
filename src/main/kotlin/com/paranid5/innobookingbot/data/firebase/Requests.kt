package com.paranid5.innobookingbot.data.firebase

import com.google.firebase.cloud.FirestoreClient
import java.security.SecureRandom
import java.util.*
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

private const val COLLECTION_NAME = "users"

private inline val Long.userData
    get() = FirestoreClient
        .getFirestore()
        .apply { println("FIREBASE 1") }
        .collection(COLLECTION_NAME)
        .apply { println("FIREBASE 2") }
        .whereEqualTo("tg_id", toString())
        .apply { println("FIREBASE 3") }
        .limit(1)
        .apply { println("FIREBASE 4") }
        .get()
        .apply { println("FIREBASE 5") }
        .get()
        .apply { println("FIREBASE 6") }
        .documents
        .apply { println("FIREBASE 7") }
        .firstOrNull()
        .apply { println("FIREBASE 8") }

internal inline val Long.isUserSignedIn: Boolean
    get() = userData != null

internal inline val Long.outlookEmail: String
    get() = userData!!.id

fun addNewUserAsync(telegramId: Long, email: String) =
    FirestoreClient
        .getFirestore()
        .collection(COLLECTION_NAME)
        .add(
            mapOf(
                "id" to email,
                "tg_id" to telegramId.toString()
            )
        )

fun sendLoginEmail(email: String): Int {
    val env = System.getenv()

    val userName = env["EMAIL_AUTH"]!!
    val password = env["EMAIL_AUTH_PASSWORD"]!!

    val emailFrom = env["EMAIL_AUTH"]!!

    val subject = "Email confirmation for InnoBookingBot"

    val authCode = SecureRandom.getInstanceStrong().nextInt(100, 1000000000);

    val text = """
        Finish login process by sending next code to the bot with /login command:
        $authCode
    """.trimIndent()

    val emailProps = Properties().also { emailProps ->
        emailProps["mail.smtp.host"] = "mail.innopolis.ru"
        emailProps["mail.smtp.port"] = "587"
        emailProps["mail.smtp.auth"] = "true"
        emailProps["mail.smtp.starttls.enable"] = "true"
        emailProps["mail.smtp.socketFactory.port"] = "587"
        emailProps["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
    }

    val session = Session.getDefaultInstance(emailProps, object : Authenticator() {
        override fun getPasswordAuthentication() = PasswordAuthentication(userName, password)
    })

    session.debug = true

    val mimeMessage = MimeMessage(session).apply {
        setFrom(InternetAddress(emailFrom))
        setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false))
        setText(text)
        this.subject = subject
        sentDate = Date()
    }

    Transport.send(mimeMessage)

    return authCode
}