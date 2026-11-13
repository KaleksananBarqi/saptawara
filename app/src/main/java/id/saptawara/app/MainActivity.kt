package id.saptawara.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.appwidget.updateAll
import id.saptawara.app.calendar.JavaneseCalendarHelper
import id.saptawara.app.calendar.JavaneseDateInfo
import id.saptawara.app.scheduler.MidnightAlarmScheduler
import id.saptawara.app.widget.JavaneseDateWidget
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Pastikan alarm jam 00:00 sudah aktif saat aplikasi dibuka
        MidnightAlarmScheduler.scheduleMidnightUpdate(this)

        setContent {
            SaptawaraAppTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun SaptawaraAppTheme(content: @Composable () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF0D0D0D)
    ) {
        content()
    }
}

@Composable
fun MainScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val calendarInfo = remember { JavaneseCalendarHelper.calculate() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Header Aplikasi bergaya Nothing OS
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "saptawara",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                color = Color.White
            )
            Spacer(modifier = Modifier.width(6.dp))
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(Color(0xFFD71921), CircleShape)
            )
        }

        Text(
            text = "PENANGGALAN JAWA & MASEHI",
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            color = Color(0x99FFFFFF),
            letterSpacing = 1.5.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Label Pratinjau Widget
        Text(
            text = "PRATINJAU WIDGET",
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            color = Color(0x80FFFFFF),
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Komponen Pratinjau Widget Home Screen
        WidgetPreviewCard(calendarInfo)

        Spacer(modifier = Modifier.height(32.dp))

        // Rincian Data Kalender Jawa Hari Ini
        Text(
            text = "RINCIAN HISAB HARI INI",
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            color = Color(0x80FFFFFF),
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        DetailCard(calendarInfo)

        Spacer(modifier = Modifier.height(28.dp))

        // Tombol Perbarui Widget Manual
        Button(
            onClick = {
                scope.launch {
                    JavaneseDateWidget().updateAll(context)
                    MidnightAlarmScheduler.scheduleMidnightUpdate(context)
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF222222),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(12.dp))
        ) {
            Text(
                text = "SINKRONKAN WIDGET SEKARANG",
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun WidgetPreviewCard(info: JavaneseDateInfo) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xCC0D0D0D), RoundedCornerShape(20.dp))
            .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(20.dp))
            .padding(20.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = info.dayPasaran.uppercase(),
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(Color(0xFFD71921), CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = info.masehiDate,
                color = Color(0xFFE0E0E0),
                fontSize = 14.sp,
                fontFamily = FontFamily.Monospace
            )

            Spacer(modifier = Modifier.height(10.dp))

            HorizontalDivider(
                color = Color(0x22FFFFFF),
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = info.secondaryText,
                color = Color(0x99FFFFFF),
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}

@Composable
fun DetailCard(info: JavaneseDateInfo) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF141414), RoundedCornerShape(16.dp))
            .border(1.dp, Color(0x22FFFFFF), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            DetailRow(label = "Saptawara (Hari)", value = "${info.dayName} (Neptu ${info.dayNeptu})")
            DetailRow(label = "Pancawara (Pasaran)", value = "${info.pasaran} (Neptu ${info.pasaranNeptu})")
            DetailRow(label = "Total Neptu Weton", value = "${info.totalNeptu}")
            DetailRow(label = "Wuku (Siklus 210 Hari)", value = info.wuku)
            DetailRow(label = "Kalender Sultan Agung", value = info.formattedJavaneseDate)
            DetailRow(label = "Kurup Acuan", value = "Asapon (Alip Selasa Pon)")
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            color = Color(0x80FFFFFF)
        )
        Text(
            text = value,
            fontSize = 13.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
    }
}
