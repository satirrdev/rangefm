<div align="center">
  <img src="graphics/icon.webp" width="96" alt="Range FM Icon" />
  <h1>Range FM</h1>
  <p>Android file manager with Material 3 Expressive and dynamic wallpaper theming.</p>

  <p>
    <img src="https://img.shields.io/badge/Platform-Android_8.0+_(API_26+)-3DDC84?logo=android&logoColor=white" alt="Platform" />
    <img src="https://img.shields.io/badge/Target_SDK-36-blue" alt="Target SDK" />
    <img src="https://img.shields.io/badge/License-GPL_v3-blue.svg" alt="License: GPL v3" />
    <img src="https://img.shields.io/badge/PR-Open_(#2)-orange" alt="PR #2" />
  </p>
</div>

Fork of Fossify File Manager, redesigned for modern Android using Material 3 Expressive components and Jetpack Compose.

## Screenshots

<!-- Add device screenshots here -->
| File List & Search | Selection Toolbar | Dark Mode |
|---|---|---|
| *(Screenshot placeholder)* | *(Screenshot placeholder)* | *(Screenshot placeholder)* |

## Features

### Material 3 Expressive UI
- **Dynamic Color (Monet)**: Theme colors adapt to wallpaper palette on Android 12+ devices.
- **Tonal Surfaces**: Surface container roles replace elevation drop shadows.
- **Expressive Search**: Pill-shaped search bar with 28dp corners and container high tonal fill.
- **Floating Toolbar**: Asymmetric bottom selection bar with actions and dropdown menu.
- **Morphing FAB**: Adaptive floating action button with spring physics transitions.

### Core File Operations
- **Storage Access**: Manage internal storage, SD cards, USB OTG, and root directories.
- **File Manipulation**: Search, copy, move, rename, compress (ZIP), and inspect file properties.
- **Security Lock**: Protect directories with fingerprint biometric, PIN, or pattern.
- **Text Editor**: Built-in file editor with zoom gestures.
- **Zero Internet Access**: No `android.permission.INTERNET`. All operations remain offline.

## Architecture

Hybrid architecture migrating from legacy Views to Jetpack Compose:
- **`:app`**: Application entry point, navigation fragments, file adapters (`com.satirr.range`).
- **`:commons`**: Shared library containing M3 Expressive theme tokens, Compose components, and base dialogs (`com.satirr.commons`).

## Building

Prerequisites: JDK 17, Android SDK with compile/target API 36.

```bash
# Debug APK
./gradlew :app:assembleCoreDebug

# Code quality checks
./gradlew :app:lintDebug
./gradlew detekt
```

Output APK path: `app/build/outputs/apk/core/debug/rangefm-4-core-debug.apk`.

CI workflow runs on GitHub Actions for pushes and tags matching `v*`.

## Versioning

Scheme documented in project guidelines:
- `1.x.x`: Major architectural changes.
- `2.x.x`: Semi-minor release (3+ features).
- `3.x.x`: Minor release (1-2 features or bug fixes).
- `a`: Hotfix suffix.

## License

GNU General Public License v3.0. See [LICENSE](LICENSE) for terms.
