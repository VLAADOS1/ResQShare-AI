package com.resq

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

object Notif {

    private var ctx: Context? = null
    private const val CH = "chat"

    fun init(c: Context) {
        ctx = c.applicationContext
        val nm = c.getSystemService(NotificationManager::class.java)
        nm.createNotificationChannel(NotificationChannel(CH, "Messages", NotificationManager.IMPORTANCE_HIGH))
    }

    fun show(title: String, body: String) {
        val c = ctx ?: return
        if (Build.VERSION.SDK_INT >= 33 &&
            ContextCompat.checkSelfPermission(c, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        val n = NotificationCompat.Builder(c, CH)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()
        try {
            NotificationManagerCompat.from(c).notify((System.currentTimeMillis() % 100000).toInt(), n)
        } catch (e: SecurityException) {
        }
    }
}
