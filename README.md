# saptawara • Android Home Screen Widget

Aplikasi dan Home Screen Widget Android monokromatis bergaya industrialis **Nothing OS** yang menyajikan Tanggal Masehi dan Kalender Jawa (Hari, Pasaran, Wuku, Neptu, dan Tahun Sultan Agung) secara akurat, presisi, dan hemat daya.

![Nothing OS Style](https://img.shields.io/badge/Design-Nothing%20OS%20Inspired-black?style=flat-square)
![Jetpack Glance](https://img.shields.io/badge/Glance-1.1.1-green?style=flat-square)
![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-purple?style=flat-square)
![Offline](https://img.shields.io/badge/Network-100%25%20Offline-blue?style=flat-square)

---

## ✦ Fitur & Logika Penanggalan

1. **Hari & Pasaran Utama (Pancawara & Saptawara)**:
   - Fokus utama dengan tipografi Sans-Serif Bold kapital (`JUMAT WAGE`).
   - Dilengkapi aksen visual ikonik titik merah Nothing OS (`#D71921`).
2. **Tanggal Masehi Lengkap**:
   - Ditampilkan dalam format monospace bersih (`13 November 2026`).
3. **Penanggalan Jawa Sultan Agung, Wuku & Neptu**:
   - Menghitung tanggal dan bulan Jawa (Sura s.d. Besar), tahun Saka Jawa, dan nama tahun Windu (`2 Jumadilakir 1960 Be`).
   - Menghitung 30 siklus Wuku (Sinta s.d. Watugunung).
   - Menghitung bobot Neptu gabungan hari dan pasaran (`Neptu 10`).
   - Format baris sekunder: `2 Jumadilakir 1960 Be • Wuku Landep • Neptu 10`.
4. **Pembaruan Otomatis Jam 00:00:00**:
   - Menggunakan `AlarmManager.setExactAndAllowWhileIdle()` yang membidik tepat pada pergantian hari `00:00:01`.
   - Menangani event sistem: `BOOT_COMPLETED`, `TIMEZONE_CHANGED`, `TIME_SET`, dan `DATE_CHANGED`.

---

## ✦ Arahan Desain Nothing OS

| Elemen Visual | Nilai / Spesifikasi |
|---|---|
| **Latar Belakang** | Hitam pekat semi-transparan (`#CC0D0D0D`) *dark glassmorphism* |
| **Border / Bingkai** | Border tipis 1dp abu-abu gelap (`#33FFFFFF`) |
| **Corner Radius** | `20dp` membulat modern |
| **Tipografi Judul** | Sans-Serif Bold uppercase (`JUMAT WAGE`) |
| **Tipografi Angka/Detail**| Monospace (`13 November 2026`, `Neptu 10`) |
| **Aksen Ikonik** | Titik merah bulat 7–8dp (`#D71921`) di samping nama Pasaran |

---

## ✦ Struktur Berkas Proyek

```
Widget Tanggal Jawa/
├── app/
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   ├── java/id/saptawara/app/
│       │   │   ├── MainActivity.kt
│       │   │   ├── calendar/
│       │   │   │   └── JavaneseCalendarHelper.kt  # Logika hisab kalender Jawa
│       │   │   ├── scheduler/
│       │   │   │   ├── MidnightAlarmScheduler.kt  # Jadwal alarm tengah malam
│       │   │   │   └── WidgetUpdateReceiver.kt    # Receiver broadcast update
│       │   │   └── widget/
│       │   │       ├── JavaneseDateWidget.kt      # UI Jetpack Glance
│       │   │       └── JavaneseDateWidgetReceiver.kt
│       │   └── res/
│       │       ├── drawable/
│       │       │   ├── ic_launcher.xml
│       │       │   ├── red_indicator_dot.xml
│       │       │   └── widget_background.xml
│       │       ├── layout/
│       │       │   └── glance_default_loading_layout.xml
│       │       ├── values/
│       │       │   ├── colors.xml
│       │       │   └── strings.xml
│       │       └── xml/
│       │           └── javanese_date_widget_info.xml
│       └── test/java/id/saptawara/app/calendar/
│           └── JavaneseCalendarHelperTest.kt
├── docs/adr/
│   └── 0001-arsitektur-widget-saptawara.md
├── gradle/
│   ├── libs.versions.toml
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradlew
├── GLOSSARY.md
└── README.md
```

---

## ✦ Cara Build & Pasang di Android

1. Buka folder `Widget Tanggal Jawa` di **Android Studio** (Koala / Ladybug atau versi terbaru).
2. Tunggu proses *Gradle Sync* selesai.
3. Hubungkan perangkat Android fisik atau emulator (Android 8.0 Oreo / API 26 ke atas).
4. Klik **Run 'app'** (`Shift + F10`) atau jalankan perintah:
   ```bash
   ./gradlew installDebug
   ```
5. Buka Home Screen Android Anda, tekan lama pada ruang kosong, pilih menu **Widgets**, cari **saptawara**, lalu tarik widget ke layar utama.
