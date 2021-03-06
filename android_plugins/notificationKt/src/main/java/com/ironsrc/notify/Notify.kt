package com.ironsrc.notify

import android.app.NotificationManager
import android.content.Context


class Notify {

    fun cancelNotification(context: Context, notificationId: Int) {
        notificationId.takeIf { it >= 0 }
                ?.let {
                    Scheduler
                            .getInstance(context)
                            .cancel(context,
                                    Event.createCancellationEvent(context, it))
                    (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                            .cancel(it)

                } ?: print("Invalid notification ID")
    }

    fun scheduleNotification(context: Context,
                             notification: Notification) {
        val intent = Event.createPendingEvent(context, notification)

        val alarmManager = Scheduler.getInstance(context)
        alarmManager.schedule(context,
                intent,
                Scheduler.Options(triggerAtMillis = notification.action.triggerAtMillis))
    }

    data class Action(val triggerAtMillis: Long,
                      val url: String = "",
                      val expirationAtMills: Long = -1L,
                      val triggerActivity: String = ""
    )

    data class Assets(val title: String,
                      val text: String,
                      val icon: Int
    )

    data class Notification(val id: Int,
                            val assets: Assets,
                            val action: Action
    )
}
