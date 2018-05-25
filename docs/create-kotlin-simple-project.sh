#!/bin/bash

PKG=com.cmartin.learn
PKG_DIR=com/cmartin/learn
KOTLIN_VERSION=1.2.41
JUNIT5_VERSION=5.2.0
JUNIT_PLATFORM_VERSION=1.2.0
ASSERTJ_VERSION=3.9.1

# create filesystem
mkdir -p project src/{main,test}/{resources,kotlin} src/{main,test}/kotlin/${PKG_DIR}

echo 'buildscript {
    ext.kotlin_version = "'${KOTLIN_VERSION}'"
    ext.junit_version = "'${JUNIT5_VERSION}'"
    ext.junit_platform_version = "'${JUNIT_PLATFORM_VERSION}'"
    ext.assertj_version = "'${ASSERTJ_VERSION}'"
    
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:${kotlin_version}"
        //classpath "org.junit.platform:junit-platform-gradle-plugin:${junit_platform_version}"
    }
}

apply plugin: "kotlin"
apply plugin: "application"
//apply plugin: "org.junit.platform.gradle.plugin"
apply plugin: "jacoco"

mainClassName = "'${PKG}'.HelloKt"

repositories {
    mavenCentral()
}

dependencies {
    compile "org.jetbrains.kotlin:kotlin-stdlib-jdk8",
            "org.jetbrains.kotlin:kotlin-reflect"

    testCompile "org.junit.jupiter:junit-jupiter-api:${junit_version}",
            "org.assertj:assertj-core:$assertj_version"

    testRuntime "org.junit.jupiter:junit-jupiter-engine:${junit_version}"
}

test {
    useJUnitPlatform()
}
' > build.gradle

echo 'package '${PKG}'

fun main(args: Array<String>) {
    println("Hello, Kotlin World!")
}
' > src/main/kotlin/${PKG_DIR}/hello.kt

gradle wrapper --gradle-version 4.7
rm -f gradlew.bat
./gradlew -v
./gradlew clean test run