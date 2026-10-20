# AGENTS.md

Android file manager (Kotlin). Two Gradle modules:

- `:app` — the app (`com.satirr.range`), View + Compose mix, `viewBinding` on.
- `:commons` — shared library, namespaced `com.satirr.commons` (`group` `com.satirr`).

## Build & verify

```bash
./gradlew :app:assembleCoreDebug    # debug APK ▶ app/build/outputs/apk/core/debug/
./gradlew :app:lintDebug            # lint.xml promotes several issues to fatal
./gradlew detekt                    # both modules; maxIssues: 0, strict rules
```

- Toolchain pinned in `gradle/libs.versions.toml`: AGP 9.3.1, Kotlin 2.3.10, Gradle 9.7.0, JDK 17, compile/target SDK 36, minSdk 26.
- CI (`.github/workflows/build.yml`) runs only `:app:assembleCoreDebug` on push to `main` and on tag `v*`.

## Flavor / signing quirks

- Flavors: `core`, `foss`, `gplay` (res-only differences); debug buildType appends `.debug` to the app id.
- Release signing: `keystore.properties` at repo root **or** `SIGNING_KEY_ALIAS`/`SIGNING_KEY_PASSWORD`/`SIGNING_STORE_FILE`/`SIGNING_STORE_PASSWORD` env vars must be set. Otherwise the build is unsigned with a warning (not an error).
- Version from `gradle.properties` (`VERSION_NAME`, `VERSION_CODE`, `APP_ID`), wired via `project.property`.

## Lint & detekt rules (CI-relevant)

- `lint.xml` (root) marks issues fatal: `ExportedReceiver`, `ExportedService`, `ExportedContentProvider`, `MissingPermission`, `NotificationPermission`, `CheckResult`, `WrongThread`, `StaticFieldLeak`, etc. Per-module `lint-baseline.xml` silences pre-existing issues.
- `detekt.yml` (root): `LongMethod` threshold 120, complexity/LongParameterList weighted 2, `MagicNumber` ignores `-1,0,1,2,42,1000`, `MaxLineLength` 120, `ReturnCount` max 4. Compose detekt rules active: `ModifierDefaultValue`, `ComposableParametersOrdering`, `PublicComposablePreview`, etc. `Composable`/`Preview` annotations exempt from naming/length/magic-number rules.
- Both modules set `buildUponDefaultConfig = true`; per-module `detekt-baseline.xml`.

## Commons (Room / Compose) specifics

- `commons` uses KSP with `room.schemaLocation = $projectDir/schemas`; exported schemas committed to `commons/schemas/...`. When a Room entity changes, commit the new schema alongside.
- `commons` compiles with `-Xcontext-receivers` and global opt-ins (material3, etc.). KSP/codegen: Room, Glide compiler, kotlinx-serialization.
- `commons` has `maven-publish` (release single variant); other modules depend on it via `project(":commons")`, not the published artifact.

## Style / workflow

- Single `main` branch, release via tag + GitHub Actions. Commit style: short imperative scope prefixes (`fix:`, `feat:`, `ci:`).
- Keep new resources translated (`strings.xml` has many locales); `MissingTranslation` is a warning (severity: warning in `lint.xml`).

## Github

All changes must be pushed to GitHub (`satirrdev/rangefm`) and built/verified via GitHub CI/CD. **Do not use local build for verification or publishing.** Contact: satirrdev@proton.me

## Version Control

Custom scheme:
- `1.x.x` → Major change (mostly changing/modifying all of the file)
- `2.x.x` → Semi-minor change (adding 3+ features)
- `3.x.x` → Minor change (1-2 features or bug fixes)
- `a` → Minor bug fixing