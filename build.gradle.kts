plugins {
    kotlin("jvm") version "2.1.21"
    id("io.ktor.plugin") version "3.2.1"
    application
}

group = "com.example"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("com.example.EngineMainKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-netty-jvm:3.2.1")
    implementation("io.ktor:ktor-server-core-jvm:3.2.1")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:3.2.1")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:3.2.1")
    implementation("ch.qos.logback:logback-classic:1.4.14")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:2.1.21")
    testImplementation("io.ktor:ktor-server-tests-jvm:3.2.1")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:2.1.21")
}
