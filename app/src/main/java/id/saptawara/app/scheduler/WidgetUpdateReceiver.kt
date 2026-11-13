package id.saptawara.app.scheduler

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.glance.appwidget.updateAll
import id.saptawara.app.widget.JavaneseDateWidget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Receiver penerima pemicu pembaruan widget, baik dari alarm tengah malam
 * maupun event sistem Android (reboot, ganti zona waktu, ganti jam manual).
 */
class WidgetUpdateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        Log.d(TAG, "Menerima event broadcast: $action")

        val pendingResult = goAsync()

        CoroutineScope(Dispatchers.Default).launch {
            try {
                // Perbarui seluruh instance widget saptawara di Home Screen
                JavaneseDateWidget().updateAll(context)
                Log.d(TAG, "Widget berhasil diperbarui untuk hari baru.")
            } catch (e: Exception) {
                Log.e(TAG, "Gagal memperbarui widget: ${e.message}", e)
            } finally {
                // Jadwalkan kembali alarm untuk tengah malam berikutnya
                MidnightAlarmScheduler.scheduleMidnightUpdate(context)
                pendingResult.finish()
            }
        }
    }

    companion object {
        private const val TAG = "WidgetUpdateReceiver"
    }
}
