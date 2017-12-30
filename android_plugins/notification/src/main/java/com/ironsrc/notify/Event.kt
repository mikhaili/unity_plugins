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
        val intent = Intent(context, NotificationReceiver::class.java)

        intent.putExtra(FIELD_ID, notification.id)

        intent.putExtras(updateWithAssets(notification.assets))
        intent.putExtras(updateWithAction(notification.action))

        return PendingIntent.getBroadcast(context,
                notification.id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun updateWithAction(action: Notify.Action): Intent {
        val intent = Intent()
        intent.putExtra(FIELD_OPTION_ACTIVITY, action.triggerActivity)
        intent.putExtra(FIELD_OPTION_NOTIFY_TTL, action.expirationAtMills)
        intent.putExtra(FIELD_URL, action.url)
        return intent
    }

    private fun updateWithAssets(notification: Notify.Assets): Intent {
        val intent = Intent()
        intent.putExtra(FIELD_TITLE, notification.title)
        intent.putExtra(FIELD_TEXT, notification.text)
        intent.putExtra(FIELD_ICON, notification.icon)
        return intent
    }

    fun create(context: Context,
               cls: Class<*>,
               id: Int,
               text: String,
               title: String,
               url: String = "",
               icon: Int): Intent {
        val intent = Intent(context, cls)

        intent.putExtra(FIELD_ID, id)
        intent.putExtra(FIELD_TITLE, title)
        intent.putExtra(FIELD_TEXT, text)
        intent.putExtra(FIELD_ICON, icon)

        url.isNotEmpty().let {
            intent.putExtra(FIELD_URL, it)
        }

        return intent
    }
}