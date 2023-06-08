package com.example.tryanimation.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.tryanimation.R
import com.example.tryanimation.ui.kl_basic.chapter_10.Chapter10MainActivity
import com.example.tryanimation.ui.kl_basic.chapter_4.Chapter4MainActivity
import com.example.tryanimation.ui.kl_basic.chapter_5.Chapter5MainActivity
import com.example.tryanimation.ui.kl_basic.chapter_6.Chapter6MainActivity
import com.example.tryanimation.ui.kl_basic.chapter_6.MyCalculatorActivity
import com.example.tryanimation.ui.kl_basic.chapter_7.Chapter7MainActivity
import com.example.tryanimation.ui.kl_basic.chapter_8.Chapter8MainActivity
import com.example.tryanimation.ui.kl_basic.chapter_9.Chapter9MainActivity
import com.example.tryanimation.ui.kl_basic.chapter_9.OnPauseActivity

class MainActivity : AppCompatActivity() {

    private lateinit var ivAnimation: ImageView
    private lateinit var btnToMap: Button
    private lateinit var btnNext: Button
    private lateinit var btnStyle: AppCompatButton
    private lateinit var btnChapter6Simple: AppCompatButton
    private lateinit var btnChapter6Complicate: AppCompatButton
    private lateinit var btnChapter7: AppCompatButton
    private lateinit var btnChapter8: AppCompatButton
    private lateinit var btnChapter9: AppCompatButton
    private lateinit var btnChapter10: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ivAnimation = findViewById(R.id.ivImageAnimation)
        btnToMap = findViewById(R.id.btnShow)
        btnNext = findViewById(R.id.btnNext)
        btnStyle = findViewById(R.id.btnStyle)
        btnChapter6Simple = findViewById(R.id.btnChapter6Simple)
        btnChapter6Complicate = findViewById(R.id.btnChapter6Complicate)
        btnChapter7 = findViewById(R.id.btnChapter7)
        btnChapter8 = findViewById(R.id.btnChapter8)
        btnChapter9 = findViewById(R.id.btnChapter9)
        btnChapter10 = findViewById(R.id.btnChapter10)

        btnNext.setOnClickListener {
            startActivity(Intent(this, Chapter4MainActivity::class.java))
        }

        btnStyle.setOnClickListener {
            startActivity(Intent(this, Chapter5MainActivity::class.java))
        }

        btnChapter6Simple.setOnClickListener {
            startActivity(Intent(this, MyCalculatorActivity::class.java))
        }

        btnChapter6Complicate.setOnClickListener {
            startActivity(Intent(this, Chapter6MainActivity::class.java))
        }

        btnChapter7.setOnClickListener {
            startActivity(Intent(this, Chapter7MainActivity::class.java))
        }

        btnChapter8.setOnClickListener {
            startActivity(Intent(this, Chapter8MainActivity::class.java))
        }

        btnChapter9.setOnClickListener {
            startActivity(Intent(this, Chapter9MainActivity::class.java))
        }

        btnChapter10.setOnClickListener {
            startActivity(Intent(this, Chapter10MainActivity::class.java))
        }


//        var runnable: Runnable? = null
//        runnable = Runnable {
//            ivAnimation.animate().apply {
//                duration = 1000
////                alpha(.5f)
////                scaleXBy(.50f)
////                scaleYBy(.50f)
////                rotationYBy(360f)
////                rotationXBy(360f)
//                translationYBy(20f)
//            }.withEndAction(runnable).start()
//        }
//        runnable.run()
//        Uri.parse("google.navigation:q=22.659239,88.435534&mode=l")
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


        ivAnimation.animate().apply {
            duration = 1000
//            alpha(.5f)
            scaleXBy(.50f)
            scaleYBy(.50f)
            rotationYBy(360f)
            rotationXBy(360f)
            translationYBy(20f)
        }.withEndAction {
            ivAnimation.animate().apply {
                duration = 1000
//                alpha(.5f)
                scaleXBy(-.5f)
                scaleYBy(-.5f)
                rotationYBy(360f)
                rotationXBy(360f)
                translationYBy(-20f)
            }
        }.start()


    }
}