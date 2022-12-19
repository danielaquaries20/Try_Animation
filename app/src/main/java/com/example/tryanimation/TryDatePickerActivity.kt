package com.example.tryanimation

import android.os.Bundle
import android.util.Log
import android.util.SparseIntArray
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.aminography.primecalendar.civil.CivilCalendar
import com.aminography.primedatepicker.common.BackgroundShapeType
import com.aminography.primedatepicker.common.LabelFormatter
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.aminography.primedatepicker.picker.callback.MultipleDaysPickCallback
import com.aminography.primedatepicker.picker.callback.RangeDaysPickCallback
import com.aminography.primedatepicker.picker.callback.SingleDayPickCallback
import com.aminography.primedatepicker.picker.theme.LightThemeFactory
import com.applandeo.materialcalendarview.CalendarUtils
import com.applandeo.materialcalendarview.CalendarView
import java.text.SimpleDateFormat
import java.util.*

class TryDatePickerActivity : AppCompatActivity() {

    private lateinit var tvDateStart: TextView
    private lateinit var tvDateEnd: TextView
    private lateinit var btnPickDate: Button

    private lateinit var tvDateStartApplandeo: TextView
    private lateinit var tvDateEndApplandeo: TextView
    private lateinit var btnPickDateApplandeo: Button
    private lateinit var applandeoDatePicker: CalendarView

    private var startDateApplandeo = ""
    private var endDateApplandeo = ""
    private var calendarStartApplandeo: Calendar? = null
    private var calendarEndApplandeo: Calendar? = null
    private var countClickApplandeoDatePicker = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_try_date_picker)

        tvDateStart = findViewById(R.id.tvDateStart)
        tvDateEnd = findViewById(R.id.tvDateEnd)
        btnPickDate = findViewById(R.id.btnPickDate)

        tvDateStartApplandeo = findViewById(R.id.tvDateStartApplandeo)
        tvDateEndApplandeo = findViewById(R.id.tvDateEndApplandeo)
        btnPickDateApplandeo = findViewById(R.id.btnPickDateApplandeo)
        applandeoDatePicker = findViewById(R.id.applandeoCalendarView)


        btnPickDate.setOnClickListener { initDatePicker() }

        setApplandeoCalendar()

    }

    private fun setApplandeoCalendar() {
//        val calendar = Calendar.getInstance()
//        calendar.set(2022, 11, 17)
//        applandeoDatePicker.setDate(calendar.time)

        applandeoDatePicker.setOnDayClickListener {
            if (countClickApplandeoDatePicker == 2) {
                startDateApplandeo = ""
                endDateApplandeo = ""
                calendarStartApplandeo = null
                calendarEndApplandeo = null
                countClickApplandeoDatePicker = 1
            } else {
                countClickApplandeoDatePicker++
            }
            val tanggal = it.calendar
            val simpleDateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy")
            val dateTime = simpleDateFormat.format(tanggal.time)

            if (startDateApplandeo == "" || startDateApplandeo == dateTime) {
                startDateApplandeo = dateTime
                calendarStartApplandeo = it.calendar
            }
            if (startDateApplandeo != dateTime) {
                endDateApplandeo = dateTime
                calendarEndApplandeo = it.calendar
            }
            tvDateStartApplandeo.text = "Tanggal Mulai : $startDateApplandeo"
            tvDateEndApplandeo.text = "Tanggal Akhir : $endDateApplandeo"

            if (calendarStartApplandeo != null && calendarEndApplandeo != null) {
                val rangePickDay =
                    CalendarUtils.getDatesRange(calendarStartApplandeo, calendarEndApplandeo)
                Log.d("CalendarPick", "Tanggal dipilih : $rangePickDay")
            }
        }
//        CalendarUtils.getDatesRange()
    }

    private val themeFactory = object : LightThemeFactory() {

        /*override val typefacePath: String?
            get() = "fonts/Righteous-Regular.ttf"*/

        override val dialogBackgroundColor: Int
            get() = getColor(R.color.white)

        override val calendarViewBackgroundColor: Int
            get() = getColor(R.color.white_transparent_70)

        override val pickedDayBackgroundShapeType: BackgroundShapeType
            get() = BackgroundShapeType.ROUND_SQUARE

        override val calendarViewPickedDayBackgroundColor: Int
            get() = getColor(R.color.green_transparent_70)

        override val calendarViewPickedDayInRangeBackgroundColor: Int
            get() = getColor(R.color.green_transparent_70)

        override val calendarViewPickedDayInRangeLabelTextColor: Int
            get() = getColor(R.color.black_transparent_40)

        override val calendarViewTodayLabelTextColor: Int
            get() = getColor(R.color.black_transparent_40)

        override val calendarViewWeekLabelFormatter: LabelFormatter
            get() = { primeCalendar ->
                when (primeCalendar[Calendar.DAY_OF_WEEK]) {
                    Calendar.SATURDAY,
                    Calendar.SUNDAY,
                    -> String.format("%s😍", primeCalendar.weekDayNameShort)
                    else -> String.format("%s", primeCalendar.weekDayNameShort)
                }
            }

        override val calendarViewWeekLabelTextColors: SparseIntArray
            get() = SparseIntArray(7).apply {
                val red = getColor(com.aminography.primedatepicker.R.color.red50)
                val indigo = getColor(com.aminography.primedatepicker.R.color.indigo50)
                put(Calendar.SATURDAY, red)
                put(Calendar.SUNDAY, red)
                put(Calendar.MONDAY, indigo)
                put(Calendar.TUESDAY, indigo)
                put(Calendar.WEDNESDAY, indigo)
                put(Calendar.THURSDAY, indigo)
                put(Calendar.FRIDAY, indigo)
            }

        override val calendarViewShowAdjacentMonthDays: Boolean
            get() = true

        override val selectionBarBackgroundColor: Int
            get() = getColor(com.aminography.primedatepicker.R.color.blue50)

        override val selectionBarRangeDaysItemBackgroundColor: Int
            get() = getColor(R.color.yellow)
    }

    private fun initDatePicker() {
        val callbackSingle = SingleDayPickCallback { day ->
            val hari = day.longDateString
            tvDateStart.text = "Tanggal Mulai : $hari"
        }
        val callbackRange = RangeDaysPickCallback { startDay, endDay ->
            val hariPertama = startDay.longDateString
            val hariAkhir = endDay.longDateString
            tvDateStart.text = "Tanggal Mulai : $hariPertama"
            tvDateEnd.text = "Tanggal Akhir : $hariAkhir"
        }

        val callbackMultiple = MultipleDaysPickCallback { multipleDays ->
            val indexTerakhir = multipleDays.lastIndex
            val hariPertama = multipleDays[0].longDateString
            val hariAkhir = multipleDays[indexTerakhir].longDateString
            tvDateStart.text = "Tanggal Mulai : $hariPertama"
            tvDateEnd.text = "Tanggal Akhir : $hariAkhir"
        }

        // To show a date picker with Civil dates, also today as the starting date
        val today = CivilCalendar()
        val minPossibeDate = CivilCalendar().also {
            it.year = Calendar.YEAR
            it.month = Calendar.MONTH
            it.firstDayOfWeek = Calendar.MONDAY
        }
        val maxPossibleDate = CivilCalendar().also {
            it.year = 2023
            it.month = 11
            it.firstDayOfWeek = Calendar.MONDAY
        }

        val datePicker = PrimeDatePicker.dialogWith(today)  // or dialogWith(today)
            .pickRangeDays(callbackRange)
            .initiallyPickedStartDay(today)
            .autoSelectPickEndDay(true)
//            .applyTheme(themeFactory)
            .build()

        datePicker.show(supportFragmentManager, "PRIME_DATE_PICKER")
    }
}