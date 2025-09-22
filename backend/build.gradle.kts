plugins {
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
    implementation("org.hibernate.orm:hibernate-core:7.1.1.Final")
    implementation("jakarta.persistence:jakarta.persistence-api:3.2.0")
    runtimeOnly("com.h2database:h2:2.3.232")

    testImplementation(platform("org.junit:junit-bom:5.11.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

sourceSets {
    val main by getting {
        java {
            // keep ONLY the bits you need for the JPA lab
            // exclude Spring MVC, controllers, DTOs, app bootstrap, etc.
            exclude(
                "com/example/dat250demo/pollapp/web/**",
                "com/example/dat250demo/pollapp/service/**",
                "com/example/dat250demo/pollapp/dto/**",
                "com/example/dat250demo/pollapp/Dat250demoApplication.java",
                // if these “domains” are your Spring-layer models, exclude them too:
                "com/example/dat250demo/pollapp/domains/**"
            )
        }
    }
}
