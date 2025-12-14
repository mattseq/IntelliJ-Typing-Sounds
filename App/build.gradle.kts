plugins {
    kotlin("jvm") version "2.2.0"
    id("org.jetbrains.intellij") version "1.17.3"
    id("java")
}

group = "dk.project"
version = "1.3.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

intellij {
    version.set("2025.2.1") // version.set("2024.2.2")
    type.set("IC")
    plugins.set(listOf("java"))
    sandboxDir.set(file("${project.buildDir}/idea-sandbox").absolutePath)
}

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }

    withType<JavaCompile>().configureEach {
        sourceCompatibility = JavaVersion.VERSION_17.toString()
        targetCompatibility = JavaVersion.VERSION_17.toString()
    }

    patchPluginXml {
        sinceBuild.set("241")
        changeNotes.set("Initial release of App Keyboard Sound plugin.")
    }

    buildSearchableOptions {
        enabled = false
    }

    runIde {
        jvmArgs = listOf("-Xmx1024m", "-Didea.is.internal=true")
        systemProperty("idea.platform.prefix", "Idea")
    }

    test {
        useJUnitPlatform()
    }
}