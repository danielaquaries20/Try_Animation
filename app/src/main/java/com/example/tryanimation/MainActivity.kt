package com.example.tryanimation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class MainActivity : AppCompatActivity() {

    private lateinit var ivAnimation: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ivAnimation = findViewById(R.id.ivImageAnimation)

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