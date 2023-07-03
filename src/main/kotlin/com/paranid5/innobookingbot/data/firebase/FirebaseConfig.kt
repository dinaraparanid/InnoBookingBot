package com.paranid5.innobookingbot.data.firebase

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import io.github.cdimascio.dotenv.dotenv
import java.io.FileInputStream

fun configureFirebase() {
    val env = dotenv()
    val credentialsPath = env["CREDENTIALS_PATH"]
    val databaseUrl = env["DATABASE_URL"]
    val projectId = env["PROJECT_ID"]

    FirebaseApp.initializeApp(
        FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(FileInputStream(credentialsPath)))
            .setProjectId(projectId)
            .setDatabaseUrl(databaseUrl)
            .build()
    )
}