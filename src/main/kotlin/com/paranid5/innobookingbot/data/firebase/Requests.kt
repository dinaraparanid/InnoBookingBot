package com.paranid5.innobookingbot.data.firebase

import com.google.firebase.cloud.FirestoreClient
import com.paranid5.innobookingbot.data.lang.Language
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import kotlin.random.Random
import kotlin.random.nextInt

private const val COLLECTION_NAME = "users"

private inline val firestoreCollection
    get() = FirestoreClient
        .getFirestore()
        .collection(COLLECTION_NAME)

internal inline val Long.userData
    get() = firestoreCollection
        .whereEqualTo("tg_id", toString())
        .limit(1)
        .get()
        .get()
        .documents
        .firstOrNull()

internal inline val Long.isUserSignedIn: Boolean
    get() = userData != null

internal inline val Long.outlookEmail: String
    get() = userData!!.get("id")!! as String

internal inline val Long.lang
    get() = when (userData?.get("lang") as? String?) {
        "ru" -> Language.Russian
        else -> Language.English
    }

internal inline val allUsersAsync
    get() = firestoreCollection.get()

fun setLangAsync(tgId: String, lang: Language) =
    firestoreCollection
        .whereEqualTo("tg_id", tgId).limit(1)
        .get().get()
        .documents.firstOrNull()
        ?.id?.let {
            firestoreCollection
                .document(it)
                .update(mapOf("lang" to lang.toString()))
                .get()
        }

fun addNewUserAsync(telegramId: Long, email: String) =
    firestoreCollection.add(
        mapOf(
            "id" to email,
            "tg_id" to telegramId.toString(),
            "lang" to "en"
        )
    )

fun sendLoginEmail(name: String, email: String): Int {
    val env = System.getenv()

    val userName = env["EMAIL_AUTH"]!!
    val password = env["EMAIL_AUTH_PASSWORD"]!!

    val emailFrom = env["EMAIL_AUTH"]!!

    val subject = "Email confirmation for InnoBookingBot"

    val authCode = Random.nextInt(100..1000000000)

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
        setContent(EmailPage(name, authCode), "text/html")
        this.subject = subject
        sentDate = Date()
    }

    Transport.send(mimeMessage)

    return authCode
}