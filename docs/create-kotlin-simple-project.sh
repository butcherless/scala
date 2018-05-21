#!/bin/bash

PKG_DIR=com/cmartin/learn
KOTLIN_VERSION=1.2.41
JUNIT5_VERSION=5.2.0
JUNIT_PLATFORM_VERSION=1.2.0
ASSERTJ_VERSION=3.9.1

# create filesystem
mkdir -p project src/{main,test}/{resources,scala} src/main/scala/${PKG_DIR} src/test/scala/${PKG_DIR}

echo '
buildscript {
    ext.kotlin_version = "'${KOTLIN_VERSION}'"
    ext.junit_version = "'${JUNIT5_VERSION}'"
    ext.junit_platform_version = "'${JUNIT_PLATFORM_VERSION}'"
    ext.assertj_version = "'${ASSERTJ_VERSION}'"
    
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:${kotlin_version}"
        classpath "org.junit.platform:junit-platform-gradle-plugin:${junit_platform_version}"
    }
}

apply plugin: "kotlin"
apply plugin: "org.junit.platform.gradle.plugin"
apply plugin: "jacoco"

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
' > build.gradle

gradle wrapper --gradle-version 4.7
rm -f gradlew.bat
./gradlew -v