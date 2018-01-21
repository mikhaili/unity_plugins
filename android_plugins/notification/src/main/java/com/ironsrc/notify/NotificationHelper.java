package com.ironsrc.notify;


import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

class NotificationHelper {
    private static final String TAG = "NotificationHelper";
    boolean _DEBUG = false;

    public NotificationHelper(boolean debug) {
        _DEBUG = debug;
    }

    public Notification createNotification(Context context, Intent intent, int notificationId) throws Exception {
        String title = intent.getStringExtra(Event.FIELD_TITLE);
        String text = intent.getStringExtra(Event.FIELD_TEXT);

        if (TextUtils.isEmpty(title) && TextUtils.isEmpty(text) && notificationId != -1) {
            return null;
        }

        String info = intent.getStringExtra(Event.FIELD_INFO);
        String url = intent.getStringExtra(Event.FIELD_URL);
        int icon = intent.getIntExtra(Event.FIELD_ICON, context.getApplicationInfo().icon);

        String triggerActivity = intent.getStringExtra(Event.FIELD_OPTION_ACTIVITY);

        Intent unityIntent = null;
        if (!TextUtils.isEmpty(triggerActivity)) {
            Class unityPlayerClass = Class.forName(triggerActivity);
            unityIntent = Event.create(context, unityPlayerClass, notificationId, text, title, url, icon);
        }

        if (!TextUtils.isEmpty(url)) {
            unityIntent = new Intent(Intent.ACTION_VIEW);
            unityIntent.setData(Uri.parse(url));
        }
        if (unityIntent == null) {
            throw new IllegalArgumentException("Not defined intent type");
        }


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentIntent(PendingIntent.getActivity(context,
                        0,
                        unityIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT));

        if (!TextUtils.isEmpty(info)) {
            builder.setContentInfo(info);
        }

        try {
            builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), icon));
        } catch (Exception e) {
            if (_DEBUG) Log.e(TAG, "Failed to set large icon $e");
        }

        return (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) ?
                builder.getNotification() : builder.build();

    }

}


//
//import android.app.Notification
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.content.Context
//import android.content.ContextWrapper
//import android.graphics.Color
//import android.os.Build
//import android.support.annotation.RequiresApi
//
//@RequiresApi(Build.VERSION_CODES.O)
//class NotificationHelper(base: Context) : ContextWrapper(base) {
//
//    companion object {
//        val CHANNEL_ONE_ID = "com.jessicathornsby.myapplication.ONE";
//        val CHANNEL_ONE_NAME = "Channel One";
//        val CHANNEL_TWO_ID = "com.jessicathornsby.myapplication.TWO";
//        val CHANNEL_TWO_NAME = "Channel Two";
//    }
//
//    init {
//        createChannels()
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun createChannels() {
//        val notificationChannel = NotificationChannel(CHANNEL_ONE_ID,
//                CHANNEL_ONE_NAME, NotificationManager.IMPORTANCE_HIGH).apply {
//            enableLights(true)
//            lightColor = Color.RED
//            setShowBadge(true)
//            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
//        }
//        notifManager.createNotificationChannel(notificationChannel)
//
//        val notificationChannel2 = NotificationChannel(CHANNEL_TWO_ID,
//                CHANNEL_TWO_NAME, NotificationManager.IMPORTANCE_DEFAULT).apply {
//            enableLights(false)
//            enableVibration(true)
//            lightColor = Color.RED
//            setShowBadge(false)
//        }
//
//        notifManager.createNotificationChannel(notificationChannel2);
//    }
//
////Create the notification that’ll be posted to Channel One//
//
//    fun getNotification1(
//            title: String,
//            body: String): Notification.Builder {
//
//        return Notification.Builder(applicationContext, CHANNEL_ONE_ID)
//                .setContentTitle(title)
//                .setContentText(body)
//                .setSmallIcon(R.drawable.abc_btn_colored_material)
//                .setAutoCancel(true)
//    }
//
////Create the notification that’ll be posted to Channel Two//
//
//    fun getNotification2(title: String, body: String): Notification.Builder {
//        return Notification.Builder(applicationContext, CHANNEL_TWO_ID)
//                .setContentTitle(title)
//                .setContentText(body)
//                .setSmallIcon(R.drawable.abc_ic_clear_material)
//                .setAutoCancel(true)
//    }
//
//
//    fun notify(id: Int, notification: Notification.Builder) {
//        notifManager.notify(id, notification.build());
//    }
//
//    private var _notifManager: NotificationManager? = null
//    val notifManager: NotificationManager
//        get() {
//            if (_notifManager == null) {
//                _notifManager = (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
//            }
//            return _notifManager ?: throw AssertionError("Set to null by another thread")
//        }
//}