package com.example.tryanimation

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.reprime.showcase.MaterialShowcaseSequence
import com.reprime.showcase.MaterialShowcaseView
import com.reprime.showcase.ShowcaseConfig

class TryShowcaseActivity : AppCompatActivity() {

    private lateinit var tvTitle: TextView
    private lateinit var ivBack: ImageView
    private lateinit var ivPhoto: ImageView

    private var isShowCase = false
    private var isShowCaseShowing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_try_showcase)

        tvTitle = findViewById(R.id.tvTitle)
        ivBack = findViewById(R.id.ivBack)
        ivPhoto = findViewById(R.id.ivPhoto)


        initShowcase()
    }

    private fun initShowcase() {

        isShowCaseShowing = false
        val config = ShowcaseConfig()
        config.delay = 500

        val sequence = MaterialShowcaseSequence(this, "11")
        sequence.setConfig(config)

        sequence.addSequenceItem(
            MaterialShowcaseView.Builder(this)
                .setTarget(ivBack)
                .renderOverNavigationBar()
                .setTitleText("Tombol Back")
                .setContentText("Ingin tau fungsi tombol ini?")
                .setImgContentText("Ini adalah tombol back, atau kembali.") //R.drawable.ic_menu_checkin
                .setButtonText("Lewati", "Coba Sekarang")
                .withCircleShape()
                .setIsGreeting(true)
                .setDismissOnTouch(false)
                .setTargetTouchable(false)
                .build()
        )

        sequence.addSequenceItem(
            MaterialShowcaseView.Builder(this)
                .setTarget(ivPhoto)
                .renderOverNavigationBar()
                .setContentText("Ini adapah foto yang telah tampil di layar Anda")
                .withRectangleShape()
                .setDismissOnTouch(false)
                .setTargetTouchable(true)
                .build()
        )

        sequence.setOnItemDismissedListener { itemView, position, skip ->
            if (skip || position == 1) {
                Toast.makeText(this, "Skipped", Toast.LENGTH_SHORT).show()
                isShowCaseShowing = false
            }
        }

        if (sequence.hasFired()) {
            initShowcase1()
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                sequence.start()
                isShowCaseShowing = true
            }, 1000)
        }
    }

    private fun initShowcase1() {
        isShowCaseShowing = false
        val config = ShowcaseConfig()
        config.delay = 500

        val sequence = MaterialShowcaseSequence(this, "12")
        sequence.setConfig(config)

        sequence.addSequenceItem(
            MaterialShowcaseView.Builder(this)
                .setTarget(tvTitle)
                .renderOverNavigationBar()
                .setContentText("Ini adalah judul Activity Anda")
                .setButtonText("Selesai")
                .withRectangleShape()
                .setDismissOnTouch(true)
                .build()
        )

        sequence.setOnItemDismissedListener { itemView, position, skip ->
            isShowCaseShowing = false
        }

        val sequence21 = MaterialShowcaseSequence(this, "21")
        if (sequence21.hasFired()) {
            Toast.makeText(this, "21 has Fired", Toast.LENGTH_SHORT).show()
            if (sequence.hasFired()) {
                isShowCaseShowing = false
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    sequence.start()
                    isShowCaseShowing = true
                }, 1000)
            }
        } else {
            Toast.makeText(this, "else 21 Fired", Toast.LENGTH_SHORT).show()

        }

    }
}