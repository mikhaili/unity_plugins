using UnityEngine;

public static class NotificationsPlugin {
#if UNITY_EDITOR
#elif UNITY_ANDROID
    private static AndroidJavaObject notifyPlugin {
        get {
            if (_notifyPlugin == null) {
                // Instantiate a plugin instance.
				_notifyPlugin = new AndroidJavaObject("com.ironsrc.notify.unity.UnityNotifyHelper");
            }
            return _notifyPlugin;
        }
    }
    private static AndroidJavaObject _notifyPlugin;
#endif

    /// <summary>
    /// Adds the notification.
    /// </summary>
    /// <param name="id">Notification ID. Can be used to cancel the notification.</param>
    /// <param name="when">Notification time in millis from now.</param>
    /// <param name="info">Short text below the notification time on the right (optional).</param>
	public static void ScheduleNotification(int id,
			string title,
			string text,
			string iconName,
			long triggerAtMillis,
			string url,
			long expirationAtMills = -1L) {


#if UNITY_EDITOR
#elif UNITY_ANDROID
		notifyPlugin.Call("scheduleNotification", id, title, text, iconName, triggerAtMillis, url, expirationAtMills);
#elif UNITY_IPHONE
#endif
	}

        public static void CancelNotification(int id) {
#if UNITY_EDITOR
#elif UNITY_ANDROID
        notifyPlugin.Call("cancelNotification", id);
#elif UNITY_IPHONE
#endif
    }
}
