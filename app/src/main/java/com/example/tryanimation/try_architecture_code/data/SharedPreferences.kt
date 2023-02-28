package com.example.tryanimation.try_architecture_code.data

import android.content.Context
import android.content.SharedPreferences
import com.example.tryanimation.try_architecture_code.utils.Constants

class SharedPreferences(context: Context) {

    private val myPref = "MainSession"
    private val sharedPreference: SharedPreferences =
        context.getSharedPreferences(myPref, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreference.edit()

    /*region setter*/
    fun setValue(key: String, value: String) {
        editor.apply {
            putString(key, value)
            apply()
        }
    }

    fun setValue(key: String, value: Int) {
        editor.apply {
            putInt(key, value)
            apply()
        }
    }

    fun setValue(key: String, value: Boolean) {
        editor.apply {
            putBoolean(key, value)
            apply()
        }
    }

    fun setValue(key: String, value: Float) {
        editor.apply {
            putFloat(key, value)
            apply()
        }
    }

    fun setValue(key: String, value: Long) {
        editor.apply {
            putLong(key, value)
            apply()
        }
    }
    /*endregion*/

    /*region getter*/
    fun getValue(key: String, defaultValue: String?): String? {
        return sharedPreference.getString(key, defaultValue)
    }

    fun getValue(key: String, defaultValue: Int = 0): Int {
        return sharedPreference.getInt(key, defaultValue)
    }

    fun getValue(key: String, defaultValue: Boolean = false): Boolean {
        return sharedPreference.getBoolean(key, defaultValue)
    }

    fun getValue(key: String, defaultValue: Float = 0.0f): Float {
        return sharedPreference.getFloat(key, defaultValue)
    }

    fun getValue(key: String, defaultValue: Long = 0L): Long {
        return sharedPreference.getLong(key, defaultValue)
    }

    /*endregion*/

    fun clearAllValue() {
        editor.apply {
            putString(Constants.KEY_USER_NAME, "")
            putString(Constants.KEY_USER_EMAIL, "")
            putString(Constants.KEY_USER_PASSWORD, "")
            putInt(Constants.KEY_USER_AGE, 0)
            apply()
        }
    }

}