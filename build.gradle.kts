plugins {
    application
    kotlin("jvm") version "1.8.10"
    id("com.palantir.graal") version "0.12.0"
    id("org.jlleitschuh.gradle.ktlint") version "11.2.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.aliyun.com/repository/public")
    maven("https://maven.aliyun.com/repository/google")
}

dependencies {
    implementation("com.alibaba.fastjson2:fastjson2-kotlin:2.0.23")
    implementation("cn.hutool:hutool-core:5.8.12")

    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
    implementation("ch.qos.logback:logback-classic:1.4.5")
    implementation("org.slf4j:slf4j-api:2.0.6")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("com.example.MainKt")
}

graal {
    graalVersion("22.3.1")
    javaVersion("17")
    mainClass(application.mainClass.get())
    outputName("native-simple")
    option("--verbose")
    option("--no-fallback")
    option("-H:+ReportExceptionStackTraces")
    option("-H:+PrintClassInitialization")
    option("-H:ReflectionConfigurationFiles=src/graal/reflect-config.json")
}

tasks.jar.configure {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    manifest.attributes["Main-Class"] = application.mainClass.get()
    from(configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) })
}