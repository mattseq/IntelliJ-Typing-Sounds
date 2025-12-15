/*

    Please use IntelliJ version.set("2024.2.2") for features
    and version.set("2025.2.1") for publish.

    Please don't upgrade plugins as they can cause issues for runIde and builds.
    Consult with Guacamoleboy if you want to do so and why.

*/


plugins {
    kotlin("jvm") version "2.2.0"                                                                       // Kotlin Plugin
    id("org.jetbrains.intellij") version "1.17.3"                                                       // IntelliJ Platform Plugin
    id("java")                                                                                          // Java Plugin
}

group = "dk.project"                                                                                    // Group ID
version = "1.4.0"                                                                                       // Version Control

repositories {
    mavenCentral()                                                                                      // Using maven to get dependencies
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))                                          // JUnit
    testImplementation("org.junit.jupiter:junit-jupiter")                                               // JUnit Test Framework
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")                                       // JUnit Test Launcher
}

intellij {
    version.set("2024.2.2") // version.set("2025.2.1") BUILD | version.set("2024.2.2") TEST             // IntelliJ Version | Publish | Test
    type.set("IC")                                                                                      // IntelliJ Community Edition
    plugins.set(listOf("java"))                                                                         // Java IntelliJ Plugin
    sandboxDir.set(file("${project.buildDir}/idea-sandbox").absolutePath)                               // Sandbox location
}

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {                         // Config Kotlin Compiler
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)                             // Java 17
        }
    }

    withType<JavaCompile>().configureEach {                                                             // Config Java Compiler
        sourceCompatibility = JavaVersion.VERSION_17.toString()                                         // Java 17
        targetCompatibility = JavaVersion.VERSION_17.toString()                                         // Java 17
    }

    patchPluginXml {
        sinceBuild.set("241") // sinceBuild.set("252") | sinceBuild.set("241")                          // Least supported version
        changeNotes.set(                                                                                // Change Notes for publish
            """
            <p><b>Version 1.5.0</b></p>
            <ul>
                <li>Custom Audio Settings</li>
                <li>Sound delay fixes</li>
                <li>Bug fixes</li>
                <li>Misc tweaks & Upgrades</li>
            </ul>
            <p>To adjust sound volume go to Settings -> Search -> Typing Sounds</p>
            """.trimIndent()
        )
    }

    buildSearchableOptions {
        enabled = false                                                                                 // False for faster build
    }

    runIde {                                                                                            // .\gradlew runIde Config
        jvmArgs = listOf("-Xmx1024m", "-Didea.is.internal=true")
        systemProperty("idea.platform.prefix", "Idea")
    }

    test {                                                                                              // Test Config
        useJUnitPlatform()                                                                              // JUnit 5
    }
}