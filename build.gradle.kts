import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.21"
    id("io.papermc.paperweight.userdev") version "1.4.0"
}

group = "io.github.faiz"
version = "0.0.1"

repositories {
    mavenCentral()
    maven(url = uri("https://repo.ranull.com/maven/external/"))
}

dependencies {
    paperDevBundle("1.19.2-R0.1-SNAPSHOT")
    compileOnly("dev.sergiferry:playernpc:2023.1")
    testImplementation(kotlin("test"))
    implementation("io.github.monun:invfx-api:3.2.0")
}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}