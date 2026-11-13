package id.saptawara.app.scheduler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Pengatur jadwal AlarmManager untuk memicu pembaruan widget secara presisi
 * tepat pada pukul 00:00:01 setiap pergantian hari.
 */
object MidnightAlarmScheduler {

    private const val TAG = "MidnightAlarmScheduler"
    const val ACTION_MIDNIGHT_UPDATE = "id.saptawara.app.action.MIDNIGHT_UPDATE"
    private const val REQUEST_CODE_MIDNIGHT = 1001

    /**
     * Menjadwalkan alarm tepat pada detik pertama hari esok (00:00:01).
     */
    fun scheduleMidnightUpdate(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return

        // Tentukan waktu tengah malam berikutnya (00:00:01)
        val tomorrowMidnight = LocalDate.now()
            .plusDays(1)
            .atTime(0, 0, 1)
        val triggerEpochMillis = tomorrowMidnight
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        val intent = Intent(context, WidgetUpdateReceiver::class.java).apply {
            action = ACTION_MIDNIGHT_UPDATE
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_MIDNIGHT,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                // Pada Android 12+, pastikan aplikasi memiliki izin exact alarm sebelum memanggil setExactAndAllowWhileIdle
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerEpochMillis,
                        pendingIntent
                    )
                } else {
                    // Fallback jika izin exact alarm dimatikan oleh user di pengaturan sistem
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerEpochMillis,
                        pendingIntent
                    )
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerEpochMillis,
                    pendingIntent
                )
            }
            Log.d(TAG, "Alarm tengah malam berhasil dijadwalkan untuk: $tomorrowMidnight")
        } catch (e: SecurityException) {
            Log.e(TAG, "Gagal menjadwalkan exact alarm karena batasan keamanan: ${e.message}")
        }
    }

    /**
     * Membatalkan jadwal alarm ketika tidak ada lagi widget yang aktif.
     */
    fun cancelMidnightUpdate(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = Intent(context, WidgetUpdateReceiver::class.java).apply {
            action = ACTION_MIDNIGHT_UPDATE
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_MIDNIGHT,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
            Log.d(TAG, "Alarm tengah malam berhasil dibatalkan.")
        }
    }
}
