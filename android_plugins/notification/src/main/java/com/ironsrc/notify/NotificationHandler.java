package com.ironsrc.notify;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;


class NotificationHandler {

    private final static String TAG = "NotificationHandler";
    NotificationHelper notificationHelper = null;
    private boolean _DEBUG = false;

    void handle(Context context, Intent intent) throws Exception {
        _DEBUG = intent.getBooleanExtra(Event.FIELD_OPTION_DEBUG, false);
        int notificationId = intent.getIntExtra(Event.FIELD_ID, -1);
        String action = intent.getStringExtra(Event.FIELD_ACTION);
        notificationHelper = new NotificationHelper(_DEBUG);
        switch (action) {
            case "CANCEL_NOTIFICATION":
                cancelNotification(context, notificationId);
                break;
            case "SCHEDULE_NOTIFICATION":
                fireNotification(context, intent, notificationId);
                break;

            case "SCHEDULE_NOTIFICATION_WITH_EXPIRATION":
                fireNotification(context, intent, notificationId);
                scheduleNotificationExpiration(context, intent, notificationId);
                break;
            default:
                throw new Exception("Not supported action action: " + action);
        }
    }

    private void cancelNotification(Context context, int notificationId) throws Exception {
        if (notificationId < 0) {
            return;
        }
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(notificationId);
    }

    private void fireNotification(Context context, Intent intent, int notificationId) throws Exception {
        if (notificationId < 0) {
            throw new Exception("Failed scheduler notification " + notificationId);
        }

        Notification notification = notificationHelper.createNotification(context, intent, notificationId);
        if (notification != null) {
            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(notificationId, notification);
            return;
        }
        throw new Exception("Failed fireNotification notification");
    }


    private void scheduleNotificationExpiration(Context context, Intent intent, int notificationId) {
        long expirationTime = intent.getLongExtra(Event.FIELD_OPTION_NOTIFY_TTL, -1L);
        if (expirationTime >= 0) {
            intent.putExtra(Event.FIELD_ACTION, "CANCEL_NOTIFICATION");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    notificationId,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            Scheduler.getInstance().
                    schedule(context,
                            pendingIntent,
                            new Scheduler.Options(expirationTime, AlarmManager.RTC));
        }

    }

}