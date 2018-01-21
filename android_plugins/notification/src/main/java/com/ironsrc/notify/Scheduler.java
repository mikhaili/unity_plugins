package com.ironsrc.notify;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

class Scheduler {
    private Scheduler() {

    }

    static Scheduler getInstance() {
        return new Scheduler();
    }

    void schedule(Context context, PendingIntent intent, Scheduler.Options options) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
        alarmManager.cancel(intent);

        alarmManager.set(options.alarmType,
                System.currentTimeMillis() + options.triggerAtMillis,
                intent);
    }

    void cancel(Context context, PendingIntent intent) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
        alarmManager.cancel(intent);
    }

    static class Options {
        long triggerAtMillis;
        int alarmType;

        Options(long triggerAtMillis, int alarmType) {
            this.triggerAtMillis = triggerAtMillis;
            this.alarmType = alarmType;
        }
    }

}