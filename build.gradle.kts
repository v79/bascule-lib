import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "org.liamjd.bascule"
version = "0.3.2"

val kotlin_version = "1.6.21"
val snakeyaml_version = "1.23"
val mockk_version = "1.12.4"
val flexmark_version = "0.61.0"
val slf4j_version = "1.7.26"
val coroutines_version = "1.6.2"
val spek_version = "2.0.18"

plugins {
    kotlin("jvm") version "1.6.21"
    `maven-publish`
    id("com.gradleup.shadow") version "8.3.8"
    kotlin("plugin.serialization") version "1.6.21"
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    // stdlib
    implementation(kotlin("stdlib", "1.6.21"))
    // reflection
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlin_version")

    // snakeyaml
    implementation("org.yaml:snakeyaml:$snakeyaml_version")

    // markdown - probably want to be more selective with this!
    implementation("com.vladsch.flexmark:flexmark-all:$flexmark_version")

    // testing
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.2")
    ("org.junit.jupiter:junit-jupiter-params:5.3.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.3.2")
    testImplementation("org.spekframework.spek2:spek-dsl-jvm:$spek_version")
    testRuntimeOnly("org.spekframework.spek2:spek-runner-junit5:$spek_version")
    testImplementation("io.mockk:mockk:$mockk_version")
    // coroutines
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")
}

tasks.test {
    useJUnitPlatform {
        includeEngines("spek2")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
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
       /* maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/v79/bascule-lib")
            credentials {
                username = (project.findProperty("gpr.user") ?: System.getenv("GH_USERNAME")) as String?
                password = (project.findProperty("gpr.key") ?: System.getenv("GH_PAT_REPO")) as String?
            }
        }*/
    }
}
