package com.example.tryanimation.ui.kl_basic.chapter_12.data.shared_pref

import android.content.Context
import android.content.SharedPreferences

class MySharedPreferences(context: Context) {

    private var pref: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun setName(name: String) {
        val editor = pref?.edit()
        editor?.putString(KEY_NAME, name)
        editor?.apply()
    }

    fun getName(): String? {
        return pref.getString(KEY_NAME, "-")
    }

    companion object {
        const val PREF_NAME = "my_sp"

        const val KEY_NAME = "name"
    }
}