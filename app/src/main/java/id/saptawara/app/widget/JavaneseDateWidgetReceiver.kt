package id.saptawara.app.widget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import id.saptawara.app.scheduler.MidnightAlarmScheduler

/**
 * Receiver resmi penyedia widget Glance yang terdaftar di AndroidManifest.
 * Bertanggung jawab mengikat GlanceAppWidget ke siklus hidup AppWidgetManager.
 */
class JavaneseDateWidgetReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget = JavaneseDateWidget()

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        // Saat widget pertama kali diletakkan di Home Screen, jadwalkan alarm pergantian hari tepat 00:00
        MidnightAlarmScheduler.scheduleMidnightUpdate(context)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        // Saat widget terakhir dihapus dari Home Screen, bersihkan alarm untuk menghemat daya
        MidnightAlarmScheduler.cancelMidnightUpdate(context)
    }
}
