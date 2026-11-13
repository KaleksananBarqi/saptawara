# ADR 0002: Dukungan Format Responsif Widget Kotak 2x2 dan Lebar 4x2 Menggunakan Glance SizeMode.Responsive

## Status
Diterima (Accepted)

## Konteks
Awalnya, widget **saptawara** dikonfigurasi dengan ukuran awal 3x2 / 4x2 membentang secara horizontal (`minWidth="180dp"`, `minHeight="80dp"`).
Pengguna membutuhkan widget berukuran kotak 2x2 (*square*) agar serasi dengan tata letak kisi beranda Android (seperti widget foto atau jam compact 2x2).

Tantangan teknis:
1. Menentukan apakah widget kotak disediakan sebagai komponen AppWidget terpisah di *manifest* atau disatukan ke dalam satu provider tunggal yang adaptif.
2. Pada kanvas sempit 2x2 (~130dp), teks informasi sekunder (`2 Jumadilakir 1960 Be • Wuku Landep • Neptu 10`) tidak muat dalam 1 baris dan akan terpotong secara visual (*clipping/truncation*).
3. Memastikan widget tetap rapi jika pengguna memperlebar ukuran widget ke 4x2 atau sebaliknya.

## Keputusan
1. **Penyatuan Provider Tunggal dengan `SizeMode.Responsive`**:
   - Alih-alih membuat beberapa deklarasi receiver/provider terpisah yang membingungkan daftar pilihan widget di launcher, kami memanfaatkan fitur bawaan Jetpack Glance:
     `override val sizeMode: SizeMode = SizeMode.Responsive(setOf(SQUARE_2X2, WIDE_4X2))`
   - Nilai default metadata XML (`javanese_date_widget_info.xml`) diubah menjadi `targetCellWidth="2"` dan `targetCellHeight="2"` dengan `minWidth="130dp"` dan `minHeight="130dp"`.
2. **Dual Layout Berdasarkan Evaluasi Ukuran (`LocalSize.current`)**:
   - **Tampilan Kotak (`Square 2x2`)** (`size.width < 210dp`):
     - Memecah informasi penanggalan sekunder menjadi 2 baris vertikal:
       - Baris 1: Tanggal Sultan Agung (`info.formattedJavaneseDate`).
       - Baris 2: Wuku & Total Neptu (`"Wuku ${info.wuku} • Neptu ${info.totalNeptu}"`).
     - Menyesuaikan ukuran font nama Weton menjadi 17sp dengan padding 14dp.
   - **Tampilan Lebar (`Wide 4x2`)** (`size.width >= 210dp`):
     - Mempertahankan tata letak horizontal baris tunggal (`info.secondaryText`) dengan font Weton 20sp dan padding 16dp.

## Konsekuensi
- **Positif:**
  - Satu widget yang bersih di app drawer/widget picker, namun mampu beradaptasi sempurna ke berbagai ukuran slot grid launcher.
  - Saat pertama kali ditarik ke beranda, widget langsung berbentuk kotak 2x2 presisi.
  - Seluruh informasi kalender Jawa tetap terbaca 100% tanpa risiko teks terpotong pada ukuran kotak.
- **Negatif / Perhatian:**
  - Glance merender komposisi untuk kedua breakpoint ukuran di latar belakang saat initial setup, namun dampak performanya sangat minim dan diimbangi oleh fleksibilitas tata letak.
