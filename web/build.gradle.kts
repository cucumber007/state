plugins {
    kotlin("multiplatform") version "2.2.0"
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    js(IR) {
        browser {
            commonWebpackConfig {
                cssSupport {
                    enabled.set(true)
                }
            }
        }
        binaries.executable()
    }
    
    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react:2025.8.0-19.1.1")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:2025.8.0-19.1.1")
            }
        }
    }
} 
