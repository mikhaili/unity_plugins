package com.ironsrc.notify;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.util.Log;


public class Notify {
    static final String TAG = "Notify";

    public void cancelNotification(Context context, int notificationId) {
        if (notificationId < 0) {
            Log.e(TAG, "Invalid notification ID");
            return;
        }
        Scheduler
                .getInstance()
                .cancel(context,
                        Event.createCancellationEvent(context, notificationId));


        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm != null) {
            nm.cancel(notificationId);
        }
    }

    public void scheduleNotification(Context context,
                                     Notification notification) {
        PendingIntent intent = Event.createPendingEvent(context, notification);

        Scheduler.getInstance().schedule(context,
                intent,
                new Scheduler.Options(notification.action.triggerAtMillis, AlarmManager.RTC));
    }

    public static class Action {
        long triggerAtMillis;
        String url;
        long expirationAtMills;
        String triggerActivity;

        public Action(long triggerAtMillis,
                      String url,
                      long expirationAtMills,
                      String triggerActivity
        ) {
            this.triggerAtMillis = triggerAtMillis;
            this.url = url;
            this.expirationAtMills = expirationAtMills;
            this.triggerActivity = triggerActivity;
        }

    }

    public static class Assets {
        String title;
        String text;
        int icon;

        public Assets(String title,
                      String text,
                      int icon
        ) {
            this.text = text;
            this.title = title;
            this.icon = icon;
        }
    }

    public static class Notification {
        int id;
        Assets assets;
        Action action;

        public Notification(int id,
                            Assets assets,
                            Action action
        ) {
            this.id = id;
            this.assets = assets;
            this.action = action;
        }
    }
}
