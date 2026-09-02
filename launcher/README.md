# Space Client Launcher

The launcher is a lightweight Java 21 desktop shell for the Space Client project.

## Current first pass

- Near-black, star-only background
- Slow continuous star movement with subtle twinkle
- 120 FPS render target
- No blue blobs, glow clouds, skins, or launcher controls yet
- Promotional white text appears every 30 seconds and floats upward before fading out
- Press `Esc` to close the launcher

## Windows builds

GitHub Actions builds the Windows launcher automatically on pushes to `main` and `setup/**`, on version tags, or manually with **Run workflow**.

Each successful build publishes:

- `SpaceClient-Setup.exe` — Windows installer
- `SpaceClient-Windows.zip` — ZIP containing the installer and SHA-256 checksum

The installer includes a bundled Java runtime, so users do not need to install Java separately. The installer lets the user choose the install directory and whether to create Start Menu and desktop shortcuts.

## Local development

With JDK 21 and Gradle installed:

```text
gradle :launcher:run
```

The launcher is intentionally dependency-free so the visual layer stays lightweight and easy to tune.
