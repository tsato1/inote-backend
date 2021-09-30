val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    application
    kotlin("jvm") version "1.5.30"
}

group = "com.tsato.server"
version = "0.0.1"
application {
    mainClass.set("com.tsato.server.ApplicationKt")
}

repositories {
    mavenLocal()
    mavenCentral()
    maven(url = "https://kotlin.bintray.com/ktor")
    maven(url = "https://kotlin.bintray.com/kotlin-js-wrappers")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-html-builder:$ktor_version")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-css:1.0.0-pre.236-kotlin-1.5.30")
//    implementation("org.jetbrains:kotlin-css-jvm:1.0.0-pre.31-kotlin-1.2.41")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    implementation("io.ktor:ktor-auth:$ktor_version")
    implementation("io.ktor:ktor-gson:$ktor_version")
    implementation("org.litote.kmongo:kmongo:4.3.0")
    implementation("org.litote.kmongo:kmongo-coroutine:4.3.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
    implementation("commons-codec:commons-codec:1.14")
    implementation("io.ktor:ktor-network-tls:$ktor_version")
    implementation("io.ktor:ktor-freemarker:$ktor_version")
}
