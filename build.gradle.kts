import org.apache.tools.ant.filters.ReplaceTokens
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.23"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "ru.enwulf"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    implementation(kotlin("stdlib"))
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        sourceCompatibility = JavaVersion.VERSION_17.toString()
        targetCompatibility = JavaVersion.VERSION_17.toString()
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }


    processResources {
        filteringCharset = "UTF-8"
        filesMatching("**/plugin.yml") {
            filter<ReplaceTokens>(
                "tokens" to mapOf(
                    "name" to project.name,
                    "version" to project.version,
                )
            )
        }
    }


    shadowJar {
        archiveFileName.set("${project.name}-${project.version}.jar")

        val libs = "libs"
        relocate("org.jetbrains", "$libs.jetbrains")
        relocate("kotlin", "$libs.kotlin")

        dependencies {
            exclude("META-INF/**")
        }

        minimize()
    }

    build {
        dependsOn(shadowJar)
    }
}



/*
tasks.register<Copy>("moveToPluginsDir") {
    from(tasks.shadowJar.get().archiveFile.get().asFile)
    //from("C:/Users/raizz/Desktop/DamagerENW/build/libs/DamagerENW-1.0.jar")

    val userName = System.getProperty("user.name")
    val pluginsDir = file("C:/Users/$userName/Desktop/server1.20.4/plugins")
    pluginsDir.mkdirs()

    into(pluginsDir)
}

tasks.named("build") { finalizedBy("moveToPluginsDir") }
*/
