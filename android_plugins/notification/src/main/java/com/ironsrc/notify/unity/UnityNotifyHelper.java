package com.ironsrc.notify.unity;

//import android.app.Activity
//import android.content.Context
//import android.util.Log
//import com.ironsrc.notify.Notify
//

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.ironsrc.notify.Notify;

import java.lang.reflect.Field;

public class UnityNotifyHelper {

    private static final String TAG = UnityNotifyHelper.class.getSimpleName();

    public void cancelNotification(int notificationId) {
        Notify notify = new Notify();
        notify.cancelNotification(getContext(), notificationId);
    }

    public void scheduleNotification(int id,
                                     String title,
                                     String text,
                                     String iconName,
                                     long triggerAtMillis,
                                     String url,
                                     long expirationAtMills) {
        Context context = getContext();

        int icon = context.getResources().getIdentifier(iconName, "drawable", context.getPackageName());
        if(icon == 0) {
            Log.e(TAG, "Icon NOT exist, please provide valid icon name, the schedule is FAILED");
        }

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

        notify.scheduleNotification(context, notification);
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