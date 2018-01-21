package com.ironsrc.notify.unity;

//import android.app.Activity
//import android.content.Context
//import android.util.Log
//import com.ironsrc.notify.Notify
//

import android.app.Activity;
import android.content.Context;

import com.ironsrc.notify.Notify;

import java.lang.reflect.Field;

public class UnityNotifyHelper {

    public void cancelNotification(int notificationId) {
        Notify notify = new Notify();
        notify.cancelNotification(getContext(), notificationId);
    }

    public void scheduleNotification(int id,
                                     String title,
                                     String text,
                                     int icon,
                                     long triggerAtMillis,
                                     String url,
                                     long expirationAtMills) {
        Notify notify = new Notify();

        Notify.Action action = new Notify.Action(
                triggerAtMillis,
                url,
                expirationAtMills,
                ""
        );

        Notify.Assets assets = new Notify.Assets(
                title,
                text,
                icon
        );

        Notify.Notification notification = new Notify.Notification(
                id,
                assets, action);
        notify.scheduleNotification(getContext(), notification);
    }


    Context getContext() {

        try {
            Class unityClass = Class.forName("com.unity3d.player.UnityPlayer");
            Field activity = unityClass.getField("currentActivity");
            return (Activity) activity.get(unityClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}