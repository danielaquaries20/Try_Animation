package com.example.tryanimation

import android.os.Bundle
import android.util.Log
import android.util.SparseIntArray
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aminography.primecalendar.civil.CivilCalendar
import com.aminography.primecalendar.japanese.JapaneseCalendar
import com.aminography.primedatepicker.common.BackgroundShapeType
import com.aminography.primedatepicker.common.LabelFormatter
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.aminography.primedatepicker.picker.callback.MultipleDaysPickCallback
import com.aminography.primedatepicker.picker.callback.RangeDaysPickCallback
import com.aminography.primedatepicker.picker.callback.SingleDayPickCallback
import com.aminography.primedatepicker.picker.theme.LightThemeFactory
import com.applandeo.materialcalendarview.CalendarUtils
import com.applandeo.materialcalendarview.CalendarView
import com.daniel.try_module.customviews.DateRangeCalendarView
import java.text.SimpleDateFormat
import java.util.*

class TryDatePickerActivity : AppCompatActivity() {

    private lateinit var tvDateStart: TextView
    private lateinit var tvDateEnd: TextView
    private lateinit var btnPickDate: Button
    private lateinit var btnPickDate2: Button

    private lateinit var tvDateStartApplandeo: TextView
    private lateinit var tvDateEndApplandeo: TextView
    private lateinit var btnPickDateApplandeo: Button
    private lateinit var applandeoDatePicker: CalendarView

    private lateinit var tvDateStartCustom: TextView
    private lateinit var tvDateEndCustom: TextView
    private lateinit var btnBatalCustomDatePicker: Button
    private lateinit var btnSimpanCustomDatePicker: Button
    private lateinit var customDatePicker: DateRangeCalendarView

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
        btnPickDate2 = findViewById(R.id.btnPickDate2)

        tvDateStartApplandeo = findViewById(R.id.tvDateStartApplandeo)
        tvDateEndApplandeo = findViewById(R.id.tvDateEndApplandeo)
        btnPickDateApplandeo = findViewById(R.id.btnPickDateApplandeo)
        applandeoDatePicker = findViewById(R.id.applandeoCalendarView)

        tvDateStartCustom = findViewById(R.id.tvDateStartCustom)
        tvDateEndCustom = findViewById(R.id.tvDateEndCustom)
        btnBatalCustomDatePicker = findViewById(R.id.btnBatalCustomDatePicker)
        btnSimpanCustomDatePicker = findViewById(R.id.btnSimpanCustomDatePicker)
        customDatePicker = findViewById(R.id.customDatePicker)

        btnPickDate.setOnClickListener { setPrimeDatePicker() }
        btnPickDate2.setOnClickListener { setPrimeDatePicker2() }

        setApplandeoCalendar()

        setCustomCalendar()

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

    private fun setCustomCalendar() {

        val simpleDateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy")

        btnBatalCustomDatePicker.setOnClickListener {
            tvDateStartCustom.text = "Tanggal Mulai :"
            tvDateEndCustom.text = "Tanggal Akhir :"
        }

        btnSimpanCustomDatePicker.setOnClickListener {
            val mulai = tvDateStartCustom.text.toString()
            val akhir = tvDateEndCustom.text.toString()
            Toast.makeText(this, "$mulai - $akhir", Toast.LENGTH_LONG).show()
        }

        customDatePicker.setCalendarListener(object : DateRangeCalendarView.CalendarListener {
            override fun onFirstDateSelected(startDate: Calendar?) {
                val tanggalMulai = startDate?.time?.let { simpleDateFormat.format(it) }
                tvDateStartCustom.text = "Tanggal Mulai : $tanggalMulai"
                tvDateEndCustom.text = "Tanggal Akhir :"
            }

            override fun onDateRangeSelected(startDate: Calendar?, endDate: Calendar?) {
                val tanggalMulai = startDate?.time?.let { simpleDateFormat.format(it) }
                val tanggalSelesai = endDate?.time?.let { simpleDateFormat.format(it) }
                tvDateStartCustom.text = "Tanggal Mulai : $tanggalMulai"
                tvDateEndCustom.text = "Tanggal Akhir : $tanggalSelesai"
            }

        })
    }

//    privaye val tgttg = object : DarkThemeFactory() {}

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
            get() = getColor(com.aminography.primedatepicker.R.color.blue200)

        override val selectionBarRangeDaysItemBackgroundColor: Int
            get() = getColor(R.color.yellow)

        override val selectionBarMultipleDaysItemBackgroundColor: Int
            get() = getColor(R.color.yellow)
    }

    private fun setPrimeDatePicker() {
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

    private fun setPrimeDatePicker2() {
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

        val today = JapaneseCalendar()
//        val minPossibeDate = today.also {
//            it.year = Calendar.YEAR
//            it.month = Calendar.MONTH
//            it.firstDayOfWeek = Calendar.MONDAY
//        }
//        val maxPossibleDate = today.also {
//            it.year = 2024
//            it.month = Calendar.MONTH
//            it.firstDayOfWeek = Calendar.MONDAY
//        }

        val datePicker = PrimeDatePicker.bottomSheetWith(today)  // or dialogWith(today)
//            .pickRangeDays(callbackRange)
            .pickMultipleDays(callbackMultiple)
//            .initiallyPickedStartDay(today)
//            .autoSelectPickEndDay(true)
//            .minPossibleDate(minPossibeDate)
//            .maxPossibleDate(maxPossibleDate)
            .applyTheme(themeFactory)
            .build()

        datePicker.show(supportFragmentManager, "PRIME_DATE_PICKER")
    }

}