import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "org.liamjd.bascule"
version = "0.5.3"

plugins {
    alias(libs.plugins.kotlin.jvm)
    `maven-publish`
    alias(libs.plugins.shadow)
    alias(libs.plugins.kotlin.serialization)
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    // reflection
    api(libs.kotlin.reflect)

    // snakeyaml
    implementation(libs.snakeyaml)

    // markdown
    implementation(libs.flexmark.all)

    // Testing
    testImplementation(libs.kotlin.test.junit5)
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testImplementation(libs.mockk)

    // coroutines
    testImplementation(libs.kotlinx.coroutines.core)}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        showStandardStreams = true
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}
tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "21"
    }
}

configurations.implementation {
    exclude(group = " org.jetbrains.kotlin", module = "kotlin-stdlib")
}

tasks.shadowJar {
    archiveClassifier.set("")
    minimize()
}

configurations {
}

// publishing
publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            groupId = "org.liamjd.bascule"
            artifactId = "bascule-lib"
        }
    }
    repositories {
       maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/v79/bascule-lib")
            credentials {
                username = (project.findProperty("gpr.user") ?: System.getenv("GH_USERNAME")) as String?
                password = (project.findProperty("gpr.key") ?: System.getenv("GH_PAT_REPO")) as String?
            }
        }
    }
}
