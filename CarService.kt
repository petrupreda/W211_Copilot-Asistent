package com.w211.copilot

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class CarService : Service() {

    private lateinit var obdReader: ObdReader

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == "ACTION_STOP") {
            stopSelf()
            return START_NOT_STICKY
        }

        createNotificationChannel()
        
        val stopIntent = Intent(this, CarService::class.java).apply {
            action = "ACTION_STOP"
        }
        val stopPendingIntent = PendingIntent.getService(
            this, 0, stopIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification: Notification = NotificationCompat.Builder(this, "W211Channel")
            .setContentTitle("W211 Copilot")
            .setContentText("Rulează în fundal - Conectat la mașină")
            .setSmallIcon(android.R.drawable.ic_menu_compass)
            .setOngoing(true)
            .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Oprește", stopPendingIntent)
            .build()

        startForeground(1, notification)

        obdReader = ObdReader()
        obdReader.startReading()

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        obdReader.stopReading()
        stopForeground(true)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                "W211Channel",
                "W211 Copilot Service Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }
    }
}
