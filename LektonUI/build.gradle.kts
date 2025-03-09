plugins {
    kotlin("jvm")
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "org.itmo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

application {
    mainClass.set("org.itmo.MainKt")
}

dependencies {
    implementation(project(":LektonCore"))
    testImplementation(kotlin("test"))
}

tasks {
    shadowJar {
        archiveFileName.set("LektonUI-all.jar")
        mergeServiceFiles()
    }

    jar {
        manifest {
            attributes["Main-Class"] = "org.itmo.console.MainKt"
        }
    }

    build {
        dependsOn("shadowJar")
    }

    test {
        useJUnitPlatform()
    }
}

kotlin {
    jvmToolchain(23)
}