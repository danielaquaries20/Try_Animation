package com.example.tryanimation.ui.kl_basic.chapter_13.helper

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.databinding.BindingAdapter

class ViewHelper {

    companion object {
        @JvmStatic
        @BindingAdapter("imageBitmap")
        fun ImageView.setImageByBitmap(bitmap: Bitmap?) {
            bitmap?.let {
                setImageBitmap(it)
            }
        }
    }

}