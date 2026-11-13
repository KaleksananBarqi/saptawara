package id.saptawara.app.calendar

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Data class yang menampung seluruh informasi hasil konversi penanggalan
 * Masehi ke sistem Kalender Jawa Sultan Agung, Pasaran, Wuku, dan Neptu.
 */
data class JavaneseDateInfo(
    val masehiDate: String,           // Contoh: "13 November 2026"
    val dayName: String,              // Contoh: "Jumat"
    val pasaran: String,              // Contoh: "Wage"
    val dayPasaran: String,           // Contoh: "Jumat Wage"
    val dayNeptu: Int,                // Bobot neptu hari (Jumat = 6)
    val pasaranNeptu: Int,            // Bobot neptu pasaran (Wage = 4)
    val totalNeptu: Int,              // Penjumlahan neptu (6 + 4 = 10)
    val wuku: String,                 // Nama siklus 30 wuku (contoh: "Landep")
    val javaneseDay: Int,             // Tanggal bulan Jawa (contoh: 2)
    val javaneseMonth: String,        // Nama bulan Jawa (contoh: "Jumadilakir")
    val javaneseYear: Int,            // Tahun Saka Jawa (contoh: 1960)
    val winduYearName: String,        // Nama tahun dalam siklus Windu 8 tahun (contoh: "Be")
    val formattedJavaneseDate: String,// Format ringkas: "2 Jumadilakir 1960 Be"
    val secondaryText: String         // Format baris sekunder widget: "2 Jumadilakir 1960 Be • Wuku Landep • Neptu 10"
)

/**
 * Helper komputasi Kalender Jawa, Pasaran, Wuku, dan Neptu berbasis hisab
 * aritmetik Kurup Asapon (Kalender Jawa Islam Sultan Agung).
 * Beroperasi 100% offline dan deterministik menggunakan java.time.LocalDate.
 */
object JavaneseCalendarHelper {

    // Daftar 5 Pasaran Jawa (Pancawara)
    private val PASARAN_NAMES = listOf("Legi", "Pahing", "Pon", "Wage", "Kliwon")

    // Bobot Neptu untuk 7 Hari Masehi (Saptawara)
    // Minggu: 5, Senin: 4, Selasa: 3, Rabu: 7, Kamis: 8, Jumat: 6, Sabtu: 9
    private val DAY_NEPTU_MAP = mapOf(
        DayOfWeek.SUNDAY to 5,
        DayOfWeek.MONDAY to 4,
        DayOfWeek.TUESDAY to 3,
        DayOfWeek.WEDNESDAY to 7,
        DayOfWeek.THURSDAY to 8,
        DayOfWeek.FRIDAY to 6,
        DayOfWeek.SATURDAY to 9
    )

    // Bobot Neptu untuk 5 Pasaran (Pancawara)
    // Legi: 5, Pahing: 9, Pon: 7, Wage: 4, Kliwon: 8
    private val PASARAN_NEPTU_MAP = mapOf(
        "Legi" to 5,
        "Pahing" to 9,
        "Pon" to 7,
        "Wage" to 4,
        "Kliwon" to 8
    )

    // Daftar 30 Wuku (siklus 210 hari, berganti setiap hari Minggu)
    private val WUKU_NAMES = listOf(
        "Sinta", "Landep", "Wukir", "Kurantil", "Tolu",
        "Gumbreg", "Warigalit", "Warigagung", "Julangwangi", "Sungsang",
        "Galungan", "Kuningan", "Langkir", "Mandasiya", "Julungpujut",
        "Pahang", "Kuruwelut", "Marakeh", "Tambir", "Medangkungan",
        "Maktal", "Wuye", "Manahil", "Prangbakat", "Bala",
        "Wugu", "Wayang", "Kulawu", "Dukut", "Watugunung"
    )

    // Siklus 8 Tahun Windu Jawa: (Nama Tahun, Apakah Kabisat, Jumlah Hari)
    // Windu: Alip (354), Ehe (355), Jimawal (354), Je (354), Dal (355), Be (354), Wawu (354), Jimakir (355)
    private data class WinduYear(val name: String, val isLeap: Boolean, val totalDays: Int)

    private val WINDU_CYCLE = listOf(
        WinduYear("Alip", false, 354),
        WinduYear("Ehe", true, 355),
        WinduYear("Jimawal", false, 354),
        WinduYear("Je", false, 354),
        WinduYear("Dal", true, 355),
        WinduYear("Be", false, 354),
        WinduYear("Wawu", false, 354),
        WinduYear("Jimakir", true, 355)
    )

    // 12 Nama Bulan Kalender Jawa
    private val JAVANESE_MONTHS = listOf(
        "Sura", "Sapar", "Mulud", "Bakda Mulud",
        "Jumadilawal", "Jumadilakir", "Rejeb", "Ruwah",
        "Pasa", "Sawal", "Sela", "Besar"
    )

    // Titik referensi Anchor: 1 Sura 1960 Be jatuh pada tanggal 17 Juni 2026 Masehi
    private val SULTAN_AGUNG_ANCHOR_DATE = LocalDate.of(2026, 6, 17)
    private const val SULTAN_AGUNG_ANCHOR_YEAR = 1960
    private const val SULTAN_AGUNG_ANCHOR_WINDU_INDEX = 5 // Index 5 = Tahun "Be"

    // Titik referensi Anchor Wuku: Minggu 1 November 2026 adalah awal Wuku Sinta (index 0)
    private val WUKU_SINTA_ANCHOR_DATE = LocalDate.of(2026, 11, 1)

    // Formatter tanggal Masehi berbahasa Indonesia (contoh: "13 November 2026")
    private val MASEHI_DATE_FORMATTER = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("id", "ID"))

    /**
     * Menghitung seluruh data kalender untuk tanggal yang diberikan.
     * Mengembalikan objek JavaneseDateInfo lengkap.
     */
    fun calculate(date: LocalDate = LocalDate.now()): JavaneseDateInfo {
        val masehiDateStr = date.format(MASEHI_DATE_FORMATTER)
        val dayName = getIndonesianDayName(date.dayOfWeek)
        val pasaran = calculatePasaran(date)
        val dayPasaran = "$dayName $pasaran"

        val dayNeptu = DAY_NEPTU_MAP[date.dayOfWeek] ?: 0
        val pasaranNeptu = PASARAN_NEPTU_MAP[pasaran] ?: 0
        val totalNeptu = dayNeptu + pasaranNeptu

        val wuku = calculateWuku(date)
        val (javaneseDay, javaneseMonth, javaneseYear, winduYearName) = calculateSultanAgungDate(date)

        val formattedJavaneseDate = "$javaneseDay $javaneseMonth $javaneseYear $winduYearName"
        val secondaryText = "$formattedJavaneseDate • Wuku $wuku • Neptu $totalNeptu"

        return JavaneseDateInfo(
            masehiDate = masehiDateStr,
            dayName = dayName,
            pasaran = pasaran,
            dayPasaran = dayPasaran,
            dayNeptu = dayNeptu,
            pasaranNeptu = pasaranNeptu,
            totalNeptu = totalNeptu,
            wuku = wuku,
            javaneseDay = javaneseDay,
            javaneseMonth = javaneseMonth,
            javaneseYear = javaneseYear,
            winduYearName = winduYearName,
            formattedJavaneseDate = formattedJavaneseDate,
            secondaryText = secondaryText
        )
    }

    /**
     * Menghitung Pasaran (Legi, Pahing, Pon, Wage, Kliwon)
     * Titik acuan: 1 Januari 1970 UTC (epochDay = 0) adalah hari Kamis Wage.
     */
    private fun calculatePasaran(date: LocalDate): String {
        val epochDay = date.toEpochDay()
        // Pada epochDay = 0 (Kamis Wage), index Wage dalam PASARAN_NAMES adalah 3.
        val pasaranIndex = Math.floorMod(epochDay + 3, 5).toInt()
        return PASARAN_NAMES[pasaranIndex]
    }

    /**
     * Menghitung Wuku (1 dari 30 siklus wuku)
     * Siklus wuku berulang setiap 210 hari (30 wuku x 7 hari).
     */
    private fun calculateWuku(date: LocalDate): String {
        val diffDays = date.toEpochDay() - WUKU_SINTA_ANCHOR_DATE.toEpochDay()
        val cycleDay = Math.floorMod(diffDays, 210).toInt()
        val wukuIndex = cycleDay / 7
        return WUKU_NAMES[wukuIndex]
    }

    /**
     * Menghitung Tanggal, Bulan, Tahun, dan Nama Tahun Windu Kalender Jawa Sultan Agung.
     * Menggunakan metode kurup Asapon dan tabel panjang bulan berulang.
     */
    private fun calculateSultanAgungDate(date: LocalDate): JavaneseDateResult {
        val diffDays = (date.toEpochDay() - SULTAN_AGUNG_ANCHOR_DATE.toEpochDay()).toInt()
        var currentYear = SULTAN_AGUNG_ANCHOR_YEAR
        var currentWinduIndex = SULTAN_AGUNG_ANCHOR_WINDU_INDEX

        if (diffDays >= 0) {
            var remainingDays = diffDays
            while (true) {
                val yearDays = WINDU_CYCLE[currentWinduIndex].totalDays
                if (remainingDays < yearDays) {
                    break
                }
                remainingDays -= yearDays
                currentYear++
                currentWinduIndex = (currentWinduIndex + 1) % 8
            }

            val isLeapYear = WINDU_CYCLE[currentWinduIndex].isLeap
            var monthIndex = 0
            while (monthIndex < 12) {
                val monthDays = getJavaneseMonthDays(monthIndex, isLeapYear)
                if (remainingDays < monthDays) {
                    break
                }
                remainingDays -= monthDays
                monthIndex++
            }

            return JavaneseDateResult(
                day = remainingDays + 1,
                month = JAVANESE_MONTHS[monthIndex],
                year = currentYear,
                winduYear = WINDU_CYCLE[currentWinduIndex].name
            )
        } else {
            var remainingDays = -diffDays
            while (true) {
                val prevWinduIndex = Math.floorMod(currentWinduIndex - 1, 8)
                val yearDays = WINDU_CYCLE[prevWinduIndex].totalDays
                if (remainingDays <= yearDays) {
                    currentYear--
                    currentWinduIndex = prevWinduIndex
                    remainingDays = yearDays - remainingDays
                    break
                }
                remainingDays -= yearDays
                currentYear--
                currentWinduIndex = prevWinduIndex
            }

            val isLeapYear = WINDU_CYCLE[currentWinduIndex].isLeap
            var monthIndex = 0
            while (monthIndex < 12) {
                val monthDays = getJavaneseMonthDays(monthIndex, isLeapYear)
                if (remainingDays < monthDays) {
                    break
                }
                remainingDays -= monthDays
                monthIndex++
            }

            return JavaneseDateResult(
                day = remainingDays + 1,
                month = JAVANESE_MONTHS[monthIndex],
                year = currentYear,
                winduYear = WINDU_CYCLE[currentWinduIndex].name
            )
        }
    }

    /**
     * Jumlah hari dalam bulan Jawa (bergantian 30 dan 29 hari; bulan Besar 30 hari jika kabisat).
     */
    private fun getJavaneseMonthDays(monthIndex: Int, isLeapYear: Boolean): Int {
        return if (monthIndex == 11) { // Bulan ke-12 (Besar)
            if (isLeapYear) 30 else 29
        } else {
            if (monthIndex % 2 == 0) 30 else 29
        }
    }

    /**
     * Nama hari dalam bahasa Indonesia.
     */
    private fun getIndonesianDayName(dayOfWeek: DayOfWeek): String {
        return when (dayOfWeek) {
            DayOfWeek.MONDAY -> "Senin"
            DayOfWeek.TUESDAY -> "Selasa"
            DayOfWeek.WEDNESDAY -> "Rabu"
            DayOfWeek.THURSDAY -> "Kamis"
            DayOfWeek.FRIDAY -> "Jumat"
            DayOfWeek.SATURDAY -> "Sabtu"
            DayOfWeek.SUNDAY -> "Minggu"
        }
    }

    private data class JavaneseDateResult(
        val day: Int,
        val month: String,
        val year: Int,
        val winduYear: String
    )
}
