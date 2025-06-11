# React + KotlinJS Demo

This is a simple demo application that shows how to integrate KotlinJS with a React TypeScript application.

## Project Structure

- `/src` - React TypeScript source code
- `/kotlin` - Kotlin source code that compiles to JavaScript

## Setup

1. Install dependencies:

```bash
npm install
```

2. Build Kotlin code:

```bash
cd kotlin
./gradlew build
```

3. Start the development server:

```bash
npm run dev
```

## How it works

The application demonstrates a simple integration between React and KotlinJS:

- The Kotlin code provides a simple greeting function
- The React app uses this function when a button is clicked
- The result is displayed on the screen

## Development

- React code is in `/src`
- Kotlin code is in `/kotlin/src/main/kotlin`
- After modifying Kotlin code, rebuild it using `./gradlew build` in the kotlin directory
