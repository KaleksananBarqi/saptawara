# Changelog

Semua perubahan penting pada proyek **saptawara** akan didokumentasikan dalam berkas ini. Format mengacu pada [Keep a Changelog](https://keepachangelog.com/id/1.0.0/) dan proyek ini mematuhi [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [1.0.0] - 2026-11-13

### 🚀 Ditambahkan (Added)
- **Logika Penanggalan Jawa Sultan Agung**: Implementasi komputasi hisab Kurup Asapon deterministik dan 100% offline (`JavaneseCalendarHelper.kt`) mencakup Hari (Saptawara), Pasaran (Pancawara), Wuku (30 siklus wuku), dan bobot total Neptu.
- **Home Screen Widget Jetpack Glance**: Widget deklaratif Android modern bergaya industrialis monokrom Nothing OS (`JavaneseDateWidget.kt`) dengan background semi-transparan (`#CC0D0D0D`), border 1dp abu-abu (`#33FFFFFF`), sudut membulat 20dp, dan aksen titik merah (#D71921).
- **Penjadwalan Tepat Waktu (Jam 00:00)**: Penjadwalan `AlarmManager.setExactAndAllowWhileIdle()` pada detik 00:00:01 tengah malam + broadcast receiver event sistem (`BOOT_COMPLETED`, `TIME_SET`, `TIMEZONE_CHANGED`, `DATE_CHANGED`).
- **Pratinjau Aplikasi Utama**: Halaman `MainActivity.kt` dengan Jetpack Compose menampilkan pratinjau widget interaktif dan rincian data hisab kalender hari ini.
- **Pengujian Unit**: Pengujian otomatis hisab penanggalan pada `JavaneseCalendarHelperTest.kt`.
- **Konfigurasi Agent Skills**: Setup Matt Pocock's skills (`docs/agents/issue-tracker.md`, `docs/agents/domain.md`, dan `AGENTS.md`).
