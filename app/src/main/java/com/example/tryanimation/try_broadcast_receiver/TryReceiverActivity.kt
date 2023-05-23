package com.example.tryanimation.try_broadcast_receiver

import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.tryanimation.R
import com.example.tryanimation.databinding.ActivityTryBroadcasrReceiverBinding

class TryReceiverActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTryBroadcasrReceiverBinding

    private lateinit var receiver: MyReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_try_broadcasr_receiver)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_try_broadcasr_receiver)
        setContentView(binding.root)

        receiver = MyReceiver()

        IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED).also {
            registerReceiver(receiver, it)
        }

//        binding.btnSendBroadcast.setOnClickListener {
//            postCustomBroadcast()
//        }
    }


    override fun onStop() {
        super.onStop()
        unregisterReceiver(receiver)
    }



    private fun postCustomBroadcast() {
        val customIntent = Intent("com.daniel.CUSTOM_INTENT")
        sendBroadcast(customIntent)
    }
}