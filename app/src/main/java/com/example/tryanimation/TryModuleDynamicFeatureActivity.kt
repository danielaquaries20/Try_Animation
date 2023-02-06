package com.example.tryanimation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.tryanimation.try_chat_app.TryChatActivity
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus

class TryModuleDynamicFeatureActivity : AppCompatActivity() {

    private lateinit var ivPhoto: ImageView
    private lateinit var btnDynamicFeature: Button
    private lateinit var btnDynamic1: Button
    private lateinit var linearLoading: LinearLayout
    private lateinit var tvInfoProgress: TextView
    private lateinit var splitInstallManager: SplitInstallManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_try_module_dynamic_feature)

        ivPhoto = findViewById(R.id.ivPhoto)
        btnDynamicFeature = findViewById(R.id.btnDynamicFeature)
        btnDynamic1 = findViewById(R.id.btnDynamic1)
        linearLoading = findViewById(R.id.linearLoading)
        tvInfoProgress = findViewById(R.id.tvInfoProgress)

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
        // TODO: Mempelajari Listener State
        val listener = SplitInstallStateUpdatedListener { state ->
            when (state.status()) {
                SplitInstallSessionStatus.DOWNLOADING -> {
                    linearLoading.visibility = View.VISIBLE
                    tvInfoProgress.text = "Downloading"
                }
                SplitInstallSessionStatus.INSTALLING -> {
                    linearLoading.visibility = View.VISIBLE
                    tvInfoProgress.text = "Installing"
                }
                SplitInstallSessionStatus.CANCELING -> {
                    linearLoading.visibility = View.VISIBLE
                    tvInfoProgress.text = "Canceling"
                }
                SplitInstallSessionStatus.PENDING -> {
                    linearLoading.visibility = View.VISIBLE
                    tvInfoProgress.text = "Pending"
                }
                SplitInstallSessionStatus.INSTALLED -> {
                    linearLoading.visibility = View.GONE
                    tvInfoProgress.text = "Installed"
                    Toast.makeText(this, "Module Installed", Toast.LENGTH_SHORT).show()
                }
                SplitInstallSessionStatus.DOWNLOADED -> {
                    linearLoading.visibility = View.GONE
                    tvInfoProgress.text = "Downloaded"
                    Toast.makeText(this, "Module Downloaded", Toast.LENGTH_SHORT).show()
                }
                SplitInstallSessionStatus.CANCELED -> {
                    linearLoading.visibility = View.GONE
                    tvInfoProgress.text = "Canceled"
                    Toast.makeText(this, "Module Canceled", Toast.LENGTH_SHORT).show()
                }
                SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {
                    linearLoading.visibility = View.VISIBLE
                    tvInfoProgress.text = "Require User Confirmation"
                }
                SplitInstallSessionStatus.FAILED -> {
                    linearLoading.visibility = View.GONE
                    tvInfoProgress.text = "Failed"
                    Toast.makeText(this, "Module Failed", Toast.LENGTH_SHORT).show()
                }
                SplitInstallSessionStatus.UNKNOWN -> {
                    linearLoading.visibility = View.GONE
                    tvInfoProgress.text = "Unknown"
                    Toast.makeText(this, "Module Unknown", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val request = SplitInstallRequest.newBuilder()
            .addModule(module)
            .build()

//        splitInstallManager.registerListener(listener)
        splitInstallManager.startInstall(request).addOnSuccessListener {
            linearLoading.visibility = View.GONE
            Toast.makeText(this, "Success Install Module", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, Class.forName(className)))
        }.addOnFailureListener {
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