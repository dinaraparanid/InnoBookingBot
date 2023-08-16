import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    application
    kotlin("jvm") version "1.9.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.paranid5"
version = "0.1.0.0"

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
    implementation("io.ktor:ktor-server-core-jvm:2.3.1")
    implementation("io.ktor:ktor-server-cors-jvm:2.3.1")
    implementation("io.ktor:ktor-server-compression-jvm:2.3.1")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:2.3.1")
    implementation("io.ktor:ktor-server-netty-jvm:2.3.1")
    implementation("io.ktor:ktor-server-cors:2.3.1")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.1")
    implementation("io.ktor:ktor-network:2.3.1")

    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")

    implementation("io.github.kotlin-telegram-bot.kotlin-telegram-bot:telegram:6.1.0")
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")

    implementation("com.google.firebase:firebase-admin:9.2.0")
    implementation("com.sun.mail:javax.mail:1.6.2")
    implementation("com.charleskorn.kaml:kaml:0.55.0")

    testImplementation("org.mockito:mockito-inline:5.2.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.2.0")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.2")

    testImplementation(kotlin("test"))
    testImplementation("io.rest-assured:rest-assured:5.3.0")
}

kotlin {
    jvmToolchain(17)
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

tasks.withType<ShadowJar> {
    mergeServiceFiles()
    archiveFileName.set("InnoBookingBot.jar")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }

    "build" { dependsOn(buildFatJar) }
}