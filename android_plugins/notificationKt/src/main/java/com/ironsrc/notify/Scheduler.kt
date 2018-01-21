package com.ironsrc.notify

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context


open class Scheduler private constructor(context: Context) {
    init {
        // Init using context argument
    }

    companion object : SingletonHolder<Scheduler, Context>(::Scheduler)
    data class Options(val triggerAtMillis: Long, val alarmType: Int = AlarmManager.RTC_WAKEUP)

    fun schedule(context: Context, intent: PendingIntent, options: Scheduler.Options) {
        val alarmManager = context.getSystemService(Activity.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(intent)

        alarmManager.set(options.alarmType,
                System.currentTimeMillis() + options.triggerAtMillis,
                intent)
    }

    fun cancel(context: Context, intent: PendingIntent) {
        val alarmManager = context.getSystemService(Activity.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(intent)
    }

}