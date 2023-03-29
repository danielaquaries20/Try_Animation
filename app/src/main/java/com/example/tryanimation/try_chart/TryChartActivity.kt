package com.example.tryanimation.try_chart

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tryanimation.R
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import com.google.android.material.snackbar.Snackbar


class TryChartActivity : AppCompatActivity() {


    private lateinit var rootLayout: ScrollView
    private lateinit var pieChart: PieChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_try_chart)

        initView()
    }

    private fun initView() {
        rootLayout = findViewById(R.id.rootLayout)
        pieChart = findViewById(R.id.pieChart)

        initChart()
    }

    private fun initChart() {
        pieChart.setUsePercentValues(true)

        pieChart.description.isEnabled = false
        pieChart.description.text = "This is dummy data"

        pieChart.setExtraOffsets(5f, 5f, 5f, 5f)
//        pieChart.dragDecelerationFrictionCoef = 0.95f

        pieChart.setCenterTextTypeface(Typeface.SANS_SERIF)
        pieChart.centerText = "Try Pie Chart"

        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.TRANSPARENT)
//        pieChart.setTransparentCircleAlpha(110)
//        pieChart.holeRadius = 58f
//        pieChart.transparentCircleRadius = 61f

        pieChart.setDrawCenterText(true)

//        pieChart.rotationAngle = 0f

        pieChart.isRotationEnabled = true
        pieChart.isHighlightPerTapEnabled = true

        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener
        pieChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                showContent("Value Selected: ${h?.dataSetIndex}")
            }

            override fun onNothingSelected() {
                showContent("Nothing Selected")
            }
        })

        pieChart.animateY(1400, Easing.EaseInOutQuad)

        val legend = pieChart.legend
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        legend.orientation = Legend.LegendOrientation.VERTICAL
        legend.direction = Legend.LegendDirection.RIGHT_TO_LEFT
        legend.setDrawInside(false)
//        legend.xEntrySpace = 7f
//        legend.yEntrySpace = 5f
        legend.yOffset = 13f
        legend.setDrawInside(true)
        legend.isEnabled = false

        // entry label styling
        pieChart.setEntryLabelColor(Color.WHITE)
        pieChart.setEntryLabelTypeface(Typeface.SANS_SERIF)
        pieChart.setEntryLabelTextSize(7f)

        setPieChartContent(3, 15f)
    }

    private fun setPieChartContent(count: Int, range: Float) {
        val entries = ArrayList<PieEntry>()

        for (i in 0 until count) {
            var iconData: Drawable? = null
            when (i) {
                0 -> iconData = getDrawable(R.drawable.ic_baseline_person)
                1 -> iconData = getDrawable(R.drawable.ic_baseline_home)
                2 -> iconData = getDrawable(R.drawable.ic_baseline_arrow_back)
            }
            entries.add(PieEntry((Math.random() * range + range / 5).toFloat(),
                "Data $i",
                iconData))
        }

        val dataSet = PieDataSet(entries, "Election Result")

        dataSet.setDrawIcons(true)
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f

        val colors = ArrayList<Int>()

//        for (c in ColorTemplate.VORDIPLOM_COLORS) colors.add(c)
//
//        for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)
//
//        for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)
//
//        for (c in ColorTemplate.LIBERTY_COLORS) colors.add(c)
//
//        for (c in ColorTemplate.PASTEL_COLORS) colors.add(c)

        colors.add(ColorTemplate.getHoloBlue())
        colors.add(Color.GREEN)
        colors.add(getColor(R.color.purple_200))

        dataSet.colors = colors

        val data = PieData(dataSet)

        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(9f)
        data.setValueTextColor(Color.WHITE)
        data.setValueTypeface(Typeface.SANS_SERIF)
        pieChart.data = data

        // undo all highlights
        pieChart.highlightValues(null)

        pieChart.invalidate()
    }

    private fun showContent(content: String, isSnack: Boolean = true, withLog: Boolean = true) {
        if (isSnack) {
            Snackbar.make(rootLayout, content, Snackbar.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, content, Toast.LENGTH_SHORT).show()
        }

        if (withLog) {
            Log.d("Signature", content)
        }
    }

}