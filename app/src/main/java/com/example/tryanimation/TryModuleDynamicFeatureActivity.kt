package com.example.tryanimation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class TryModuleDynamicFeatureActivity : AppCompatActivity() {

    private lateinit var ivPhoto: ImageView
    private lateinit var btnDynamicFeature: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_try_module_dynamic_feature)

        ivPhoto = findViewById(R.id.ivPhoto)
        btnDynamicFeature = findViewById(R.id.btnDynamicFeature)

//        ivPhoto.setImageResource(com.daniel.try_module.R.drawable.photo_1)

        initClick()

    }

    private fun initClick() {
        btnDynamicFeature.setOnClickListener {
            alertDialogTry("Module 1", "Install Module 1", 0)
        }
    }

    private fun alertDialogTry(title: String, subtitle: String?, condition: Int) {
        val alertDialogInstallConfirmation =
            LayoutInflater.from(this)
                .inflate(R.layout.alert_dialog_install_module_confirmation,
                    findViewById(R.id.alertDialogInstallModuleConfirmation))


        val alertDialogBuilder = AlertDialog.Builder(this)
            .setView(alertDialogInstallConfirmation)

        val theAlertDialog = alertDialogBuilder.show()

        val tvTitle = alertDialogInstallConfirmation.findViewById<TextView>(R.id.tvTitleAlert)
        val tvSubtitle = alertDialogInstallConfirmation.findViewById<TextView>(R.id.tvSubtitleAlert)
        val btnBatal = alertDialogInstallConfirmation.findViewById<Button>(R.id.btnBatal)
        val btnKonfirmasi = alertDialogInstallConfirmation.findViewById<Button>(R.id.btnKonfirmasi)

        tvTitle.text = title

        if (subtitle.isNullOrEmpty()) {
            tvSubtitle.visibility = View.GONE
        } else {
            tvSubtitle.text = subtitle
        }

        btnBatal.setOnClickListener {
            theAlertDialog.dismiss()
        }

        btnKonfirmasi.setOnClickListener {
            if (condition == 0) {
                Toast.makeText(this, "Install module $condition", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Don't know which module to Install", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }

}