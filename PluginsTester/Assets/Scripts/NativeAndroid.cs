using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class NativeAndroid : MonoBehaviour {

	public void SayMyName () {
		using (AndroidJavaObject helper = new AndroidJavaObject ("com.ironsrc.notify.unity.UnityNotifyHelper")) { 
			AndroidJavaObject actionObject = helper.CallStatic<AndroidJavaObject>("createActionObj", "1000");

			Debug.Log("testFunction url = " + actionObject.Get<string>("url")); 

			helper.Call ("testFunction");
		} 


//		using (AndroidJavaClass cls = new AndroidJavaClass("java.util.Locale")) { 
//			using(AndroidJavaObject locale = cls.CallStatic<AndroidJavaObject>("getDefault")) { 
//				Debug.Log("current lang = " + locale.Call<string>("getDisplayLanguage")); 
//
//			} 
//		}
	}



	// Use this for initialization
	void Start () {
		SayMyName ();
	}
	
	// Update is called once per frame
	void Update () {
		
	}
}
