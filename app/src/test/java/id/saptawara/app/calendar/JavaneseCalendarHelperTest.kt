package id.saptawara.app.calendar

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

/**
 * Pengujian unit untuk memverifikasi akurasi hisab kalender Jawa Sultan Agung,
 * Pasaran, Neptu, dan Wuku.
 */
class JavaneseCalendarHelperTest {

    @Test
    fun testReferenceDate13November2026() {
        val date = LocalDate.of(2026, 11, 13)
        val info = JavaneseCalendarHelper.calculate(date)

        // Verifikasi Hari Masehi & Pasaran
        assertEquals("Jumat", info.dayName)
        assertEquals("Wage", info.pasaran)
        assertEquals("Jumat Wage", info.dayPasaran)

        // Verifikasi Bobot Neptu: Jumat (6) + Wage (4) = 10
        assertEquals(6, info.dayNeptu)
        assertEquals(4, info.pasaranNeptu)
        assertEquals(10, info.totalNeptu)

        // Verifikasi Wuku (Siklus 210 hari) -> Landep
        assertEquals("Landep", info.wuku)

        // Verifikasi Tanggal Jawa Sultan Agung Kurup Asapon
        assertEquals(2, info.javaneseDay)
        assertEquals("Jumadilakir", info.javaneseMonth)
        assertEquals(1960, info.javaneseYear)
        assertEquals("Be", info.winduYearName)

        // Verifikasi String output sekunder
        assertEquals("2 Jumadilakir 1960 Be • Wuku Landep • Neptu 10", info.secondaryText)
    }

    @Test
    fun testEpochAnchor1January1970() {
        // 1 Januari 1970 adalah Kamis Wage (Pasaran Wage)
        val date = LocalDate.of(1970, 1, 1)
        val info = JavaneseCalendarHelper.calculate(date)

        assertEquals("Kamis", info.dayName)
        assertEquals("Wage", info.pasaran)
        assertEquals("Kamis Wage", info.dayPasaran)
        assertEquals(8, info.dayNeptu)
        assertEquals(4, info.pasaranNeptu)
        assertEquals(12, info.totalNeptu)
    }

    @Test
    fun testSura1960Anchor17June2026() {
        // 17 Juni 2026 adalah 1 Sura 1960 Be (Rabu Kliwon)
        val date = LocalDate.of(2026, 6, 17)
        val info = JavaneseCalendarHelper.calculate(date)

        assertEquals("Rabu", info.dayName)
        assertEquals("Kliwon", info.pasaran)
        assertEquals("Rabu Kliwon", info.dayPasaran)
        assertEquals(1, info.javaneseDay)
        assertEquals("Sura", info.javaneseMonth)
        assertEquals(1960, info.javaneseYear)
        assertEquals("Be", info.winduYearName)
    }

    @Test
    fun testSura1958Je8July2024() {
        // 8 Juli 2024 adalah 1 Sura 1958 Je (Senin Legi)
        val date = LocalDate.of(2024, 7, 8)
        val info = JavaneseCalendarHelper.calculate(date)

        assertEquals("Senin", info.dayName)
        assertEquals("Legi", info.pasaran)
        assertEquals("Senin Legi", info.dayPasaran)
        assertEquals(1, info.javaneseDay)
        assertEquals("Sura", info.javaneseMonth)
        assertEquals(1958, info.javaneseYear)
        assertEquals("Je", info.winduYearName)
    }
}
