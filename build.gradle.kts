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
}

kotlin {
    jvmToolchain(19)
}

val mainClassTitle = "com.paranid5.innobookingbot.MainKt"

application {
    mainClass.set(mainClassTitle)
}

tasks.wrapper {
    gradleVersion = "8.2"
    distributionType = Wrapper.DistributionType.ALL
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    manifest {
        attributes["Main-Class"] = mainClassTitle
    }

    configurations["compileClasspath"].forEach { from(zipTree(it.absoluteFile)) }
    exclude(listOf("META-INF/*.RSA", "META-INF/*.SF", "META-INF/*.DSA"))
}

val buildFatJar = task("buildFatJar", type = Jar::class) {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    archiveBaseName.set("${project.name}-fat")

    manifest {
        attributes["Implementation-Title"] = "Gradle Jar File Example"
        attributes["Implementation-Version"] = archiveVersion
        attributes["Main-Class"] = mainClassTitle
    }

    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    exclude(listOf("META-INF/*.RSA", "META-INF/*.SF", "META-INF/*.DSA"))
    with(tasks.jar.get() as CopySpec)
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "19"
    }

    "build" { dependsOn(buildFatJar) }
}