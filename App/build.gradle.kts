plugins {
    kotlin("jvm") version "2.2.0"
    id("org.jetbrains.intellij") version "1.15.0"
    id("java")
}

group = "dk.project"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

intellij {
    version.set("2025.2.1")
    type.set("IC")
    plugins.set(listOf())
}

tasks {
    patchPluginXml {
        changeNotes.set("Initial release of App Keyboard Sound plugin.")
    }

    runIde {
        jvmArgs = listOf("-Xmx1024m")
    }

    test {
        useJUnitPlatform()
    }
}