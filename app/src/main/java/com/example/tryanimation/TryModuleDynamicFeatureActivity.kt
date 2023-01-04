package com.example.tryanimation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest

class TryModuleDynamicFeatureActivity : AppCompatActivity() {

    private lateinit var ivPhoto: ImageView
    private lateinit var btnDynamicFeature: Button
    private lateinit var btnDynamic1: Button
    private lateinit var linearLoading: LinearLayout
    private lateinit var splitInstallManager : SplitInstallManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_try_module_dynamic_feature)

        ivPhoto = findViewById(R.id.ivPhoto)
        btnDynamicFeature = findViewById(R.id.btnDynamicFeature)
        btnDynamic1 = findViewById(R.id.btnDynamic1)
        linearLoading = findViewById(R.id.linearLoading)

        splitInstallManager = SplitInstallManagerFactory.create(this)

//        ivPhoto.setImageResource(com.daniel.try_module.R.drawable.photo_1)

        initClick()

    }

    private fun initClick() {
        btnDynamicFeature.setOnClickListener {
            tryDynamicFeatureModule()
        }
        btnDynamic1.setOnClickListener {
            val cobaLihat = splitInstallManager.installedModules
            Toast.makeText(this, "Installed Module: $cobaLihat", Toast.LENGTH_SHORT).show()
        }
    }

    private fun tryDynamicFeatureModule() {
        try {
            val moduleTryDynamicFeature = "try_dynamic_feature"
            val className = "com.daniel.try_dynamic_feature.DefaultActivity"
            val cobaLihat = splitInstallManager.installedModules
            if (splitInstallManager.installedModules.contains(moduleTryDynamicFeature)) {
                Toast.makeText(this, "Install : $cobaLihat", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, Class.forName(className)))
            } else {
                alertDialogTry("Install Module Confirmation",
                    "Apakah Anda ingin meng-install module Try Dynamic Feature?\nInstalled Module : $cobaLihat",
                    moduleTryDynamicFeature, className)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error : $e", Toast.LENGTH_SHORT).show()
        }
    }

    private fun installModule(module: String, className: String) {
        linearLoading.visibility = View.VISIBLE
        val request = SplitInstallRequest.newBuilder()
            .addModule(module)
            .build()

        splitInstallManager.startInstall(request).addOnSuccessListener {
            linearLoading.visibility = View.GONE
            Toast.makeText(this, "Success Install Module", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, Class.forName(className)))
        }.addOnFailureListener {
            // TODO: Handle saat Install Module Failed
            Log.e("FailedModule", "Error: $it")
            alertDialogTry("Error Notification", "$it", module, className, false)
            linearLoading.visibility = View.GONE
            Toast.makeText(this, "Failed to Install Module, Error: $it", Toast.LENGTH_SHORT).show()
        }
    }

    private fun alertDialogTry(
        title: String,
        subtitle: String?,
        module: String,
        className: String,
        withBtn: Boolean = true,
    ) {
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
        val linearBtn = alertDialogInstallConfirmation.findViewById<LinearLayout>(R.id.linearBtn)

        tvTitle.text = title

        if (subtitle.isNullOrEmpty()) {
            tvSubtitle.visibility = View.GONE
        } else {
            tvSubtitle.text = subtitle
        }

        if (withBtn) {
            linearBtn.visibility = View.VISIBLE
        } else {
            linearBtn.visibility = View.GONE
        }

        btnBatal.setOnClickListener {
            theAlertDialog.dismiss()
        }

        btnKonfirmasi.setOnClickListener {
            installModule(module, className)
            theAlertDialog.dismiss()
        }

    }

}