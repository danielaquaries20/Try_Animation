package com.example.tryanimation.ui.kl_basic.chapter_10.helper

import android.widget.ImageView
import androidx.databinding.BindingAdapter

class ViewHelper {

    companion object {
        @JvmStatic
        @BindingAdapter(value = ["imageSrc"], requireAll = false)
        fun setImageSrc(view: ImageView, imageSrc: Int?) {
            imageSrc?.let {
                view.setImageResource(it)
            }
        }
    }

}