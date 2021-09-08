plugins {
    kotlin("jvm") version "1.5.20"
    kotlin("plugin.allopen") version "1.5.20"
    id("com.expediagroup.graphql") version "4.1.1"
    id("io.quarkus")
}

repositories {
    mavenCentral()
    mavenLocal()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

dependencies {
    // Platform / framework deps
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    implementation("io.quarkus:quarkus-arc")
    implementation("io.quarkus:quarkus-resteasy")
    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")
    implementation("io.quarkus:quarkus-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.5.20")
    implementation("io.ktor:ktor:1.5.3")
    implementation("io.ktor:ktor-http:1.5.3")
    implementation("io.ktor:ktor-client-websockets:1.5.3")
    implementation("io.ktor:ktor-features:1.5.3")
    implementation("io.ktor:ktor-server:1.5.3")
    implementation("io.ktor:ktor-client-okhttp:1.5.3")
    implementation( "io.ktor:ktor-server-core:1.5.3")
    implementation( "io.ktor:ktor-server-core:1.5.3")
    implementation("io.ktor:ktor-server-netty:1.5.3")
    implementation("io.ktor:ktor-websockets:1.5.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.20")
    implementation("com.expediagroup:graphql-kotlin-maven-plugin:5.0.0-alpha.4")
    implementation("com.expediagroup", "graphql-kotlin-schema-generator", "5.0.0-alpha.4")
    implementation("com.expediagroup", "graphql-kotlin-spring-server", "5.0.0-alpha.4")
    implementation("com.expediagroup", "graphql-kotlin-server", "5.0.0-alpha.4")

    // Our when2meat deps


}

group = "me.alanp"
version = "2.2.2.Final"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}

allOpen {
    annotation("javax.ws.rs.Path")
    annotation("javax.enterprise.context.ApplicationScoped")
    annotation("io.quarkus.test.junit.QuarkusTest")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
    kotlinOptions.javaParameters = true
}
