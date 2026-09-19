# AGENTS.md

Android file manager (Kotlin), fork of Fossify File Manager. Two Gradle modules:

- `:app` — the app (`com.satirr.range`), View + Compose mix, `viewBinding` on.
- `:commons` — vendored fork of Fossify Commons, de-branded and renamespaced to `com.satirr.commons` (`group` `com.satirr`). Do not restore Fossify branding or the upstream `org.fossify` namespace.

## Build & verify

```bash
./gradlew :app:assembleCoreDebug        # debug APK ▶ app/build/outputs/apk/core/debug/
./gradlew :app:lintDebug                # lint.xml promotes several issues to fatal
./gradlew detekt                        # both modules; maxIssues: 0, buildUponDefaultConfig, strict rules
```

- Toolchain pinned by catalog (`gradle/libs.versions.toml`): AGP 9.3.1, Kotlin 2.3.10, Gradle 9.7.0 (wrapper), JDK 17, compile/target SDK 36, minSdk 26.
- No unit or instrumentation tests exist; verification is build + lint + detekt. CI runs only `:app:assembleCoreDebug` (`.github/workflows/build.yml`); tag `v*` → release.

## Flavor / signing quirks

- Flavors: `core`, `foss`, `gplay` (res-only differences); debug buildType appends `.debug` to the app id.
- `:app` release is signed only when `keystore.properties` exists at repo root or `SIGNING_KEY_ALIAS`/`SIGNING_KEY_PASSWORD`/`SIGNING_STORE_FILE`/`SIGNING_STORE_PASSWORD` env vars are set. Otherwise builds unsigned with a warning — not an error.
- Version comes from `gradle.properties` (`VERSION_NAME`, `VERSION_CODE`, `APP_ID`), wired via `project.property`.

## Lint & detekt rules (CI-relevant)

- `lint.xml` (root) marks issues fatal, e.g. `ExportedReceiver`, `ExportedService`, `MissingPermission`, `StaticFieldLeak`, `CheckResult`, `WrongThread`. `lint-baseline.xml` per module silences pre-existing issues — keep new code out of the baseline.
- `detekt.yml` (root): `LongMethod` threshold 120, complexity/LongParameterList weighted 2, `MagicNumber` (whitelist -1,0,1,2,42,1000), `ReturnCount` max 4 (tests excluded), `MaxLineLength` 120. Compose detekt rules (`io.nlopez.compose.rules`) active: `ModifierDefaultValue`, `ComposableParametersOrdering`, etc. `Composable`/`Preview` annotations are exempt from naming/length/magic-number rules.
- Both modules set `buildUponDefaultConfig = true`; `detekt-baseline.xml` per module.
- `.editorconfig`: 4-space indent, star imports at 5 names.

## Commons (Room / Compose) specifics

- `commons` uses KSP with `room.schemaLocation = $projectDir/schemas`; exported schemas are committed (`commons/schemas/...`). When a Room entity changes, commit the new schema alongside.
- `commons` compiles with `-Xcontext-receivers` and global experimental API opt-ins (material3, etc.). KSP/codegen: Room, Glide compiler, kotlinx-serialization.
- `commons` has `maven-publish` (release single variant); other stuff depends on it via `project(":commons")`, not the published artifact.

## Style / workflow

- Single `main` branch, release via tag + GitHub Actions. Commit style: short imperative scope prefixes (`fix:`, `feat:`, `ci:`).
- Keep new resources translated (`strings.xml` has many locales); `MissingTranslation` is a warning.

## Github
All Change must be automaticly Pushed into Github, and builded with Github CI/CD, for testing or publishing. DO NOT USE LOCAL BUILD

## Version Control
1.2.3a
1 -> Major Change. Mostly Changing or Modifiying all of the file
2 -> Semi-Minor Change, Mostly Changing or Adding 3 or More Features
3 -> Minor Change, Changing or Adding 1 or 2 Feature or Bug Fixing
a -> Minor bug Fixing
