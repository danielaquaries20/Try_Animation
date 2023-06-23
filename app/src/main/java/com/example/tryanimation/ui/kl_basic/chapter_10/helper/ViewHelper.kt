package com.example.tryanimation.ui.kl_basic.chapter_10.helper

import android.widget.ImageView
import androidx.databinding.BindingAdapter

class ViewHelper {

    companion object {
        @JvmStatic
        @BindingAdapter("imageSrc")
        fun ImageView.setImageSrc(imageSrc: Int?) {
            imageSrc?.let {
                setImageResource(it)
            }
        }
    }

}