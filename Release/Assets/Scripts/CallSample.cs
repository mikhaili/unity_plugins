using UnityEngine;
using System;
using System.Collections.Generic;

public class CallSample : MonoBehaviour {

    [Tooltip("Hour ranges are ranges that notifications are allowed")]
    public List<Range> hourRanges;

    [Serializable]
    public class Range {
        public string from;
        public string to;
    }

	public void ScheduleNotification() {
		Debug.Log("NotificationsPlugin | ScheduleNotification"); 
		NotificationsPlugin.ScheduleNotification(
			123,
			"Hello world 3000",
			"Yo Yo Yo",
			"notify_icon_small",
			3000,
			"http://google.com");
	}

	public void ScheduleNotificationWithExpiration() {
		Debug.Log("NotificationsPlugin | ScheduleNotification"); 
		NotificationsPlugin.ScheduleNotification(
			123,
			"Hello world 10000",
			"Yo Yo Yo",
			"notify_icon_small",
			3000,
			"http://google.com",
			10000
		);
	}

	public void Cancel() {
		Debug.Log("NotificationsPlugin | Cancel"); 
		NotificationsPlugin.CancelNotification(123);
	}


	void Start () {
//		ScheduleNotification ();
	}

    private DateTime FindValidTime(DateTime desiredTime) {
        if (hourRanges.Count == 0) return desiredTime;

        int hours = desiredTime.Hour;
        int minutes = hours * 60 + desiredTime.Minute;

        int bestadd = -1;

        foreach (var hourRange in hourRanges) {
            string[] tokens = hourRange.from.Split(':');
            int fromMinutes = int.Parse(tokens[0]) * 60 + int.Parse(tokens[1]);
            tokens = hourRange.to.Split(':');
            int toMinutes = int.Parse(tokens[0]) * 60 + int.Parse(tokens[1]);

            // Used for figuring how many minutes should be added to make it fit to this entry.
            int needadd = -1;

            if (fromMinutes <= minutes && minutes <= toMinutes) {
                return desiredTime;
            }

            needadd = fromMinutes - minutes;
            if (needadd < 0) {
                // Move notification to tomorrow.
                needadd += 24 * 60;
            }

            if (bestadd == -1 || (needadd > 0 && bestadd > needadd)) {
                // Add minutes to make it fit (in case no match found in array).
                bestadd = needadd;
            }
        }

        // Try to match the closest hour-range.
        if (bestadd > 0) {
            desiredTime = desiredTime.AddMinutes(bestadd);
        }
        return desiredTime;
    }
}
