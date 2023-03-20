package com.example.tryanimation.try_signature_pad

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.tryanimation.R
import com.example.tryanimation.utils.BitmapOperator
import com.google.android.material.snackbar.Snackbar
import se.warting.signatureview.views.SignaturePad
import se.warting.signatureview.views.SignedListener

class TrySignaturePadActivity : AppCompatActivity() {

    private var bitmapSgt: Bitmap? = null
    private var bitmapSgt2: Bitmap? = null
    private var signature: String? = null
    private var signature2: String? = null

    private lateinit var rootLayout: ScrollView
    private lateinit var btnClear: AppCompatButton
    private lateinit var btnClear2: AppCompatButton
    private lateinit var btnShow: AppCompatButton
    private lateinit var btnShow2: AppCompatButton
    private lateinit var ivSignature: ImageView
    private lateinit var ivSignature2: ImageView
    private lateinit var vSignature: SignaturePad
    private lateinit var vSignature2: com.github.gcacace.signaturepad.views.SignaturePad

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_try_signature_pad)

        initView()
        initClick()
    }

    private fun initView() {
        rootLayout = findViewById(R.id.rootLayout)
        vSignature = findViewById(R.id.vSignature)
        vSignature2 = findViewById(R.id.vSignature2)
        btnClear = findViewById(R.id.btnClear)
        btnClear2 = findViewById(R.id.btnClear2)
        ivSignature = findViewById(R.id.ivSignature)
        ivSignature2 = findViewById(R.id.ivSignature2)
        btnShow = findViewById(R.id.btnShow)
        btnShow2 = findViewById(R.id.btnShow2)

        vSignature.setOnSignedListener(object : SignedListener {
            override fun onClear() {
                signature = ""
                bitmapSgt = null
                showContent("onClear - Called")
            }

            override fun onSigned() {
                val strSignature =
                    BitmapOperator().encodeToBase64(vSignature.getTransparentSignatureBitmap())
                signature = strSignature
                bitmapSgt = vSignature.getTransparentSignatureBitmap()
                showContent("onSigned - Called")
            }

            override fun onStartSigning() {
                showContent("onStartSigning - Called")
            }
        })

        vSignature2.setOnSignedListener(object :
            com.github.gcacace.signaturepad.views.SignaturePad.OnSignedListener {
            override fun onStartSigning() {
                showContent("onStartSigning - Called")
            }

            override fun onSigned() {
                val strSignature =
                    BitmapOperator().encodeToBase64(vSignature.getTransparentSignatureBitmap())
                signature2 = strSignature
                bitmapSgt2 = vSignature2.signatureBitmap
                showContent("onSigned - Called")
            }

            override fun onClear() {
                signature2 = ""
                bitmapSgt2 = null
                showContent("onClear - Called")
            }
        })
    }

    private fun initClick() {
        btnClear.setOnClickListener {
            vSignature.clear()
            ivSignature.visibility = View.GONE
        }

        btnClear2.setOnClickListener {
            vSignature2.clear()
            ivSignature2.visibility = View.GONE
        }

        btnShow.setOnClickListener {
            showContent("Signature: $signature")
            bitmapSgt?.let {
                ivSignature.visibility = View.VISIBLE
                ivSignature.setImageBitmap(it)
            }
        }

        btnShow2.setOnClickListener {
            showContent("Signature: $signature2")
            bitmapSgt2?.let {
                ivSignature2.visibility = View.VISIBLE
                ivSignature2.setImageBitmap(it)
            }
        }
    }

    private fun showContent(content: String, isSnack: Boolean = true, withLog: Boolean = true) {
        if (isSnack) {
            Snackbar.make(rootLayout, content, Snackbar.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, content, Toast.LENGTH_SHORT).show()
        }

        if (withLog) {
            Log.d("Signature", content)
        }
    }
}