package com.example.tryanimation

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class AnimationOneActivity : AppCompatActivity() {

    private lateinit var scrollRoot: ScrollView
    private lateinit var linearBiodata: LinearLayout
    private lateinit var ivPhoto: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvDescription: TextView

    private lateinit var btnShow: Button
    private lateinit var btnHide: Button

    private lateinit var btnRootShow: Button
    private lateinit var btnRootHide: Button

    private lateinit var btnRootRootShow: Button
    private lateinit var btnRootRootHide: Button

    private lateinit var btnImageShow: Button
    private lateinit var btnImageHide: Button

    private lateinit var btnTitleShow: Button
    private lateinit var btnTitleHide: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animation_one)

        scrollRoot = findViewById(R.id.scrollRootLayout)
        linearBiodata = findViewById(R.id.linearBiodata)
        ivPhoto = findViewById(R.id.ivTop)
        tvName = findViewById(R.id.tvName)
        tvDescription = findViewById(R.id.tvDescription)
        btnShow = findViewById(R.id.btnClick)
        btnHide = findViewById(R.id.btnClick2)

        btnRootShow = findViewById(R.id.btnForRoot1)
        btnRootHide = findViewById(R.id.btnForRoot2)
        btnRootRootShow = findViewById(R.id.btnForRootRoot1)
        btnRootRootHide = findViewById(R.id.btnForRootRoot2)

        btnImageShow = findViewById(R.id.btnForImage1)
        btnImageHide = findViewById(R.id.btnForImage2)

        btnTitleShow = findViewById(R.id.btnForTitle1)
        btnTitleHide = findViewById(R.id.btnForTitle2)


        val blinkAnim = AnimationUtils.loadAnimation(this, R.anim.blink_anim)
        val bounce = AnimationUtils.loadAnimation(this, R.anim.bounce)
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein)
        val fadeInForRoot = AnimationUtils.loadAnimation(this, R.anim.fadein)
        val fadeOut = AnimationUtils.loadAnimation(this, R.anim.fadeout)
        val leftToRight = AnimationUtils.loadAnimation(this, R.anim.lefttoright)
        val rightToLeft = AnimationUtils.loadAnimation(this, R.anim.righttoleft)
        val mixedAnim = AnimationUtils.loadAnimation(this, R.anim.mixed_anim)
        val rotate = AnimationUtils.loadAnimation(this, R.anim.rotate)
        val sampleAnim = AnimationUtils.loadAnimation(this, R.anim.sample_anim)
        val zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoomin)
        val zoomOut = AnimationUtils.loadAnimation(this, R.anim.zoomout)
        val reverseTopToBottom = AnimationUtils.loadAnimation(this, R.anim.reverse_toptobottom)
        val backState = AnimationUtils.loadAnimation(this, R.anim.transition_state)

//        ivPhoto.startAnimation(bounce)
//        tvName.startAnimation(fadeIn)
        scrollRoot.startAnimation(fadeInForRoot)

        /*region Description*/
        btnShow.setOnClickListener {
            it.visibility = View.GONE
            btnHide.visibility = View.VISIBLE
            tvDescription.startAnimation(fadeIn)
            tvDescription.visibility = View.VISIBLE
//            Handler().postDelayed({  }, 1000)
//            tvName.startAnimation(blinkAnim)
//            tvDescription.startAnimation(reverseTopToBottom)
//            scrollRoot.startAnimation(sampleAnim)
//            linearBiodata.startAnimation(zoomIn)
        }

        btnHide.setOnClickListener {
            it.visibility = View.GONE
            btnShow.visibility = View.VISIBLE
            tvDescription.startAnimation(fadeOut)
            Handler().postDelayed({ tvDescription.visibility = View.GONE }, 1000)
        }
        /*endregion*/

        /*region Root*/
        btnRootShow.setOnClickListener {
            it.visibility = View.GONE
            btnRootHide.visibility = View.VISIBLE
            scrollRoot.visibility = View.VISIBLE
//            scrollRoot.startAnimation(zoomIn)
            scrollRoot.animate().apply {
                duration = 1000
//                scaleXBy(1f)
                scaleYBy(1f)
                scaleX(1f)
//                scaleY(1f)
            }
        }

        btnRootHide.setOnClickListener {
            it.visibility = View.GONE
            btnRootShow.visibility = View.VISIBLE
            scrollRoot.visibility = View.VISIBLE
//            scrollRoot.startAnimation(backState)
            scrollRoot.animate().apply {
                duration = 1000
//                scaleXBy(-1f)
                scaleYBy(-1f)
                scaleX(-1f)
//                scaleY(-1f)
            }
        }
        /*endregion*/

        /*region Root*/
        btnRootRootShow.setOnClickListener {
            it.visibility = View.GONE
            btnRootRootHide.visibility = View.VISIBLE
            scrollRoot.visibility = View.VISIBLE
            scrollRoot.startAnimation(zoomIn)

        }

        btnRootRootHide.setOnClickListener {
            it.visibility = View.GONE
            btnRootRootShow.visibility = View.VISIBLE
            scrollRoot.visibility = View.VISIBLE
            scrollRoot.startAnimation(zoomOut)
        }
        /*endregion*/

        /*region Image*/
        btnImageShow.setOnClickListener {
            it.visibility = View.GONE
            btnImageHide.visibility = View.VISIBLE
            ivPhoto.visibility = View.VISIBLE
            ivPhoto.startAnimation(bounce)
        }

        btnImageHide.setOnClickListener {
            it.visibility = View.GONE
            btnImageShow.visibility = View.VISIBLE
            ivPhoto.visibility = View.INVISIBLE
            ivPhoto.startAnimation(fadeOut)
        }
        /*endregion*/

        /*region Title*/
        btnTitleShow.setOnClickListener {
            it.visibility = View.GONE
            btnTitleHide.visibility = View.VISIBLE
            tvName.visibility = View.VISIBLE
            tvName.startAnimation(mixedAnim)
        }

        btnTitleHide.setOnClickListener {
            it.visibility = View.GONE
            btnTitleShow.visibility = View.VISIBLE
            tvName.visibility = View.VISIBLE
            tvName.startAnimation(backState)
        }
        /*endregion*/

    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.transition_from_top, R.anim.transition_to_bottom)
    }
}