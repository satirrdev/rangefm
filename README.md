# Range FM

Range FM is a fast, ad-free, open-source file manager for Android, developed at `satirrdev/rangefm`.

<img alt="Logo" src="graphics/icon.webp" width="120" />

- Swiftly manage your files with compression, transfer, and search.
- Access frequently used folders with a customizable home folder and favorites.
- Secure sensitive files with password, pattern, or fingerprint locks.
- No internet access. Your files stay on your device.
- Navigate root files, SD cards, and USB devices.
- Light file editor with zoom gestures.

## Building

Build the APK locally:

```bash
./gradlew :app:assembleCoreDebug
```

The debug APK is output to `app/build/outputs/apk/core/debug/`. It is signed with the debug keystore and can be installed directly.

GitHub Actions builds every push to `main` and on manual dispatch. Tag a release with `v*` (e.g., `v1.1.3`) to create a GitHub Release with the installable APK. Continuous integration is defined in `.github/workflows/android-ci.yml` and `.github/workflows/build.yml`.

## License

```
Copyright (C) 2023 FossifyOrg — this project is a fork of Fossify File Manager.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.
```

See [LICENSE](LICENSE) for the full text.

## Version Control

Custom scheme at `AGENTS.md`:
- `1.x.x` → Major change (mostly changing/modifying all of the file)
- `2.x.x` → Semi-minor change (adding 3+ features)
- `3.x.x` → Minor change (1-2 features or bug fixes)
- `a` → Minor bug fixing