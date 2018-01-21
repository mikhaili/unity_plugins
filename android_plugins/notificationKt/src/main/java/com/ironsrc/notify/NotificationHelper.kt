package com.ironsrc.notify

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import android.os.Build
import android.support.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.O)
class NotificationHelper(base: Context) : ContextWrapper(base) {

    companion object {
        val CHANNEL_ONE_ID = "com.jessicathornsby.myapplication.ONE";
        val CHANNEL_ONE_NAME = "Channel One";
        val CHANNEL_TWO_ID = "com.jessicathornsby.myapplication.TWO";
        val CHANNEL_TWO_NAME = "Channel Two";
    }

    init {
        createChannels()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannels() {
        val notificationChannel = NotificationChannel(CHANNEL_ONE_ID,
                CHANNEL_ONE_NAME, NotificationManager.IMPORTANCE_HIGH).apply {
            enableLights(true)
            lightColor = Color.RED
            setShowBadge(true)
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        }
        notifManager.createNotificationChannel(notificationChannel)

        val notificationChannel2 = NotificationChannel(CHANNEL_TWO_ID,
                CHANNEL_TWO_NAME, NotificationManager.IMPORTANCE_DEFAULT).apply {
            enableLights(false)
            enableVibration(true)
            lightColor = Color.RED
            setShowBadge(false)
        }

        notifManager.createNotificationChannel(notificationChannel2);
    }

//Create the notification that’ll be posted to Channel One//

    fun getNotification1(
            title: String,
            body: String): Notification.Builder {

        return Notification.Builder(applicationContext, CHANNEL_ONE_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.abc_btn_colored_material)
                .setAutoCancel(true)
    }

//Create the notification that’ll be posted to Channel Two//

    fun getNotification2(title: String, body: String): Notification.Builder {
        return Notification.Builder(applicationContext, CHANNEL_TWO_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.abc_ic_clear_material)
                .setAutoCancel(true)
    }


    fun notify(id: Int, notification: Notification.Builder) {
        notifManager.notify(id, notification.build());
    }

    private var _notifManager: NotificationManager? = null
    val notifManager: NotificationManager
        get() {
            if (_notifManager == null) {
                _notifManager = (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
            }
            return _notifManager ?: throw AssertionError("Set to null by another thread")
        }
}