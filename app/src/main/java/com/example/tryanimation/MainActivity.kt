package com.example.tryanimation

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

class MainActivity : AppCompatActivity() {

    private lateinit var ivAnimation: ImageView
    private lateinit var btnToMap: Button
    private lateinit var btnNext: Button
    private lateinit var btnMotionLayout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ivAnimation = findViewById(R.id.ivImageAnimation)
        btnToMap = findViewById(R.id.btnShow)
        btnNext = findViewById(R.id.btnNext)
        btnMotionLayout = findViewById(R.id.btnMotionLayout)

        btnNext.setOnClickListener {
            startActivity(Intent(this,AnimationOneActivity::class.java))
        }

        btnMotionLayout.setOnClickListener {
            startActivity(Intent(this,MotionLayoutOneActivity::class.java))
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