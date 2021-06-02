plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("plugin.spring")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation(project(":service"))
}
