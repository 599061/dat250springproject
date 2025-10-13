plugins {
    id("org.springframework.boot") version "3.5.5"
    id("io.spring.dependency-management") version "1.1.7"
    java
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web:3.5.5")
    implementation("org.springframework.boot:spring-boot-starter-validation:3.5.5")
    implementation("org.springframework.boot:spring-boot-starter-amqp:3.1.4")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.4.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.3")
    implementation("org.hibernate.orm:hibernate-core:7.1.1.Final")
    implementation("jakarta.persistence:jakarta.persistence-api:3.2.0")
    runtimeOnly("com.h2database:h2:2.3.232")
//    implementation("redis.clients:jedis:6.2.0")
    testImplementation(platform("org.junit:junit-bom:5.11.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.google.guava:guava:33.2.1-jre")

    testImplementation("org.springframework.boot:spring-boot-starter-test:3.5.5")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

sourceSets {
    named("main") {
        java {
            exclude("**/redis/**")
        }
    }
}

//sourceSets {
//    val main by getting {
//        java {
//            // keep ONLY the bits you need for the JPA lab
//            // exclude Spring MVC, controllers, DTOs, app bootstrap, etc.
//            exclude(
//                "com/example/dat250demo/pollapp/web/**",
//                "com/example/dat250demo/pollapp/service/**",
//                "com/example/dat250demo/pollapp/dto/**",
//                "com/example/dat250demo/pollapp/Dat250demoApplication.java",
//                // if these “domains” are your Spring-layer models, exclude them too:
//                "com/example/dat250demo/pollapp/domains/**"
//            )
//        }
//    }
//}
