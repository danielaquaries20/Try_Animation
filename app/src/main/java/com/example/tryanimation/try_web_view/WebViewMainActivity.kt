package com.example.tryanimation.try_web_view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.net.http.SslError
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.crocodic.core.base.activity.NoViewModelActivity
import com.crocodic.core.extension.checkLocationPermission
import com.crocodic.core.extension.tos
import com.example.tryanimation.R
import com.example.tryanimation.databinding.ActivityWebViewMainBinding


class WebViewMainActivity :
    NoViewModelActivity<ActivityWebViewMainBinding>(R.layout.activity_web_view_main) {

    private var urlWeb: String? = null

    private var fileUploadCallback: ValueCallback<Array<Uri>>? = null

    private var mRequest: PermissionRequest? = null
    val cameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                mRequest?.grant(mRequest?.resources)
            }
        }

    var mGeoLocationRequestOrigin: String? = null
    var mGeoLocationCallback: GeolocationPermissions.Callback? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        urlWeb = intent.getStringExtra(URL_WEB_KEY)

        if (urlWeb.isNullOrEmpty()) {
            finish()
            tos("Do not have any Url to open")
        } else {
            val isLocation = intent.getBooleanExtra(IS_LOCATION, false)
            if (isLocation) {
                checkLocationPermission {
                    listenLocationChange()
                    initWebView()
                }
            } else {
                initWebView()
            }
        }

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
        webSettings.domStorageEnabled = true

//        webSettings.setGeolocationEnabled(true)
//        webSettings.setGeolocationDatabasePath(filesDir.path)

        binding.wvMain.webViewClient = MyWebViewClient()
        binding.wvMain.webChromeClient = MyChromeClient()

        binding.wvMain.loadUrl(urlWeb!!)
    }

    inner class MyWebViewClient : WebViewClient() {
        override fun onReceivedSslError(
            view: WebView?,
            handler: SslErrorHandler?,
            error: SslError?,
        ) {
                super.onReceivedSslError(view, handler, error)
//            handler?.proceed()
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
            chooserIntent?.type = "image/*"
            chooserIntent?.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            try {
                startActivityForResult(chooserIntent, FILE_CHOOSER_REQUEST_CODE)
            } catch (e: Exception) {
                e.printStackTrace()
                fileUploadCallback = null
                return false
            }

            return true
        }

        override fun onPermissionRequest(request: PermissionRequest?) {
//            super.onPermissionRequest(request)
            mRequest = request
            val requestedResource = request?.resources
            for (permission in requestedResource!!) {
                if (permission == PermissionRequest.RESOURCE_VIDEO_CAPTURE) {
                    cameraPermission.launch(Manifest.permission.CAMERA)
                }
            }
        }

        override fun onPermissionRequestCanceled(request: PermissionRequest?) {
            super.onPermissionRequestCanceled(request)
            tos("Permission Cancelled")
        }

        override fun onGeolocationPermissionsShowPrompt(
            origin: String?,
            callback: GeolocationPermissions.Callback?,
        ) {
            super.onGeolocationPermissionsShowPrompt(origin, callback)
//            callback!!.invoke(origin, true, true)

            if (ContextCompat.checkSelfPermission(
                    this@WebViewMainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                mGeoLocationCallback = callback
                mGeoLocationRequestOrigin = origin
                ActivityCompat.requestPermissions(
                    this@WebViewMainActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE
                )
            } else {
                callback!!.invoke(origin, true, true)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            LOCATION_REQUEST_CODE -> {
                //if permission is cancel result array would be empty
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission was granted
                    if (mGeoLocationCallback != null) {
                        mGeoLocationCallback!!.invoke(mGeoLocationRequestOrigin, true, true)
                    }
                } else {
                    //permission denied
                    if (mGeoLocationCallback != null) {
                        mGeoLocationCallback!!.invoke(mGeoLocationRequestOrigin, false, false)
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FILE_CHOOSER_REQUEST_CODE) {
            if (fileUploadCallback != null) {
                val clipData = data?.clipData
                val uris = ArrayList<Uri>()
                try {
                    if (resultCode == RESULT_OK) {
                        if (clipData != null ){
                            for (i in 0 until clipData.itemCount) {
                                val imageUri = clipData.getItemAt(i).uri
                                uris.add(imageUri)
                            }
                        } else {
                            val imageUri = data?.data
                            imageUri?.let { uris.add(it) }
                        }
                    }
                } catch (e:Exception) {
                    e.printStackTrace()
                }
                fileUploadCallback?.onReceiveValue(uris.toTypedArray())
                fileUploadCallback = null
            }
        }
    }

    companion object {
        const val FILE_CHOOSER_REQUEST_CODE = 100
        const val CAMERA_REQUEST_CODE = 101
        const val LOCATION_REQUEST_CODE = 102


        const val URL_WEB_KEY = "url_web_key"
        const val IS_LOCATION = "is_location"
    }
}

