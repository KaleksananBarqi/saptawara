# Changelog

Semua perubahan penting pada proyek **saptawara** akan didokumentasikan dalam berkas ini. Format mengacu pada [Keep a Changelog](https://keepachangelog.com/id/1.0.0/) dan proyek ini mematuhi [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [1.1.0] - 2026-11-13

### 🚀 Ditambahkan (Added)
- **Format Kotak 2x2 (Square) & Mode Responsif (`SizeMode.Responsive`)**:
  - Menjadikan ukuran dasar widget default sebagai **2x2 cell** (`130dp × 130dp`) agar serasi dengan widget foto/kotak beranda Android.
  - Memperkenalkan arsitektur *dual breakpoint* menggunakan Jetpack Glance `SizeMode.Responsive`: otomatis beralih antara tampilan padat 2x2 (`SquareWidgetContent`) dan tampilan membentang 4x2 (`WideWidgetContent`).
  - Pemecahan baris informasi sekunder kalender Jawa (Baris 1: Tahun Sultan Agung, Baris 2: Wuku & Total Neptu) pada tampilan 2x2 agar teks tidak terpotong.
  - Penyesuaian skala tipografi Weton (17sp) dan padding rapat (14dp) pada format kotak.
- **Dokumentasi Keputusan Arsitektur**: Menambahkan `docs/adr/0002-widget-responsif-ukuran-kotak-dan-lebar.md`.
- **Glosarium Domain**: Penambahan istilah kanonik `Tampilan Kotak (Square 2x2)` dan `Tampilan Lebar (Wide 4x2)` pada `GLOSSARY.md`.

---

## [1.0.1] - 2026-11-13

### 🐛 Diperbaiki (Fixed)
- **Perbaikan Crash "Tidak dapat memuat widget"**: Memperbaiki pemanggilan background XML drawable pada Jetpack Glance (`JavaneseDateWidget.kt`) dengan membungkus `R.drawable.widget_background` menggunakan `ImageProvider`. Sebelumnya pemanggilan langsung menyebabkan `Resources$NotFoundException` karena Glance memperlakukannya sebagai `@ColorRes`.
- **Indikator Titik Merah Ikonik Nothing OS**: Mengganti `Box` sudut lengkung manual dengan `Image(provider = ImageProvider(R.drawable.red_indicator_dot))` agar titik merah `#D71921` ter-render sempurna sebagai lingkaran di seluruh launcher Android.
- **Handling Error Komposisi**: Menambahkan layout fallback `errorUiLayout` dan override `onCompositionError` untuk memudahkan pencatatan log jika terjadi kendala pada Glance.

---

## [1.0.0] - 2026-11-13

### 🚀 Ditambahkan (Added)
- **Logika Penanggalan Jawa Sultan Agung**: Implementasi komputasi hisab Kurup Asapon deterministik dan 100% offline (`JavaneseCalendarHelper.kt`) mencakup Hari (Saptawara), Pasaran (Pancawara), Wuku (30 siklus wuku), dan bobot total Neptu.
- **Home Screen Widget Jetpack Glance**: Widget deklaratif Android modern bergaya industrialis monokrom Nothing OS (`JavaneseDateWidget.kt`) dengan background semi-transparan (`#CC0D0D0D`), border 1dp abu-abu (`#33FFFFFF`), sudut membulat 20dp, dan aksen titik merah (#D71921).
- **Penjadwalan Tepat Waktu (Jam 00:00)**: Penjadwalan `AlarmManager.setExactAndAllowWhileIdle()` pada detik 00:00:01 tengah malam + broadcast receiver event sistem (`BOOT_COMPLETED`, `TIME_SET`, `TIMEZONE_CHANGED`, `DATE_CHANGED`).
- **Pratinjau Aplikasi Utama**: Halaman `MainActivity.kt` dengan Jetpack Compose menampilkan pratinjau widget interaktif dan rincian data hisab kalender hari ini.
- **Pengujian Unit**: Pengujian otomatis hisab penanggalan pada `JavaneseCalendarHelperTest.kt`.
- **Konfigurasi Agent Skills**: Setup Matt Pocock's skills (`docs/agents/issue-tracker.md`, `docs/agents/domain.md`, dan `AGENTS.md`).
