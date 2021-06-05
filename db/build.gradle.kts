import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    enabled = false
}

tasks.getByName<Jar>("jar") {
    enabled = true
}

plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("plugin.spring")
    id("org.jetbrains.kotlin.plugin.jpa") version "1.5.10"
    kotlin("kapt")
    idea
}

sourceSets["main"].withConvention(org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet::class) {
    kotlin.srcDir("$buildDir/generated/source/kapt/main")
}

dependencies {
    api("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.h2database:h2")
}

dependencies {
    implementation("com.querydsl:querydsl-jpa:4.4.0") // querydsl
    implementation("com.querydsl:querydsl-apt:4.4.0") // querydsl
    kapt("com.querydsl:querydsl-apt:4.4.0:jpa")
}

idea {
    module {
        val kaptMain = file("build/generated/source/kapt/main")
        sourceDirs.add(kaptMain)
        generatedSourceDirs.add(kaptMain)
    }
}
