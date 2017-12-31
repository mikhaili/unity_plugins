package com.ironsrc.notify

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.util.Log

class NotificationHandler(val context: Context,
                          private val intent: Intent) {
    val _DEBUG = intent.getBooleanExtra(Event.FIELD_OPTION_DEBUG, false)
    private val notificationId = intent.getIntExtra(Event.FIELD_ID, -1)

    @Throws(Exception::class)

    internal fun handle(action: String) {
        when (action) {
            "CANCEL_NOTIFICATION" -> cancelNotification()
            "SCHEDULE_NOTIFICATION" -> {
                scheduleNotification()
                intent.getLongExtra(Event.FIELD_OPTION_NOTIFY_TTL, -1L)
                        .takeIf { it >= 0 }
                        ?.also {
                            scheduleNotificationExpiration(it)
                        }
            }
            else -> {
                throw Exception("Not supported action")
            }
        }
    }


    companion object {
        val TAG = "NotificationHandler"
    }

    private fun cancelNotification() {
        notificationId.takeIf { it >= 0 }
                ?.also {
                    (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).cancel(it)
                }
    }

    @Throws(Exception::class)
    private fun scheduleNotification() {
        notificationId.takeIf { it > 0 }
                ?.also {
                    createNotification()?.also {
                        (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                                .notify(notificationId, it)
                    } ?: throw Exception("Failed scheduler notification")
                }
    }

    @Throws(IllegalArgumentException::class)
    private fun createNotification(): Notification? {
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


            NotificationCompat.Builder(context).apply {
                setSmallIcon(icon)
                setContentTitle(title)
                setContentText(text)
                setDefaults(Notification.DEFAULT_SOUND)
                setAutoCancel(true)
                setOnlyAlertOnce(true)
                setContentIntent(PendingIntent.getActivity(context,
                        0,
                        unityIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT))

                info?.also { setContentInfo(it) }

                try {
                    setLargeIcon(BitmapFactory.decodeResource(context.resources, icon))
                } catch (e: Exception) {
                    if (_DEBUG) Log.e(TAG, "Failed to set large icon $e")
                }

                return android.os.Build.VERSION.SDK_INT
                        .takeIf { it < Build.VERSION_CODES.JELLY_BEAN }
                        ?.let { notification }
                        ?: build()
            }


        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }


    private fun scheduleNotificationExpiration(ttl: Long) {
        Scheduler.getInstance(context).apply {
            intent.action = "CANCEL_NOTIFICATION"
            val pendingIntent = PendingIntent.getBroadcast(context,
                    notificationId,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT)
            schedule(context,
                    pendingIntent,
                    Scheduler.Options(triggerAtMillis = ttl))

        }
    }

}