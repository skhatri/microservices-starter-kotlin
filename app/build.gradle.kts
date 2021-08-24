import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.5.3"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    id("org.sonarqube") version "2.8"
    id("jacoco")
    kotlin("jvm") version "1.5.21"
    kotlin("plugin.spring") version "1.5.21"
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

configurations {
    implementation {
        resolutionStrategy.failOnVersionConflict()
    }
}

dependencies {
    listOf(
            "spring-boot-starter-webflux",
            "spring-boot-starter-${project.ext["server.type"]}",
            "spring-boot-starter"
    ).forEach { name ->
        implementation("org.springframework.boot:${name}") {
            exclude(module = "spring-boot-starter-logging")
        }
    }

    if (project.ext["server.type"] == "reactor-netty") {
        implementation("io.netty:netty-tcnative-boringssl-static:2.0.29.Final")
    }

    if (project.ext["server.type"] == "jetty") {
        listOf("jetty-alpn-server", "jetty-alpn-conscrypt-server").forEach { name ->
            implementation("org.eclipse.jetty:$name:9.4.27.v20200227")
        }
        implementation("org.eclipse.jetty.http2:http2-server:9.4.27.v20200227")
    }

    implementation("org.springframework.boot:spring-boot-starter-log4j2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:1.5.1")
    implementation("io.projectreactor.addons:reactor-adapter:3.3.2.RELEASE")
    implementation("org.yaml:snakeyaml:1.26")
    implementation("io.r2dbc:r2dbc-spi:0.8.5.RELEASE")
    implementation("io.r2dbc:r2dbc-postgresql:0.8.5.RELEASE")
    implementation("io.r2dbc:r2dbc-h2:0.8.1.RELEASE")
    implementation("org.springframework.data:spring-data-r2dbc:1.3.4")
    implementation("io.github.skhatri:mounted-secrets-client:0.2.5")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "junit")
        exclude(module = "mockito-core")
        exclude(module = "spring-boot-starter-logging")
    }
    testImplementation("io.projectreactor:reactor-test:3.3.2.RELEASE")
    testImplementation("org.mockito:mockito-core:3.3.3")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.1")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.7.1")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:5.7.1")

    testImplementation("org.junit.platform:junit-platform-commons:1.7.1")
    testImplementation("org.junit.platform:junit-platform-runner:1.7.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.7.1")
    testRuntimeOnly("org.junit.platform:junit-platform-engine:1.7.1")
    testImplementation("org.testcontainers:testcontainers:1.16.0")
    testImplementation("org.testcontainers:junit-jupiter:1.16.0")
    testImplementation("org.testcontainers:postgresql:1.16.0")

}

tasks.test {
    useJUnitPlatform()
}

sonarqube {
    properties {
        property("sonar.projectName", "microservices-starter-kotlin")
        property("sonar.host.url", "http://localhost:9000")
        property("sonar.projectKey", "microservices-starter-kotlin-app")
        property("sonar.projectVersion", "${project.version}")
        property("sonar.junit.reportPaths", "${projectDir}/build/test-results/test")
        property("sonar.coverage.jacoco.xmlReportPaths", "${projectDir}/build/reports/jacoco/test/jacocoTestReport.xml")
        property("sonar.coverage.exclusions", "**/R.kt")
        property("sonar.language", "kotlin")
    }
}

apply(from = "$rootDir/gradle/includes/codestyle.gradle.kts")
tasks.build {
    dependsOn(arrayOf("checkstyleMain", "checkstyleTest"))
}

tasks.jacocoTestReport {
    reports {
        xml.isEnabled = true
        csv.isEnabled = false
        html.destination = file("${buildDir}/jacocoHtml")
    }
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            enabled = true
            limit {
                minimum = "0.2".toBigDecimal()
            }
        }

        rule {
            enabled = false
            element = "BUNDLE"
            includes = listOf("com.github.starter.*")
            excludes = listOf("**/Application*")
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.1".toBigDecimal()
            }
        }
    }
}

tasks.test {
    extensions.configure(JacocoTaskExtension::class) {
        destinationFile = file("$buildDir/jacoco/jacocoTest.exec")
        classDumpDir = file("$buildDir/jacoco/classpathdumps")
    }
}

tasks.test {
    finalizedBy("jacocoTestReport")
}

tasks.check {
    dependsOn(arrayOf("jacocoTestReport", "jacocoTestCoverageVerification"))
}

task("runApp", JavaExec::class) {
    main = "com.github.starter.ApplicationKt"
    classpath = sourceSets["main"].runtimeClasspath
    jvmArgs = listOf(
            "-Xms512m", "-Xmx512m"
    )
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}
