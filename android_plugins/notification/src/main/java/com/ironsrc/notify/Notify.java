package com.ironsrc.notify;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;


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

    public String getExtras(final Context context) {
        if(!(context instanceof Activity)) {
            return "";
        }

        boolean DEBUG = false;
        try {

            Intent intent = ((Activity)context).getIntent();
            if (intent == null) {
                if (DEBUG) Log.e(TAG, "Intent is null");
                return null;
            }
            JSONObject json = new JSONObject();
            Bundle extras = intent.getExtras();
            if (extras == null) {
                if (DEBUG) Log.e(TAG, "Extras is null");
            } else if (extras.size() <= 0) {
                if (DEBUG) Log.d(TAG, "Extras is empty");
            } else {
                bundle2JSON(json, extras, DEBUG);
            }
            String action = intent.getAction();
            if (!TextUtils.isEmpty(action)) {
                json.put("Action", action);
            }
            String data = intent.getDataString();
            if (!TextUtils.isEmpty(data)) {
                json.put("Data", data);
            }
            return json.toString();
        } catch (Exception e) {
            if (DEBUG) Log.e(TAG, "Failed to get extras " + e.toString());
            return null;
        }
    }

    private void bundle2JSON(JSONObject json, Bundle bundle, boolean DEBUG) {
        if ((bundle == null) || (bundle.size() <= 0)) {
            return;
        }
        Set<String> keys = bundle.keySet();
        for (String key : keys) {
            try {
                if (DEBUG) Log.d(TAG, "Bundle '" + key + "': '" + bundle.get(key) + "'");
                Object value = bundle.get(key);
                if (value instanceof Bundle) {
                    JSONObject nestedJson = new JSONObject();
                    Bundle nestedBundle = (Bundle) value;
                    bundle2JSON(nestedJson, nestedBundle, DEBUG);
                    value = nestedJson;
                }
                json.put(key, value);
            } catch (JSONException e) {
                if (DEBUG) Log.e(TAG, "Failed to build json " + e.toString());
                break;
            }
        }
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
