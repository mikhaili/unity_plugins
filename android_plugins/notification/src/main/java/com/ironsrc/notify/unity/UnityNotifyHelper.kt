package com.ironsrc.notify.unity

import android.app.Activity
import android.content.Context
import android.util.Log
import com.ironsrc.notify.Notify


object UnityNotifyHelper {
    val TAG = "UnityNotifyHelper"
    fun testFunction() {
        Log.e(TAG, "testFunction | testFunction")
    }

    fun createActionObj(triggerAtMillis: String): Notify.Action {
        return Notify.Action(
                triggerAtMillis = triggerAtMillis.toLong(),
                url = "",
                expirationAtMills = -1L)
    }

    fun createAssetsObj(title: String,
                        text: String,
                        icon: Int): Notify.Assets {
        return Notify.Assets(title, text, icon)
    }

    val context: Context?
        get() {
            try {
                val unityClass = Class.forName("com.unity3d.player.UnityPlayer")
                val activity = unityClass.getField("currentActivity")

                // Gets the reference to the activity.
                return activity.get(unityClass) as Activity

                // Gets the method from the UnityPlayer.
            } catch (e: NoSuchFieldException) {
            } catch (e: NoSuchMethodException) {
            } catch (e: IllegalAccessException) {
                // Manage Exception
            } catch (e: IllegalArgumentException) {
                // Manage Exception
            }
            return null
        }
}