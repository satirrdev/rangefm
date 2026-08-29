# Range FM

<img alt="Logo" src="graphics/icon.webp" width="120" />

Range FM is a fast, ad-free, open-source file manager for Android, forked from [Fossify File Manager](https://github.com/FossifyOrg/File-Manager).

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

GitHub Actions builds every push to `main` and on manual dispatch. Push a tag like `v1.0.0` to create a GitHub Release with the installable APK.

## License

```
Copyright (C) 2023 FossifyOrg — this project is a fork of Fossify File Manager.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.
```

See [LICENSE](LICENSE) for the full text.