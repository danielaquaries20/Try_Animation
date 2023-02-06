package com.daniel.try_dynamic_feature

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aminography.primecalendar.civil.CivilCalendar
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.aminography.primedatepicker.picker.callback.RangeDaysPickCallback
import com.example.tryanimation.try_chat_app.TryChatActivity
import com.google.android.play.core.splitcompat.SplitCompat
import java.util.*

class DefaultActivity : AppCompatActivity() {

    private lateinit var tvNumber: TextView
    private lateinit var tvTitle: TextView
    private lateinit var btnDecrement: Button
    private lateinit var btnIncrement: Button

    private var number: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_default)

        tvNumber = findViewById(R.id.tvNumber)
        tvTitle = findViewById(R.id.tvTitle)
        btnDecrement = findViewById(R.id.btnDecrement)
        btnIncrement = findViewById(R.id.btnIncrement)

        initClick()
        tvNumber.text = number.toString()
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        SplitCompat.installActivity(this)
    }

    private fun initClick() {
        tvTitle.setOnClickListener {
//            startActivity(Intent(this, TryChatActivity::class.java))
            setPrimeDatePicker()
        }

        btnIncrement.setOnClickListener {
            number++
            tvNumber.text = number.toString()
        }

        btnDecrement.setOnClickListener {
            number--
            tvNumber.text = number.toString()
        }

        tvNumber.setOnClickListener {
            Toast.makeText(this, "Angka : $number", Toast.LENGTH_SHORT).show()
        }

    }


    private fun setPrimeDatePicker() {

        val callbackRange = RangeDaysPickCallback { startDay, endDay ->
            val firstDay = startDay.longDateString
            val finishDay = endDay.longDateString
            Toast.makeText(this, "Start: $firstDay, Finish: $finishDay", Toast.LENGTH_LONG).show()
        }

        // To show a date picker with Civil dates, also today as the starting date
        val today = CivilCalendar()

        val datePicker = PrimeDatePicker.bottomSheetWith(today)  // or dialogWith(today)
            .pickRangeDays(callbackRange)
            .initiallyPickedStartDay(today)
            .autoSelectPickEndDay(true)
            .build()

        datePicker.show(supportFragmentManager, "PRIME_DATE_PICKER")
    }

}