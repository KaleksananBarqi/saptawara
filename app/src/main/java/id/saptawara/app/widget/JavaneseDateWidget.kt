package id.saptawara.app.widget

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalSize
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.color.ColorProvider
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontFamily
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import id.saptawara.app.MainActivity
import id.saptawara.app.R
import id.saptawara.app.calendar.JavaneseCalendarHelper
import id.saptawara.app.calendar.JavaneseDateInfo

/**
 * Implementasi Home Screen Widget berbasis Jetpack Glance dengan estetika
 * Nothing OS: monokromatis industrialis, sudut membulat 20dp, font monospace,
 * dan aksen dot merah (#D71921).
 *
 * Mendukung format responsif:
 * - Format Kotak 2x2 (Square): Untuk slot grid 2x2 compact.
 * - Format Lebar 4x2 (Wide): Untuk slot horizontal melebar.
 */
class JavaneseDateWidget : GlanceAppWidget(errorUiLayout = R.layout.glance_default_loading_layout) {

    companion object {
        // Breakpoint ukuran widget untuk Glance SizeMode.Responsive
        val SQUARE_2X2 = DpSize(130.dp, 130.dp)
        val WIDE_4X2 = DpSize(240.dp, 90.dp)
    }

    override val sizeMode: SizeMode = SizeMode.Responsive(
        setOf(SQUARE_2X2, WIDE_4X2)
    )

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        // Hitung informasi kalender Jawa & Masehi hari ini
        val calendarInfo = JavaneseCalendarHelper.calculate()

        provideContent {
            GlanceTheme {
                WidgetContent(calendarInfo)
            }
        }
    }

    override fun onCompositionError(
        context: Context,
        glanceId: GlanceId,
        appWidgetId: Int,
        throwable: Throwable
    ) {
        super.onCompositionError(context, glanceId, appWidgetId, throwable)
        Log.e("JavaneseDateWidget", "Error saat komposisi widget: ${throwable.message}", throwable)
    }

    @Composable
    private fun WidgetContent(info: JavaneseDateInfo) {
        val size = LocalSize.current
        if (size.width >= 210.dp) {
            WideWidgetContent(info)
        } else {
            SquareWidgetContent(info)
        }
    }

    /**
     * Tampilan Kotak 2x2: Tata letak padat vertikal dengan pemecahan baris
     * informasi kalender Jawa menjadi dua baris agar muat presisi.
     */
    @Composable
    private fun SquareWidgetContent(info: JavaneseDateInfo) {
        val white = Color(0xFFFFFFFF)
        val lightGray = Color(0xFFE0E0E0)
        val mutedText = Color(0x99FFFFFF)
        val dividerColor = Color(0x22FFFFFF)

        Box(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(ImageProvider(R.drawable.widget_background))
                .cornerRadius(20.dp)
                .padding(14.dp)
                .clickable(actionStartActivity<MainActivity>()),
            contentAlignment = Alignment.CenterStart
        ) {
            Column(
                modifier = GlanceModifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Header: Nama Hari & Pasaran (Uppercase Sans-Serif Bold 17sp) + Titik Merah Nothing OS
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = GlanceModifier.fillMaxWidth()
                ) {
                    Text(
                        text = info.dayPasaran.uppercase(),
                        style = TextStyle(
                            color = ColorProvider(day = white, night = white),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif
                        )
                    )

                    Spacer(modifier = GlanceModifier.width(6.dp))

                    Image(
                        provider = ImageProvider(R.drawable.red_indicator_dot),
                        contentDescription = null,
                        modifier = GlanceModifier.size(7.dp)
                    )
                }

                Spacer(modifier = GlanceModifier.height(3.dp))

                // Tanggal Masehi Lengkap (Monospace 12sp)
                Text(
                    text = info.masehiDate,
                    style = TextStyle(
                        color = ColorProvider(day = lightGray, night = lightGray),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily.Monospace
                    ),
                    maxLines = 1
                )

                Spacer(modifier = GlanceModifier.height(6.dp))

                // Garis Pemisah Halus
                Box(
                    modifier = GlanceModifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(ColorProvider(day = dividerColor, night = dividerColor))
                ) {}

                Spacer(modifier = GlanceModifier.height(6.dp))

                // Baris Jawa 1: Tanggal Bulan Tahun Jawa Sultan Agung (Monospace 11sp)
                Text(
                    text = info.formattedJavaneseDate,
                    style = TextStyle(
                        color = ColorProvider(day = mutedText, night = mutedText),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = FontFamily.Monospace
                    ),
                    maxLines = 1
                )

                Spacer(modifier = GlanceModifier.height(2.dp))

                // Baris Jawa 2: Siklus Wuku & Bobot Total Neptu (Monospace 11sp)
                Text(
                    text = "Wuku ${info.wuku} • Neptu ${info.totalNeptu}",
                    style = TextStyle(
                        color = ColorProvider(day = mutedText, night = mutedText),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = FontFamily.Monospace
                    ),
                    maxLines = 1
                )
            }
        }
    }

    /**
     * Tampilan Lebar 4x2: Tata letak horizontal satu baris untuk teks sekunder.
     */
    @Composable
    private fun WideWidgetContent(info: JavaneseDateInfo) {
        val white = Color(0xFFFFFFFF)
        val lightGray = Color(0xFFE0E0E0)
        val mutedText = Color(0x99FFFFFF)
        val dividerColor = Color(0x22FFFFFF)

        Box(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(ImageProvider(R.drawable.widget_background))
                .cornerRadius(20.dp)
                .padding(16.dp)
                .clickable(actionStartActivity<MainActivity>()),
            contentAlignment = Alignment.CenterStart
        ) {
            Column(
                modifier = GlanceModifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Baris Utama: Nama Hari & Pasaran + Titik Merah Ikonik
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = GlanceModifier.fillMaxWidth()
                ) {
                    Text(
                        text = info.dayPasaran.uppercase(),
                        style = TextStyle(
                            color = ColorProvider(day = white, night = white),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif
                        )
                    )

                    Spacer(modifier = GlanceModifier.width(8.dp))

                    Image(
                        provider = ImageProvider(R.drawable.red_indicator_dot),
                        contentDescription = null,
                        modifier = GlanceModifier.size(8.dp)
                    )
                }

                Spacer(modifier = GlanceModifier.height(4.dp))

                // Baris Tanggal Masehi Lengkap
                Text(
                    text = info.masehiDate,
                    style = TextStyle(
                        color = ColorProvider(day = lightGray, night = lightGray),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily.Monospace
                    )
                )

                Spacer(modifier = GlanceModifier.height(8.dp))

                // Pemisah Garis Tipis Halus
                Box(
                    modifier = GlanceModifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(ColorProvider(day = dividerColor, night = dividerColor))
                ) {}

                Spacer(modifier = GlanceModifier.height(8.dp))

                // Baris Sekunder: Tanggal Sultan Agung, Wuku, dan Neptu dalam satu baris
                Text(
                    text = info.secondaryText,
                    style = TextStyle(
                        color = ColorProvider(day = mutedText, night = mutedText),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = FontFamily.Monospace
                    ),
                    maxLines = 1
                )
            }
        }
    }
}
