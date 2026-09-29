plugins {
    java
    id("org.springframework.boot") version "3.5.14"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.openapi.generator") version "7.10.0"
}

group = "com.mukminov"
version = "0.0.1-SNAPSHOT"
description = "sre-mentorflow"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-freemarker")

    implementation("org.springframework.boot:spring-boot-starter-security")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.postgresql:postgresql")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")

    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    implementation("io.swagger.core.v3:swagger-annotations:2.2.25")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

val openApiSpec = "$projectDir/src/main/resources/api.yaml"
val openApiGeneratedDir: String = layout.buildDirectory.dir("generated").get().asFile.absolutePath

openApiGenerate {
    inputSpec.set(openApiSpec)
    outputDir.set(openApiGeneratedDir)
    generatorName.set("spring")
    modelPackage.set("com.mukminov.api.generated.dto")
    apiPackage.set("com.mukminov.api.generated.api")

    configOptions.set(
        mapOf(
            "useJakartaEe" to "true",
            "useSpringBoot3" to "true",
            "library" to "spring-boot",
            "interfaceOnly" to "true",
            "skipDefaultInterface" to "true",
            "useBeanValidation" to "true",
            "useTags" to "true",
            "dateLibrary" to "java8",
            "openApiNullable" to "false",
            "documentationProvider" to "none",
            "useResponseEntity" to "true"
        )
    )
    additionalProperties.set(
        mapOf(
            "generateApiTests" to "false",
            "generateModelTests" to "false",
            "generateApiDocumentation" to "false",
            "generateModelDocumentation" to "false"
        )
    )
}

sourceSets {
    getByName("main") {
        java {
            srcDir(layout.buildDirectory.dir("generated/src/main/java"))
        }
    }
}

tasks.named("compileJava") {
    dependsOn("openApiGenerate")
}
