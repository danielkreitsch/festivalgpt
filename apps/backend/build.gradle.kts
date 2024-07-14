import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  id("org.springframework.boot") version "3.3.1"
  id("io.spring.dependency-management") version "1.1.5"
  kotlin("jvm") version "1.9.24"
  kotlin("plugin.jpa") version "1.9.24"
  kotlin("plugin.spring") version "1.9.24"
  id("maven-publish")
  id("com.diffplug.spotless") version "6.8.0"
}

group = "de.festivalgpt"

version = "0.0.1-SNAPSHOT"

java { sourceCompatibility = JavaVersion.VERSION_17 }

repositories {
  mavenCentral()
  maven { url = uri("https://repo.spring.io/milestone") }
}

extra["springAiVersion"] = "1.0.0-M1"

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-security")
  implementation("org.springframework.boot:spring-boot-starter-validation")
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation("org.springframework.ai:spring-ai-qdrant-store-spring-boot-starter")
  implementation("org.springframework.ai:spring-ai-transformers-spring-boot-starter")
  runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
  annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
  testImplementation("org.springframework.security:spring-security-test")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
  imports { mavenBom("org.springframework.ai:spring-ai-bom:${property("springAiVersion")}") }
}

kotlin {
  compilerOptions {
    freeCompilerArgs.addAll("-Xjsr305=strict")
    jvmTarget.set(JvmTarget.JVM_17)
  }
}

tasks {
  withType<Test> { useJUnitPlatform() }
  jar { enabled = false }
  named("build") { finalizedBy("copyFiles") }
  register<Copy>("copyFiles") {
    from("Dockerfile")
    from("build/libs")
    into("../../dist/apps/backend")
  }
}

tasks.withType<Test> { useJUnitPlatform() }

springBoot {
  buildInfo()
  mainClass = "de.festivalgpt.backend.ApplicationKt"
}

publishing {
  publications { create<MavenPublication>("mavenJava") { artifact(tasks.getByName("bootJar")) } }
}

configure<com.diffplug.gradle.spotless.SpotlessExtension> {
  format("misc") {
    target("*.gradle.kts")
    trimTrailingWhitespace()
    indentWithSpaces()
    endWithNewline()
  }
  kotlin { ktfmt() }
  kotlinGradle {
    target("*.gradle.kts")
    ktfmt()
  }
}
