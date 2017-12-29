package com.ironsrc.notify

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.util.Log

class NotificationReceiver : BroadcastReceiver() {
    val TAG = "NotificationReceiver"

    override fun onReceive(context: Context, intent: Intent) {

        val _DEBUG = intent.getBooleanExtra(Event.FIELD_OPTION_DEBUG, false)

        when (intent.action) {
            "CANCEL_NOTIFICATION" -> cancelNotification(context, intent, _DEBUG)
            else -> {
                val notificationId = intent.getIntExtra(Event.FIELD_ID, -1)
                val notification = notificationId.takeIf { it > 0 }?.let { createNotification(context, notificationId, intent, _DEBUG) }

                notification?.let {

                    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.notify(notificationId, notification)
                    val notificationTTL = intent.getLongExtra(Event.FIELD_OPTION_NOTIFY_TTL, -1L)

                    notificationTTL
                            .takeIf { it >= 0 }
                            ?.let {

                                scheduleNotificationExpiration(context,
                                        intent,
                                        notificationTTL,
                                        notificationId)
                            }
                }
            }
        }
    }

    private fun createNotification(context: Context,
                                   notificationId: Int,
                                   intent: Intent, _DEBUG: Boolean): Notification? {
        val title = intent.getStringExtra(Event.FIELD_TITLE)
        val text = intent.getStringExtra(Event.FIELD_TEXT)

        if (title.isNullOrEmpty() && text.isNullOrEmpty() && notificationId != -1) {
            return null
        }

        val info = intent.getStringExtra(Event.FIELD_INFO)
        val url = intent.getStringExtra(Event.FIELD_URL)
        val icon = intent.getIntExtra(Event.FIELD_ICON, context.applicationInfo!!.icon)
        val triggerActivity = intent.getStringExtra(Event.FIELD_OPTION_ACTIVITY)

        try {
            val unityIntent: Intent = when {
                triggerActivity.isNotEmpty() -> {
                    val unityPlayerClass = Class.forName(triggerActivity)
                    Event.create(context, unityPlayerClass, notificationId, title, text, icon = icon)
                }
                url.isNotEmpty() -> {
                    val i = Intent(Intent.ACTION_VIEW)
                    i.data = Uri.parse(url)
                    i
                }
                else -> throw IllegalArgumentException("Not defined intent type")
            }

            val contentIntent = PendingIntent.getActivity(context, 0, unityIntent, PendingIntent.FLAG_UPDATE_CURRENT)

            val builder = NotificationCompat.Builder(context)
                    .setSmallIcon(icon)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setAutoCancel(true)
                    .setOnlyAlertOnce(true)
                    .setContentIntent(contentIntent)

            info?.let { builder.setContentInfo(info) }

            try {
                builder.setLargeIcon(BitmapFactory.decodeResource(context.resources, icon))
            } catch (e: Exception) {
                if (_DEBUG) Log.e(TAG, "Failed to set large icon $e")
            }

            return android.os.Build.VERSION.SDK_INT
                    .takeIf { it < Build.VERSION_CODES.JELLY_BEAN }
                    ?.let { builder.notification }
                    ?: builder.build()

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun cancelNotification(context: Context, intent: Intent, _DEBUG: Boolean) {
        val notificationId = intent.getIntExtra(Event.FIELD_ID, -1)
        notificationId.takeIf { it >= 0 }
                ?.let {
                    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.cancel(notificationId)

                }
    }

    private fun scheduleNotificationExpiration(context: Context,
                                               intent: Intent,
                                               notificationTTL: Long,
                                               notificationId: Int) {
        val alarmManager = Scheduler.getInstance(context)

        intent.action = "CANCEL_NOTIFICATION"

        val pendingIntent = PendingIntent.getBroadcast(context,
                notificationId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.schedule(context,
                pendingIntent,
                Scheduler.Options(triggerAtMillis = notificationTTL))
    }
}
