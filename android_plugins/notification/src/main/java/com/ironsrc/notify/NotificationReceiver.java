package com.ironsrc.notify;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            NotificationHandler notificationHandler = new NotificationHandler();
            notificationHandler.handle(context, intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
