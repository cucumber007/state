plugins {
    kotlin("multiplatform") version "1.9.22"
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
}

kotlin {
    js {
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
        jsMain {
            dependencies {
                implementation(kotlin("stdlib-js"))
                implementation("org.jetbrains.kotlinx:kotlinx-html-js:0.9.1")
                implementation(npm("react", "^19.1.0"))
                implementation(npm("react-dom", "^19.1.0"))
            }
        }
    }
} 
