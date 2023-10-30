package com.example.tryanimation.try_web_view

import android.content.Intent
import android.os.Bundle
import com.crocodic.core.base.activity.NoViewModelActivity
import com.example.tryanimation.R
import com.example.tryanimation.databinding.ActivityTryWebMainBinding

class TryWebMainActivity :
    NoViewModelActivity<ActivityTryWebMainBinding>(R.layout.activity_try_web_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnFileChooser.setOnClickListener {
//            val url = "https://postimages.org/id/"
            val url = "https://pickerwheel.com/tools/random-image-generator/"
//            val url = "https://store-visit-service.app.reprime.id/app/to"
//            val url = "https://store-visit-service.app.reprime.id/app/?lat=-7.0642874&long=110.4164804"
            val toWeb = Intent(this, WebViewMainActivity::class.java).apply {
                putExtra(WebViewMainActivity.URL_WEB_KEY, url)
            }
            startActivity(toWeb)
        }

        binding.btnWebCam.setOnClickListener {
            val url = "https://webcamtests.com/"
            val toWeb = Intent(this, WebViewMainActivity::class.java).apply {
                putExtra(WebViewMainActivity.URL_WEB_KEY, url)
            }
            startActivity(toWeb)
        }

        binding.btnQrScanner.setOnClickListener {
            val url = "https://qrcodescanner.net/"
            val toWeb = Intent(this, WebViewMainActivity::class.java).apply {
                putExtra(WebViewMainActivity.URL_WEB_KEY, url)
            }
            startActivity(toWeb)
        }

        binding.btnLocation.setOnClickListener {
            val url = "https://mylocation.org/"
            val toWeb = Intent(this, WebViewMainActivity::class.java).apply {
                putExtra(WebViewMainActivity.URL_WEB_KEY, url)
                putExtra(WebViewMainActivity.IS_LOCATION, true)
            }
            startActivity(toWeb)
        }



    }
}