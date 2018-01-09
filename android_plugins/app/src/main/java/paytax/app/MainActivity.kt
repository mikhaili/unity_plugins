package paytax.app

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import com.ironsrc.notify.NotificationHelper
import com.ironsrc.notify.Notify
import com.ironsrc.notify.unity.UnityNotifyHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        useOfNotify()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun useForOreo() {
        Build.VERSION.SDK_INT
                .takeIf {
                    it >= Build.VERSION_CODES.O
                }?.let {
            val notificationHelper = NotificationHelper(this)
            val notification2 = notificationHelper.getNotification2(title = "getNotification2",
                    body = "getNotification2")
            notificationHelper.notify(id = 1, notification = notification2)
            val notification3 = notificationHelper.getNotification2(title = "notification3",
                    body = "notification3")
            notificationHelper.notify(id = 3, notification = notification3)
            val notification1 = notificationHelper.getNotification1(title = "getNotification1",
                    body = "getNotification1")
            notificationHelper.notify(id = 2, notification = notification1)
            val notification4 = notificationHelper.getNotification1(title = "notification4",
                    body = "notification4")
            notificationHelper.notify(id = 4, notification = notification4)
        }

    }

    fun useOfNotify() {
        val notify = Notify()

        btnNotify.setOnClickListener { view ->
            val valueId = notifyId.text.toString().toInt()
            val valueText = notifyText.text.toString()
            val valueTitle = notifyTitle.text.toString()
            val valueTime = notifyTime.text.toString().toLong()
            val valueUrl = notifyUrl.text.toString()
            val expirationTime = expirationTime.text.toString().toLongOrNull() ?: -1L
            UnityNotifyHelper.createActionObj(1000)

            val notification = Notify.Notification(valueId,
                    Notify.Assets(valueText,
                            valueTitle,
                            R.drawable.ic_launcher_background),

                    Notify.Action(triggerAtMillis = valueTime,
                            url = valueUrl,
                            triggerActivity = "paytax.app.MainActivity",
                            expirationAtMills = expirationTime
                    )
            )

            notify.scheduleNotification(this, notification)
        }

        btnCancel.setOnClickListener {
            val id = notifyId.text.toString().toInt()
            notify.cancelNotification(this, id)
        }

        btnClear.setOnClickListener {
            notifyId.text.clear()
            notifyText.text.clear()
            notifyTitle.text.clear()
            notifyTime.text.clear()
            notifyUrl.text.clear()
        }

    }
}
