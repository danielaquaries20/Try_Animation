package com.example.tryanimation.try_web_view

import android.content.Intent
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.*
import com.crocodic.core.base.activity.NoViewModelActivity
import com.crocodic.core.extension.snacked
import com.example.tryanimation.R
import com.example.tryanimation.databinding.ActivityWebViewMainBinding
import timber.log.Timber

class WebViewMainActivity :
    NoViewModelActivity<ActivityWebViewMainBinding>(R.layout.activity_web_view_main) {

    private var fileUploadCallback: ValueCallback<Array<Uri>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initWebView()

    }

    override fun onBackPressed() {
        if (binding.wvMain.canGoBack()) {
            binding.wvMain.goBack()
        } else {
            super.onBackPressed()
        }
    }

    private fun initWebView() {
        val webSettings = binding.wvMain.settings
        webSettings.javaScriptEnabled = true

        binding.wvMain.webViewClient = MyWebViewClient()
        binding.wvMain.webChromeClient = MyChromeClient()

        binding.wvMain.loadUrl("https://postimages.org/id/")
    }

    inner class MyWebViewClient : WebViewClient() {
        override fun onReceivedSslError(
            view: WebView?,
            handler: SslErrorHandler?,
            error: SslError?,
        ) {
//                super.onReceivedSslError(view, handler, error)
            handler?.proceed()
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            binding.pbHorizontalLoading.visibility = View.GONE
        }

    }

    inner class MyChromeClient : WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            binding.pbHorizontalLoading.progress = newProgress
            binding.pbHorizontalLoading.visibility =
                if (newProgress < 100) View.VISIBLE else View.GONE
        }

        override fun onShowFileChooser(
            webView: WebView?,
            filePathCallback: ValueCallback<Array<Uri>>?,
            fileChooserParams: FileChooserParams?,
        ): Boolean {
            fileUploadCallback = filePathCallback

            val chooserIntent = fileChooserParams?.createIntent()
            try {
                startActivityForResult(chooserIntent, FILE_CHOOSER_REQUEST_CODE)
            } catch (e: Exception) {
                e.printStackTrace()
                fileUploadCallback = null
                return false
            }

            return true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FILE_CHOOSER_REQUEST_CODE) {
            if (fileUploadCallback != null) {
                val results = WebChromeClient.FileChooserParams.parseResult(resultCode, data)
                fileUploadCallback?.onReceiveValue(results)
                fileUploadCallback = null
            }
        }
    }

    companion object {
        const val FILE_CHOOSER_REQUEST_CODE = 100
    }
}

