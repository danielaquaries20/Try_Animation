package com.example.tryanimation.try_kizitonwose_calendar

import android.view.View
import android.widget.TextView
import com.example.tryanimation.R
import com.kizitonwose.calendar.view.ViewContainer

class DayViewContainer(view: View) : ViewContainer(view) {
    val textView = view.findViewById<TextView>(R.id.calendarDayText)
}