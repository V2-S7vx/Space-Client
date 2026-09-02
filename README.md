# Space Client

The first Space Client milestone is a Windows launcher and installer foundation.

## Stack

- Java 21 + Gradle
- Swing for the dependency-free launcher UI
- `jpackage` for a self-contained Windows app image
- Inno Setup for the final Windows installer
- GitHub Actions for repeatable Windows builds

## Launcher

The launcher uses a pure black background with small white stars. The first milestone intentionally leaves the Minecraft account/skin area out so the launcher foundation can be built first.

## Windows installer

The installer is named **Space Client** and provides two optional shortcut choices:

- Create a desktop icon
- Add Space Client to the Windows Start Menu/search

The second option creates a Start Menu shortcut, which allows Windows Search to discover the installed application.

## Build

On Windows with JDK 21 and Gradle installed:

```text
gradle clean jar
```

The GitHub Actions workflow performs the full Windows packaging and publishes `SpaceClient-Setup.exe` as a workflow artifact.
