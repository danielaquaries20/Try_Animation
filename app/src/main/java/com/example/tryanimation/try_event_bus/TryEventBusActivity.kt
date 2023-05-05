package com.example.tryanimation.try_event_bus

import android.media.RingtoneManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.databinding.DataBindingUtil
import com.example.tryanimation.R
import com.example.tryanimation.databinding.ActivityTryEventBusBinding
import com.google.android.material.snackbar.Snackbar
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class TryEventBusActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTryEventBusBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_try_event_bus)
        setContentView(binding.root)

        binding.btnDoEvent.setOnClickListener {
            EventBus.getDefault().postSticky("Are you sure want to send this String? This is a dummy data you know.")
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = false)
    fun showSnack(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = false)
    fun showNotif(message: String) {
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val buildNotification = NotificationCompat.Builder(this, "Chanels")
            .setSmallIcon(R.drawable.ic_baseline_person)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)
            .setContentTitle("Hei, ada Notifikasi!")
            .setContentText(message)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("$message")
            )

        with(NotificationManagerCompat.from(this)) {
            notify(909, buildNotification.build())
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
}