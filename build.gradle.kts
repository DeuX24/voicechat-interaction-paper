plugins {
    java
}

group = "dev.igalaxy.voicechatinteraction"
description = "Detect voice chat with the sculk sensor"

// The build number is the number of commits, so every commit gets its own version: 26.2-<commits>.
val commitCount = providers.exec {
    commandLine("git", "rev-list", "--count", "HEAD")
    isIgnoreExitValue = true
}.standardOutput.asText.map { it.trim().ifEmpty { "0" } }.getOrElse("0")
version = "26.2-$commitCount"

java {
    toolchain.languageVersion = JavaLanguageVersion.of(25)
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://maven.maxhenkel.de/repository/public")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:26.2.build.129-stable")
    // Provided at runtime by the Simple Voice Chat plugin.
    compileOnly("de.maxhenkel.voicechat:voicechat-api:2.6.24")
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name()
        val props = mapOf(
            "version" to project.version,
            "description" to project.description,
        )
        inputs.properties(props)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
}
