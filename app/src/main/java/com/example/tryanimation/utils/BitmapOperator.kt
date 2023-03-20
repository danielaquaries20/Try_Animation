package com.example.tryanimation.utils

import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream

class BitmapOperator {
    fun encodeToBase64(image: Bitmap): String {
        var baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        var b = baos.toByteArray()
        var temp: String? = null
        try {
            System.gc()
            temp = Base64.encodeToString(b, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
        } catch (e: OutOfMemoryError) {
            baos = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.JPEG, 75, baos)
            b = baos.toByteArray()
            temp = Base64.encodeToString(b, Base64.DEFAULT)
        }
        return temp ?: ""
    }

}