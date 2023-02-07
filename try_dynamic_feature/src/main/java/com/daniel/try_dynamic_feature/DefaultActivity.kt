package com.daniel.try_dynamic_feature

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.aminography.primecalendar.civil.CivilCalendar
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.aminography.primedatepicker.picker.callback.RangeDaysPickCallback
import com.google.android.play.core.splitcompat.SplitCompat
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest

class DefaultActivity : AppCompatActivity() {

    private lateinit var splitInstallManager: SplitInstallManager

    private lateinit var tvNumber: TextView
    private lateinit var tvTitle: TextView
    private lateinit var btnDecrement: Button
    private lateinit var btnIncrement: Button
    private lateinit var ivPhoto: ImageView

    private var number: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_default)

        splitInstallManager = SplitInstallManagerFactory.create(this)

        tvNumber = findViewById(R.id.tvNumber)
        tvTitle = findViewById(R.id.tvTitle)
        btnDecrement = findViewById(R.id.btnDecrement)
        btnIncrement = findViewById(R.id.btnIncrement)
        ivPhoto = findViewById(R.id.ivPhoto)

        initClick()
        tvNumber.text = number.toString()
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        SplitCompat.installActivity(this)
    }

    private fun initClick() {
        tvTitle.setOnClickListener {
//            startActivity(Intent(this, TryChatActivity::class.java))
            setPrimeDatePicker()
        }

        ivPhoto.setOnClickListener {
            checkModuleMain()
        }

        btnIncrement.setOnClickListener {
            number++
            tvNumber.text = number.toString()
        }

        btnDecrement.setOnClickListener {
            number--
            tvNumber.text = number.toString()
        }

        tvNumber.setOnClickListener {
            Toast.makeText(this, "Angka : $number", Toast.LENGTH_SHORT).show()
        }

    }

    private fun checkModuleMain() {
        try {
            val moduleTryDynamicFeature = "dynamic1"
            val className = "com.daniel.dynamic1.MainActivity"
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


    private fun setPrimeDatePicker() {

        val callbackRange = RangeDaysPickCallback { startDay, endDay ->
            val firstDay = startDay.longDateString
            val finishDay = endDay.longDateString
            Toast.makeText(this, "Start: $firstDay, Finish: $finishDay", Toast.LENGTH_LONG).show()
        }

        // To show a date picker with Civil dates, also today as the starting date
        val today = CivilCalendar()

        val datePicker = PrimeDatePicker.bottomSheetWith(today)  // or dialogWith(today)
            .pickRangeDays(callbackRange)
            .initiallyPickedStartDay(today)
            .autoSelectPickEndDay(true)
            .build()

        datePicker.show(supportFragmentManager, "PRIME_DATE_PICKER")
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
                .inflate(com.example.tryanimation.R.layout.alert_dialog_install_module_confirmation,
                    findViewById(com.example.tryanimation.R.id.alertDialogInstallModuleConfirmation))


        val alertDialogBuilder = AlertDialog.Builder(this)
            .setView(alertDialogInstallConfirmation)

        val theAlertDialog = alertDialogBuilder.show()

        val tvTitle =
            alertDialogInstallConfirmation.findViewById<TextView>(com.example.tryanimation.R.id.tvTitleAlert)
        val tvSubtitle =
            alertDialogInstallConfirmation.findViewById<TextView>(com.example.tryanimation.R.id.tvSubtitleAlert)
        val btnBatal =
            alertDialogInstallConfirmation.findViewById<Button>(com.example.tryanimation.R.id.btnBatal)
        val btnKonfirmasi =
            alertDialogInstallConfirmation.findViewById<Button>(com.example.tryanimation.R.id.btnKonfirmasi)
        val linearBtn =
            alertDialogInstallConfirmation.findViewById<LinearLayout>(com.example.tryanimation.R.id.linearBtn)

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

    private fun installModule(module: String, className: String) {
        val request = SplitInstallRequest.newBuilder()
            .addModule(module)
            .build()

        splitInstallManager.startInstall(request).addOnSuccessListener {
            Toast.makeText(this, "Success Install Module", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, Class.forName(className)))
        }.addOnFailureListener {
            alertDialogTry("Error Notification", "$it", module, className, false)
            Toast.makeText(this, "Failed to Install Module, Error: $it", Toast.LENGTH_SHORT).show()
        }
    }

}