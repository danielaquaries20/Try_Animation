package com.example.tryanimation.ui.kl_basic.chapter_12.data.test_data.shared_preferences

import android.content.Context
import android.content.SharedPreferences

class TestSharedPreferences(context: Context) {

    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences(TEST_PREF_NAME, Context.MODE_PRIVATE)

    fun saveName(value: String) {
        val editor = sharedPreferences.edit()
        editor?.putString(TEST_KEY_NAME, value)
        editor?.apply()
    }

    fun getName(): String? {
        return sharedPreferences.getString(TEST_KEY_NAME, "-")
    }

    companion object {
        const val TEST_PREF_NAME = "test_shared_preferences"
        const val TEST_KEY_NAME = "name"
    }
}