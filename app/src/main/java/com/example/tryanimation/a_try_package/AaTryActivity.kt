package com.example.tryanimation.a_try_package

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.tryanimation.R

class AaTryActivity : AppCompatActivity() {

    var alertDialog : AlertDialog.Builder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aa_try)

        alertDialog = AlertDialog.Builder(this)
            .setTitle("Alert Dialog")
            .setMessage("Message for you")
            .setPositiveButton("Okey") { dialog, which ->
                Toast.makeText(this, "Okey", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Refuse") { dialog, which ->
                Toast.makeText(this, "Refuse", Toast.LENGTH_SHORT).show()
            }
            .setNeutralButton("Wait") {dialog, which ->
                Toast.makeText(this, "Wait", Toast.LENGTH_SHORT).show()
            }


        alertDialog?.show()
    }
}