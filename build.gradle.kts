plugins {
    application
    kotlin("jvm") version "1.8.21"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.21"
}

group = "com.paranid5"
version = "0.0.0.1"

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
    google()
    jcenter()
}

dependencies {
    implementation("io.arrow-kt:arrow-fx-coroutines:1.2.0-RC")

    implementation("io.ktor:ktor-client-core:2.3.1")
    implementation("io.ktor:ktor-client-logging:2.3.1")
    implementation("io.ktor:ktor-client-cio-jvm:2.3.1")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.1")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.1")

    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")

    implementation("io.github.kotlin-telegram-bot.kotlin-telegram-bot:telegram:6.1.0")
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")

    implementation("com.google.firebase:firebase-admin:9.2.0")
    implementation("com.sun.mail:javax.mail:1.6.2")

    testImplementation("org.mockito:mockito-inline:5.2.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.2.0")
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("MainKt")
}