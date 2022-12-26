package com.example.tryanimation

import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.tryanimation.try_chat_app.TryChatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import smartdevelop.ir.eram.showcaseviewlib.GuideView
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity
import smartdevelop.ir.eram.showcaseviewlib.config.PointerType


class MainActivity : AppCompatActivity() {

    /*region Inisialisai*/
    private lateinit var ivAnimation: ImageView
    private lateinit var btnToMap: Button
    private lateinit var btnNext: Button
    private lateinit var btnMotionLayout: Button
    private lateinit var btnCoordinatorLayout: Button
    private lateinit var btnBtmSheetShow: Button
    private lateinit var btnAlerDialogShow: Button
    private lateinit var btnTryCenterConstraint: Button
    private lateinit var btnTryScannQRCOde: Button
    private lateinit var btnTryNavigationComponent: Button
    private lateinit var btnTryChat: Button
    private lateinit var btnTryDatePicker: Button
    private lateinit var btnTryStickyView: Button
    /*endregion*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*region Deklarasi*/
        ivAnimation = findViewById(R.id.ivImageAnimation)
        btnToMap = findViewById(R.id.btnShow)
        btnNext = findViewById(R.id.btnNext)
        btnMotionLayout = findViewById(R.id.btnMotionLayout)
        btnCoordinatorLayout = findViewById(R.id.btnCoordinatorLayout)
        btnBtmSheetShow = findViewById(R.id.btnBtmSheetShow)
        btnAlerDialogShow = findViewById(R.id.btnAlertDialogShow)
        btnTryCenterConstraint = findViewById(R.id.btnTryCenterConstraintLayout)
        btnTryScannQRCOde = findViewById(R.id.btnTryScanQR)
        btnTryNavigationComponent = findViewById(R.id.btnTryNavigationComponent)
        btnTryChat = findViewById(R.id.btnTryChatApp)
        btnTryDatePicker = findViewById(R.id.btnTryDatePicker)
        btnTryStickyView = findViewById(R.id.btnTryStickyView)
        /*endregion*/

        /*region Action*/
        val tempat = "SMP+Negeri+3+Ungaran"
        btnToMap.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("google.navigation:q=$tempat&mode=d")
            )
            intent.setPackage("com.google.android.apps.maps")
            startActivity(intent)
//            intent.resolveActivity(packageManager)?.let {
//                startActivity(intent)
//            }
        }

        btnNext.setOnClickListener {
            startActivity(Intent(this, AnimationOneActivity::class.java))
            overridePendingTransition(R.anim.transition_from_bottom, R.anim.transition_to_top)
        }

        btnMotionLayout.setOnClickListener {
            startActivity(Intent(this, MotionLayoutOneActivity::class.java))
            overridePendingTransition(R.anim.transition_to_left, R.anim.transition_to_right)
//            overridePendingTransition(R.anim.transition_from_right, R.anim.transition_to_left)
        }

        btnCoordinatorLayout.setOnClickListener {
            startActivity(Intent(this, CoordinatorActivity::class.java))
            overridePendingTransition(R.anim.transition_zoom_in, R.anim.transition_to_right)
        }

        btnBtmSheetShow.setOnClickListener {
            bottomSheetTry()
        }

        btnAlerDialogShow.setOnClickListener {
            alertDialogTry()
        }

        btnTryCenterConstraint.setOnClickListener {
            startActivity(Intent(this, TryCenterViewActivity::class.java))
        }

        btnTryScannQRCOde.setOnClickListener {
            startActivity(Intent(this, TryScanQRActivity::class.java))
        }

        btnTryNavigationComponent.setOnClickListener {
            startActivity(Intent(this, TryNavigationComponentActivity::class.java))
        }

        btnTryChat.setOnClickListener {
            startActivity(Intent(this, TryChatActivity::class.java))
        }

        btnTryDatePicker.setOnClickListener {
            startActivity(Intent(this, TryDatePickerActivity::class.java))
        }

        btnTryStickyView.setOnClickListener {
            startActivity(Intent(this, TryStickyScrollActivity::class.java))
        }

        /*endregion*/

        /*region Animation*/
        var runnable: Runnable? = null
        runnable = Runnable {
            ivAnimation.animate().apply {
                duration = 1000
                alpha(.5f)
//                scaleXBy(.50f)
//                scaleYBy(.50f)
                rotationYBy(360f)
                rotationXBy(360f)
//                translationYBy(20f)
            }.withEndAction(runnable).start()
        }
        runnable.run()
//        Uri.parse("google.navigation:q=22.659239,88.435534&mode=l")

        btnToMap.animate().apply {
            duration = 1000
//            alpha(.5f)
            scaleXBy(.50f)
            scaleYBy(.50f)
            rotationYBy(360f)
            rotationXBy(360f)
            translationYBy(20f)
        }.withEndAction {
            btnNext.animate().apply {
                duration = 1000
//                alpha(.5f)
                scaleXBy(-.5f)
                scaleYBy(-.5f)
                rotationYBy(360f)
                rotationXBy(360f)
                translationYBy(-20f)
            }
        }.start()
        /*endregion*/

//        guideViewFirst()

    }

    private fun bottomSheetTry() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val bottomSheetTry = LayoutInflater.from(applicationContext).inflate(
            R.layout.bottom_sheet_try, findViewById(R.id.btmSheetTry)
        )
        bottomSheetDialog.setContentView(bottomSheetTry)
        bottomSheetDialog.show()

        bottomSheetTry.findViewById<Button>(R.id.btnBtmShtDismiss)
            .setOnClickListener {
                bottomSheetDialog.dismiss()
            }

    }

    private fun guideViewFirst() {
        GuideView.Builder(this)
            .setTitle("Button Google Map")
            .setContentText("Jika Button ini di klik\n maka akan pindah ke Google Map")
            .setTargetView(btnToMap)
            .setGravity(Gravity.center)
            .setCircleIndicatorSize(10f)
            .setCircleStrokeIndicatorSize(2f)
            .setCircleInnerIndicatorSize(5f)
            .setContentTypeFace(Typeface.MONOSPACE)//optional
            .setTitleTypeFace(Typeface.SANS_SERIF)//optional
            .setDismissType(DismissType.anywhere) //optional - default dismissible by TargetView
            .setPointerType(PointerType.circle)
            .build()
            .show()
    }

    private fun alertDialogTry() {
        val alertDialogTry =
            LayoutInflater.from(this)
                .inflate(R.layout.alert_dialog_try, findViewById(R.id.alertDialogTry))


        val alertDialogBuilder = AlertDialog.Builder(this)
            .setView(alertDialogTry)

        val theAlertDialog = alertDialogBuilder.show()

        alertDialogTry.findViewById<Button>(R.id.btnAlertDialogDismiss).setOnClickListener {
            theAlertDialog.dismiss()
        }
    }

}