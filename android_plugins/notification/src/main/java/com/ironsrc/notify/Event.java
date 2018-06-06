package com.ironsrc.notify;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

class Event {

    static String FIELD_TITLE = "title";
    static String FIELD_TEXT = "text";
    static String FIELD_ICON = "icon";
    static String FIELD_INFO = "info";
    static String FIELD_ID = "id";
    static String FIELD_ACTION = "action";

    static String FIELD_OPTION_ACTIVITY = "activity";
    static String FIELD_OPTION_DEBUG = "debug";
    static String FIELD_OPTION_NOTIFY_TTL = "ttl";

    static String FIELD_URL = "createCancellationEvent";

    static PendingIntent createCancellationEvent(Context context, int notificationId) {
        Intent intent = new Intent(context, NotificationReceiver.class);
//        intent.putExtra(FIELD_ID, notificationId);
        return PendingIntent.getBroadcast(context,
                notificationId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    static PendingIntent createPendingEvent(Context context,
                                                   Notify.Notification notification) {
        Intent intent = new Intent(context, NotificationReceiver.class);

        String action = (notification.action.expirationAtMills > -1) ?
                "SCHEDULE_NOTIFICATION_WITH_EXPIRATION"
                : "SCHEDULE_NOTIFICATION";
        intent.putExtra(FIELD_ID, notification.id);
        intent.putExtra(FIELD_ACTION, action);

        intent.putExtra(FIELD_TITLE, notification.assets.title);
        intent.putExtra(FIELD_TEXT, notification.assets.text);
        intent.putExtra(FIELD_ICON, notification.assets.icon);

        intent.putExtra(FIELD_OPTION_ACTIVITY, notification.action.triggerActivity);
        intent.putExtra(FIELD_OPTION_NOTIFY_TTL, notification.action.expirationAtMills);
        intent.putExtra(FIELD_URL, notification.action.url);

        return PendingIntent.getBroadcast(context,
                notification.id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }


    public static final String ID_PARAM = "NotifID";
    public static final String TITLE_PARAM = "NotifTitle";
    public static final String TEXT_PARAM = "NotifText";
    public static final String DATA_PARAM = "Data";
    public static final String ACTION_PARAM = "Action";

    static Intent create(Context context,
                         Class<?> cls,
                         int id,
                         String text,
                         String title,
                         String url,
                         int icon) {
        Intent intent = new Intent(context, cls);
        intent.putExtra(ID_PARAM, id);
        intent.putExtra(TITLE_PARAM, title);
        intent.putExtra(TEXT_PARAM, text);
        intent.putExtra(FIELD_ICON, icon);

        if (TextUtils.isEmpty(url)) {
            intent.putExtra(FIELD_URL, url);
        }
        return intent;
    }

    static Intent create(
            String action,
            int id,
            String url) {
        Intent intent = new Intent(action);
        intent.putExtra(ID_PARAM, id);
        intent.setData(Uri.parse(url));
        return intent;
    }
}