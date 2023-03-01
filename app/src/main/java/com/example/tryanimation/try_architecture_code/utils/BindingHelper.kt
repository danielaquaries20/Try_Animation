package com.example.tryanimation.try_architecture_code.utils

import android.content.Context
import android.widget.Button
import android.widget.Toast
import androidx.databinding.BindingAdapter
import com.google.android.material.snackbar.Snackbar

class BindingHelper(private val context: Context) {

    fun btnShowToast(text: String?) {
        if (text.isNullOrEmpty()) {
            Toast.makeText(context, "No Data", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {

        @JvmStatic
        @BindingAdapter(value = ["sbText"], requireAll = false)
        fun btnShowSnackbar(view: Button, sbText: String?) {
            view.setOnClickListener {
                if (sbText.isNullOrEmpty()) {
                    Snackbar.make(view, "No Data", Snackbar.LENGTH_SHORT).show()
                } else {
                    Snackbar.make(view, sbText, Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

}