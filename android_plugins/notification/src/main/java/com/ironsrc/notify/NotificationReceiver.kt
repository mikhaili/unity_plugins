package com.ironsrc.notify

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val notificationHandler = NotificationHandler(context, intent)

        try {
            notificationHandler.handle(intent.action)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
