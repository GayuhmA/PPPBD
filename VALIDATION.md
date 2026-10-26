# Hasil pemeriksaan MyShop

Tanggal: **26 Oktober 2026**, zona waktu Asia/Jakarta.

## Build dan pengujian pada HP

Perintah yang selesai dengan **BUILD SUCCESSFUL**:

```bash
./gradlew :app:assembleDebug :app:lintDebug :app:connectedDebugAndroidTest \
  --console=plain --max-workers=2 --no-parallel \
  '-Dorg.gradle.jvmargs=-Xmx1024m -XX:MaxMetaspaceSize=512m -Dfile.encoding=UTF-8'
```

Perangkat pengujian: **Samsung SM-A366B, Android 16**, tersambung lewat USB debugging.

| Pemeriksaan | Hasil |
| --- | --- |
| Build APK debug | Berhasil |
| Generate Safe Args dan View Binding | Berhasil |
| Kompilasi APK tes Espresso | Berhasil |
| Android Lint | **0 errors, 7 warnings** |
| Tes UI pada HP | **8 lulus, 0 gagal, 0 error, 0 dilewati** |
| Parse seluruh XML Android dan JSON VS Code | Berhasil |
| Sintaks Bash `scripts/android.sh` | Berhasil |
| Kesesuaian graph/menu dan jumlah provinsi | 5 destination, 3 tab, 10 provinsi |

Delapan tes yang lulus:

1. `bottomNavigationOpensAllThreeTabs`: Home, Keranjang, dan Profile dapat dibuka.
2. `productNameIsPassedToCheckout`: nama produk diterima melalui Safe Args.
3. `everyProvinceCanBeReturnedToCheckout`: seluruh 10 provinsi dikembalikan ke Checkout.
4. `checkoutDoneReturnsHomeAndNewCheckoutHasNoOldAddress`: Selesai kembali ke Home, checkout baru tidak mewarisi alamat checkout lama.
5. `backCancelsUnconfirmedProvinceChange`: Back membatalkan perubahan provinsi yang belum dikonfirmasi.
6. `checkoutRetainsProductAndAddressAfterRecreation`: produk dan alamat bertahan setelah Activity dibuat ulang.
7. `addressSelectionSurvivesRecreationAndReturnsToCheckout`: pilihan spinner bertahan setelah Activity dibuat ulang dan dapat dikembalikan.
8. `selectedTabSurvivesRecreation`: tab aktif bertahan setelah Activity dibuat ulang.

## Catatan Android Lint

Enam warning memberi tahu adanya versi Gradle/plugin/dependensi yang lebih baru. Versi Navigation **2.9.4** dipertahankan sesuai modul dan versi toolchain proyek telah berhasil dibangun.

Satu warning `KeyboardInaccessibleWidget` terkait kolom alamat yang dibuat tidak fokus dan tidak dapat diketik langsung, mengikuti pola modul. Kolom tersebut telah teruji dapat ditekan melalui layar sentuh untuk membuka pemilihan provinsi. Akses navigasi dengan keyboard perangkat keras masih merupakan keterbatasan pola ini.

Pengujian dilakukan pada satu HP Android 16. Dukungan minimum API 24 dikonfigurasi dalam proyek, tetapi belum diuji langsung pada perangkat API 24. Tes pemulihan menggunakan `ActivityScenario.recreate()`; penghentian proses oleh sistem tidak disimulasikan.

## Artefak

- APK: `app/build/outputs/apk/debug/app-debug.apk`.
- Lint: `app/build/reports/lint-results-debug.html`.
- Hasil tes: `app/build/reports/androidTests/connected/debug/index.html`.
- JUnit XML: `app/build/outputs/androidTest-results/connected/debug/TEST-SM-A366B - 16-_app-.xml`.

Laporan dan APK dibuat oleh Gradle di folder `build` yang diabaikan Git. Bangun kembali untuk membuatnya setelah folder build dihapus.
