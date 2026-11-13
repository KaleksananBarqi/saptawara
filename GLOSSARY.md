# saptawara Domain Glossary

**saptawara** adalah aplikasi Android dan Home Screen Widget minimalis industrialis (Nothing OS inspired) untuk menampilkan kalender Masehi, Kalender Jawa Sultan Agung, siklus Pasaran (Pancawara), Wuku, dan bobot Neptu.

## Core Domain Terms

**Saptawara**:
Siklus tujuh hari mingguan (Minggu, Senin, Selasa, Rabu, Kamis, Jumat, Sabtu). Masing-masing hari memiliki bobot neptu tertentu (Minggu: 5, Senin: 4, Selasa: 3, Rabu: 7, Kamis: 8, Jumat: 6, Sabtu: 9).
_Avoid_: Hari masehi biasa tanpa konteks siklus.

**Pancawara (Pasaran)**:
Siklus lima harian dalam kalender Jawa yang terdiri dari Legi (5), Pahing (9), Pon (7), Wage (4), dan Kliwon (8). Berputar terus-menerus secara berurutan dan independen dari siklus Saptawara.
_Avoid_: Pasar mingguan, Hari pasar.

**Weton**:
Kombinasi antara Saptawara dan Pancawara pada suatu hari tertentu (contoh: "Jumat Wage"). Menghasilkan total neptu gabungan (6 + 4 = 10).
_Avoid_: Hari lahir saja (weton berlaku untuk setiap hari berjalan).

**Neptu**:
Nilai numerik mistis/simbolik matematis yang diasosiasikan dengan hari (Saptawara) dan pasaran (Pancawara). Digunakan dalam tradisi Jawa untuk perhitungan keselarasan dan penanggalan.
_Avoid_: Ramalan numerologi.

**Wuku**:
Siklus penanggalan tradisional Jawa-Bali sepanjang 210 hari yang terbagi menjadi 30 wuku (masing-masing 7 hari, dimulai dari Minggu dan berakhir Sabtu). Dimulai dari wuku Sinta hingga Watugunung.
_Avoid_: Horoskop, Zodiak Jawa.

**Kurup Asapon**:
Standar kurup hisab kalender Jawa Sultan Agung masa kini yang dimulai pada tahun Alip dengan hari Selasa Pon (Alip-Selasa-Pon). Menjadi acuan deterministik hisab aritmetika penanggalan Jawa.
_Avoid_: Kurup Anenhing (kurup lama).

**Windu**:
Siklus 8 tahun dalam penanggalan Jawa Sultan Agung yang terdiri dari tahun Alip (354 hari), Ehe (355 hari - kabisat), Jimawal (354 hari), Je (354 hari), Dal (355 hari - kabisat), Be (354 hari), Wawu (354 hari), dan Jimakir (355 hari - kabisat). Total hari per windu adalah tepat 2.835 hari (kelipatan bulat dari 5 dan 7).
_Avoid_: Dekade, Lustrum.

**Nothing OS Aesthetic**:
Bahasa visual industrialis modern yang ditandai dengan latar belakang monokromatik gelap semi-transparan (`#CC0D0D0D`), border halus 1dp (`#33FFFFFF`), sudut membulat 20dp, tipografi monospace, dan aksen titik merah ikonik (`#D71921`).
_Avoid_: Colorful material, Neumorphism, Skeuomorphism.

**Tampilan Kotak (Square 2x2)**:
Format tata letak widget padat berdimensi grid 2×2 (ukuran dasar `130dp × 130dp`) dengan susunan vertikal seimbang di mana penanggalan sekunder dipecah menjadi dua baris terpisah (Baris 1: Tahun Jawa Sultan Agung; Baris 2: Wuku & Neptu) agar tidak terpotong.
_Avoid_: Widget kecil mini, Tampilan bujursangkar terpotong.

**Tampilan Lebar (Wide 4x2)**:
Format tata letak widget horizontal membentang berdimensi grid 4×2 (ukuran dasar `240dp × 90dp`) di mana seluruh informasi penanggalan sekunder ditampilkan dalam satu baris horizontal berkesinambungan.
_Avoid_: Widget panjang, Full-screen banner.
