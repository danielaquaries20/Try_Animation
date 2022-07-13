package com.example.tryanimation

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.*

class AnimationOneActivity : AppCompatActivity() {

    private lateinit var scrollRoot: ScrollView
    private lateinit var linearBiodata: LinearLayout
    private lateinit var ivPhoto: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvDescription: TextView
    private lateinit var btnClick: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animation_one)

        scrollRoot = findViewById(R.id.scrollRootLayout)
        linearBiodata = findViewById(R.id.linearBiodata)
        ivPhoto = findViewById(R.id.ivTop)
        tvName = findViewById(R.id.tvName)
        tvDescription = findViewById(R.id.tvDescription)
        btnClick = findViewById(R.id.btnClick)

        val blinkAnim = AnimationUtils.loadAnimation(this, R.anim.blink_anim)
        val bounce = AnimationUtils.loadAnimation(this, R.anim.bounce)
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein)
        val fadeOut = AnimationUtils.loadAnimation(this, R.anim.fadeout)
        val leftToRight = AnimationUtils.loadAnimation(this, R.anim.lefttoright)
        val rightToLeft = AnimationUtils.loadAnimation(this, R.anim.righttoleft)
        val mixedAnim = AnimationUtils.loadAnimation(this, R.anim.mixed_anim)
        val rotate = AnimationUtils.loadAnimation(this, R.anim.rotate)
        val sampleAnim = AnimationUtils.loadAnimation(this, R.anim.sample_anim)
        val zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoomin)
        val zoomOut = AnimationUtils.loadAnimation(this, R.anim.zoomout)
        val reverseTopToBottom = AnimationUtils.loadAnimation(this, R.anim.reverse_toptobottom)

        ivPhoto.startAnimation(bounce)
        tvName.startAnimation(fadeIn)
        tvDescription.startAnimation(leftToRight)

        btnClick.setOnClickListener {
            tvName.startAnimation(blinkAnim)
            tvDescription.startAnimation(reverseTopToBottom)
//            scrollRoot.startAnimation(sampleAnim)
//            linearBiodata.startAnimation(zoomIn)
        }

    }
}