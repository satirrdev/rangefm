# Next Move: RangeFM Material 3 Expressive & Material You Implementation

Rencana tindak lanjut migrasi M3 Expressive dan integrasi Material You (Wallpaper Dynamic Color):

## 1. Material You Wallpaper (Dynamic Colors) System
- [x] Daftarkan `DynamicColors.applyToActivitiesIfAvailable(this)` pada `App.kt` (level application).
- [x] Sesuaikan tema XML `AppTheme.Base` di `commons/src/main/res/values/styles.xml` agar kompatibel dengan Material3 Dynamic Colors (`Theme.Material3.DynamicColors.DayNight.NoActionBar`).
- [x] Sinkronkan color scheme Compose (`AppTheme.kt` / `Theme.kt`) dan Activity/Dialogs (`setupDialogStuff`, `MyCompatRadioButton`, `MyTextInputLayout`, `MySearchMenu`) agar otomatis mengikuti wallpaper monet Android 12+ (SDK 31+) saat dynamic theme aktif.

## 2. Expressive Progress & Loading Indicator
- [x] Perbarui indicator di `items_fragment.xml` dengan M3 Expressive attributes (pill corner radius, trackColor `colorSurfaceContainerHighest`, indicatorColor `colorPrimary`).
- [x] Sinkronkan `progressBar` di `ItemsFragment.kt` agar otomatis mewarisi palette dynamic wallpaper tanpa overwrite manual tinting.

## 3. Expressive Search Bar Integration
- [x] Transformasi `MySearchMenu` dan `search_menu_background.xml` ke M3 Expressive pill shape (`28dp` radius, `colorSurfaceContainerHigh`).
- [x] Hilangkan tinting manual hex saat dynamic theme aktif agar mengikuti palette wallpaper.

## 4. Migrasi Dialog Inti ke M3 Expressive
- [x] Update `Activity.setupDialogStuff` agar dialog berbasis `MaterialAlertDialogBuilder` (seperti `CreateNewItemDialog`, `ChangeSortingDialog`) tidak di-override warna teks button-nya saat dynamic theme aktif.
- [x] Pertahankan `material_dialog_corner_radius` di 28dp (M3 extra-large squircle).

## 5. Expressive Menu & Toolbar Actions
- [x] Integrasikan `ExpressiveDropdownMenu` (M3 card shape, elevation) ke dalam `ExpressiveFloatingToolbar` di `ItemsFragment`.
- [x] Tambahkan menu aksi More: Rename (`R.id.cab_rename`), Properties (`R.id.cab_properties`), Compress (`R.id.cab_compress`).

## 6. Verifikasi & Build
- [ ] Verifikasi build lokal: `./gradlew :app:assembleCoreDebug`.
- [ ] Push ke branch `feat/m3-expressive`.
- [ ] Pastikan GitHub Actions CI lulus hijau.
