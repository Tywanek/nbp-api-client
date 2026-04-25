# NBP API Client (Kotlin Multiplatform)

![Kotlin](https://img.shields.io/badge/kotlin-2.1.0-blue.svg?logo=kotlin)
![JitPack](https://img.shields.io/jitpack/v/github/tywanek/nbp-api-client?color=green)
![License](https://img.shields.io/badge/license-MIT-lightgrey.svg)

A lightweight Kotlin Multiplatform (KMP) library for fetching currency exchange rates from the National Bank of Poland (NBP). Built with **Ktor** and **Kotlinx Serialization**.

## 🚀 Supported Platforms
- **Android** (SDK 24+)
- **iOS** (Arm64, x64, Simulator)
- **JVM** (Desktop)

## 📦 Installation

### 1. Add the JitPack repository
In your `settings.gradle.kts` file:

```kotlin
dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven { url = uri("[https://jitpack.io](https://jitpack.io)") }
    }
}
```

### 2. Add the dependency
In your shared module's `build.gradle.kts` (usually `commonMain`):

```kotlin
sourceSets {
    commonMain.dependencies {
        implementation("com.github.tywanek:nbp-api-client:1.0.0")
    }
}
```

## 🛠 Quick Start

The library provides an easy-to-use client to communicate with the NBP API. Usage in `commonMain`:

```kotlin
// Example usage
val client = NbpApiClient() 

suspend fun fetchRates() {
    try {
        val result = client.getExchangeRates()
        println("Current rates: $result")
    } catch (e: Exception) {
        println("Error fetching data: ${e.message}")
    }
}
```

## 🏗 Project Structure

* `/nbp-api-client` — **Core KMP Library** (the actual library).
* `/composeApp` — Demo application showing how to use the library in Compose Multiplatform.
* `/iosApp` — Xcode project for the iOS demo application.

## 🔨 Development & Testing

Run all tests across all platforms:
```bash
./gradlew :nbp-api-client:allTests
```

---
*Created by [tywanek](https://github.com/tywanek). Powered by Kotlin Multiplatform.*
```