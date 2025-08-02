# Kotlin Multiplatform JS Example

A simple Kotlin Multiplatform project that generates JavaScript.

## Project Structure

```
├── build.gradle.kts          # Gradle build configuration
├── settings.gradle.kts       # Project settings
├── gradle.properties         # Gradle properties
├── src/
│   └── jsMain/
│       ├── kotlin/
│       │   └── Main.kt      # Main Kotlin source file
│       └── resources/
│           └── index.html    # HTML file to test the JS
└── README.md
```

## How to Build

1. **Build the JavaScript:**

   ```bash
   ./gradlew jsBrowserDevelopmentRun
   ```

2. **Generate JavaScript files:**
   ```bash
   ./gradlew jsBrowserDevelopmentWebpack
   ```

The generated JavaScript files will be in the `build/distributions/` directory.

## What it does

- The Kotlin code in `Main.kt` gets compiled to JavaScript
- It creates a simple web page with a button
- When you click the button, it shows an alert message
- The JavaScript is generated in the `build/distributions/` folder

## Running the Example

After building, you can open `src/jsMain/resources/index.html` in a browser to see the result, or use the development server:

```bash
./gradlew jsBrowserDevelopmentRun
```

This will start a development server and open the page in your browser.
