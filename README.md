# Tilt Ball Maze

An Android tilt-controlled ball maze game built with Kotlin. Navigate the ball through obstacles and reach the goal hole.

## Features

- Tilt controls (accelerometer-based)
- Multiple levels with obstacles (moving + rotating)
- Sound effects (collision / win / explosion)
- Win overlay with fireworks
- Settings screen

## Tech stack

- Kotlin
- Android Views + ViewBinding (game UI/layouts)
- Jetpack Compose (Material 3 used for some UI/theme)
- Gradle Kotlin DSL

## Requirements

- Android Studio (recommended)
- Android SDK
- **minSdk**: 24

## Build & run

### Android Studio

1. Open this folder in Android Studio
2. Let Gradle sync
3. Run the `app` configuration on an emulator or physical device

### Command line

Build debug APK:

```bash
./gradlew assembleDebug
```

Install on a connected device (ADB required):

```bash
./gradlew installDebug
```

## How to play

- **Tilt your phone** to roll the ball
- Avoid obstacles and walls
- Reach the **hole** to win the level

## Project structure (high level)

- `app/src/main/java/com/yourcompany/tiltballmaze/game/` — core game logic (loop, models, sensors, audio)
- `app/src/main/java/com/yourcompany/tiltballmaze/ui/` — menus, level select, settings
- `app/src/main/res/` — layouts, animations, drawables, sounds

## Package / app id

- `com.densappstudio.tiltballmaze`

## License

No license specified yet.
