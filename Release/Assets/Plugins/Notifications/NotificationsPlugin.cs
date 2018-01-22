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
	/// Schedule notification
    /// </summary>
    /// <param name="id">Notification ID. Can be used to cancel the notification.</param>
	/// <param name="title">Title of the notification.</param>
	/// <param name="text">Text of the notification.</param>
	/// <param name="iconName">Icon of the notification.</param>
	/// <param name="triggerAtMillis">Time to trigger notification.</param>
	/// <param name="url">Url to be engage</param>
	/// <param name="expirationAtMills">Expiration of the notification.</param>
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

		/// <summary>
		/// Cancel notification
		/// </summary>
		/// <param name="id">Notification ID of the notification.</param>
        public static void CancelNotification(int id) {
#if UNITY_EDITOR
#elif UNITY_ANDROID
        notifyPlugin.Call("cancelNotification", id);
#elif UNITY_IPHONE
#endif
    }
}
