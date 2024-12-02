package com.dol.itrack.notification.foregroundservice

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import com.dol.itrack.notification.NotificationUtils.showNotification


class ForegroundNotificationService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(1, createServiceNotification())

        // Schedule notifications using a Handler or Timer
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                showNotification(applicationContext)
                handler.postDelayed(this, 20 * 1000) // 1 hour
            }
        }
        handler.post(runnable)

        return START_STICKY
    }

    private fun createServiceNotification(): Notification {
        val CHANNEL_ID = "SampleID"
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Notification Service")
            .setContentText("Service is running...")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
