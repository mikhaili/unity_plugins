package paytax.app

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ironsrc.notify.Notify
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val notify = Notify()

        btnNotify.setOnClickListener { view ->
            val valueId = notifyId.text.toString().toInt()
            val valueText = notifyText.text.toString()
            val valueTitle = notifyTitle.text.toString()
            val valueTime = notifyTime.text.toString().toLong()
            val valueUrl = notifyUrl.text.toString()
            val expirationTime = expirationTime.text.toString().toLongOrNull() ?: -1L


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
