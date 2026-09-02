# Space Client Launcher

The launcher is a lightweight Java 21 desktop shell built for a clean, animated first screen.

## Current first pass

- Near-black, star-only background
- Slow continuous star movement with subtle twinkle
- 120 FPS render target
- No blue blobs, glow clouds, skins, or launcher controls yet
- Promotional white text appears every 30 seconds and floats upward before fading out
- Press `Esc` to close the launcher

## Run

From the repository root with JDK 21 and Gradle installed:

```text
gradle :launcher:run
```

The launcher is intentionally kept dependency-free so the visual layer stays lightweight and easy to tune.
