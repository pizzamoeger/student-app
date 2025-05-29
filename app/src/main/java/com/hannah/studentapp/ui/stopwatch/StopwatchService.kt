package com.hannah.studentapp.ui.stopwatch

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.PowerManager
import android.util.Log
import androidx.core.app.NotificationCompat

class StopwatchService : Service() {

    private lateinit var wakeLock: PowerManager.WakeLock
    private val handler = Handler(Looper.getMainLooper())
    private var seconds = 0

    private val runnable = object : Runnable {
        override fun run() {
            seconds += 1

            // Send broadcast
            val intent = Intent("com.hannah.studentapp.STOPWATCH_UPDATE")
            intent.putExtra("seconds", seconds)
            sendBroadcast(intent)

            Log.d("aaaaaa", seconds.toString())

            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreate() {
        super.onCreate()

        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "StudentApp::StopwatchWakeLock")
        wakeLock.acquire()

        val channelId = "stopwatch_channel"
        val channel = NotificationChannel(channelId, "Stopwatch", NotificationManager.IMPORTANCE_LOW)
        val nm = getSystemService(NotificationManager::class.java)
        nm.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Stopwatch Running")
            .setContentText("Tracking time...")
            .build()

        startForeground(1, notification)

        handler.post(runnable)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        Log.d("aaaaaa", "started")
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "StudentApp::StopwatchWakeLock")
        wakeLock.acquire()

        val channelId = "stopwatch_channel"
        val channel = NotificationChannel(channelId, "Stopwatch", NotificationManager.IMPORTANCE_LOW)
        val nm = getSystemService(NotificationManager::class.java)
        nm.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Stopwatch Running")
            .setContentText("Tracking time...")
            .build()

        startForeground(1, notification)

        handler.post(runnable)
        return START_STICKY
    }

    override fun onDestroy() {
        handler.removeCallbacks(runnable)
        wakeLock.release()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
