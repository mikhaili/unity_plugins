package com.ironsrc.notify

import android.app.PendingIntent
import android.content.Context
import android.content.Intent

object Event {

    val FIELD_TITLE = "title"
    val FIELD_TEXT = "text"
    val FIELD_ICON = "icon"
    val FIELD_INFO = "info"
    val FIELD_ID = "id"

    val FIELD_OPTION_ACTIVITY = "activity"
    val FIELD_OPTION_DEBUG = "debug"
    val FIELD_OPTION_NOTIFY_TTL = "ttl"

    val FIELD_URL = "createCancellationEvent"

    fun createCancellationEvent(context: Context, notificationId: Int): PendingIntent {
        val intent = Intent(context, NotificationReceiver::class.java)
        intent.putExtra(FIELD_ID, notificationId)
        return PendingIntent.getBroadcast(context,
                notificationId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT)
    }

    fun createPendingEvent(context: Context,
                           notification: Notify.Notification): PendingIntent {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            action = notification.action.expirationAtMills.takeIf { it > -1 }?.let {
                "SCHEDULE_NOTIFICATION_WITH_EXPIRATION"
            } ?:"SCHEDULE_NOTIFICATION"

            putExtra(FIELD_ID, notification.id)
            putExtras(updateWithAssets(notification.assets))
            putExtras(updateWithAction(notification.action))
        }

        return PendingIntent.getBroadcast(context,
                notification.id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun updateWithAction(action: Notify.Action): Intent {
        return Intent().apply {
            putExtra(FIELD_OPTION_ACTIVITY, action.triggerActivity)
            putExtra(FIELD_OPTION_NOTIFY_TTL, action.expirationAtMills)
            putExtra(FIELD_URL, action.url)
        }
    }

    private fun updateWithAssets(notification: Notify.Assets): Intent {
        return Intent().apply {
            putExtra(FIELD_TITLE, notification.title)
            putExtra(FIELD_TEXT, notification.text)
            putExtra(FIELD_ICON, notification.icon)
        }
    }

    fun create(context: Context,
               cls: Class<*>,
               id: Int,
               text: String,
               title: String,
               url: String = "",
               icon: Int): Intent {
        return Intent(context, cls).apply {
            putExtra(FIELD_ID, id)
            putExtra(FIELD_TITLE, title)
            putExtra(FIELD_TEXT, text)
            putExtra(FIELD_ICON, icon)

            url.isNotEmpty().also {
                putExtra(FIELD_URL, it)
            }
        }
    }
}