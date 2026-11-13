# ADR 0001: Arsitektur Widget saptawara Menggunakan Jetpack Glance dan Hisab Kurup Asapon Murni

## Status
Diterima (Accepted)

## Konteks
Aplikasi dan Home Screen Widget **saptawara** dirancang untuk menyajikan informasi ganda: Penanggalan Masehi modern dan Penanggalan Jawa tradisional (Hari/Saptawara, Pasaran/Pancawara, Wuku, Neptu, serta kalender Sultan Agung).
Tantangan teknis utama meliputi:
1. Memilih framework widget Android antara XML RemoteViews tradisional atau Jetpack Glance deklaratif.
2. Memilih sumber data hisab kalender Jawa: integrasi library eksternal/API daring vs algoritma komputasi hisab deterministik lokal (offline).
3. Memastikan pembaruan data widget tepat pada tengah malam (pukul 00:00:00) tanpa memboroskan baterai pengguna melalui polling berkala.

## Keputusan
1. **Framework UI: Jetpack Glance (`androidx.glance:glance-appwidget`)**
   - Menggunakan Glance karena mendukung paradigma deklaratif modern (Jetpack Compose untuk AppWidget), memudahkan penerapan styling visual Nothing OS (border, corner radius, glassmorphism gelap, dot aksen), dan terintegrasi aman dengan lifecycle Android modern.
2. **Logika Penanggalan: Algoritma Kurup Asapon Murni (`JavaneseCalendarHelper`)**
   - Mengimplementasikan hisab kalender Sultan Agung (Kurup Asapon) dan siklus Wuku (210 hari) secara murni di dalam class helper berbasis `java.time.LocalDate`.
   - Menghindari ketergantungan pada library pihak ketiga yang usang atau API daring yang rentan terhadap putusnya jaringan atau latency.
3. **Mekanisme Update Tepat Waktu: AlarmManager Exact Alarm + BroadcastReceiver**
   - Menjadwalkan `AlarmManager.setExactAndAllowWhileIdle()` yang membidik detik pertama tengah malam (`00:00:01`).
   - Menyertakan listener terhadap event sistem (`TIME_SET`, `TIMEZONE_CHANGED`, `DATE_CHANGED`, `BOOT_COMPLETED`) untuk memastikan widget selalu sinkron saat pengguna bepergian atau mengubah jam perangkat secara manual.

## Konsekuensi
- **Positif:**
  - 100% fungsional tanpa koneksi internet (zero network footprint).
  - Konsumsi daya baterai mendekati nol karena tidak ada background polling berkala.
  - Tampilan konsisten dan modern sesuai panduan visual Nothing OS.
- **Negatif / Perhatian:**
  - Memerlukan izin `SCHEDULE_EXACT_ALARM` pada Android 12+ (API 31+) dengan mekanisme fallback `setAndAllowWhileIdle()`.
