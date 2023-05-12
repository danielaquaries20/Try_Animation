package com.example.tryanimation.ui.kl_basic.chapter_6

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.DataBindingUtil
import com.example.tryanimation.R
import com.example.tryanimation.databinding.ActivityChapter6MainBinding

class Chapter6MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityChapter6MainBinding

    private var canAddOperation = false
    private var canAddDecimal = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chapter6_main)
        setContentView(binding.root)

        initClick()
    }

    private fun initClick() {
        binding.btnAc.setOnClickListener(this)
        binding.btnDelete.setOnClickListener(this)
        binding.btnDivider.setOnClickListener(this)
        binding.btnMultiplied.setOnClickListener(this)
        binding.btnMinus.setOnClickListener(this)
        binding.btnPlus.setOnClickListener(this)
        binding.btnDot.setOnClickListener(this)
        binding.btnResult.setOnClickListener(this)
        binding.btn0.setOnClickListener(this)
        binding.btn1.setOnClickListener(this)
        binding.btn2.setOnClickListener(this)
        binding.btn3.setOnClickListener(this)
        binding.btn4.setOnClickListener(this)
        binding.btn5.setOnClickListener(this)
        binding.btn6.setOnClickListener(this)
        binding.btn7.setOnClickListener(this)
        binding.btn8.setOnClickListener(this)
        binding.btn9.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        //btnNumber -> numberAction()
        //btnOperator -> operationAction
        when (v) {
            binding.btnAc -> allClearAction()
            binding.btnDelete -> backSpaceAction()
            binding.btnResult -> equalsAction()
            binding.btnDivider, binding.btnMultiplied, binding.btnMinus, binding.btnPlus -> operationAction(
                v)
            else -> numberAction(v!!)
        }
    }

    private fun numberAction(view: View) {
        if (view is AppCompatButton) {
            if (view.text == ".") {
                if (canAddDecimal)
                    binding.workingsTV.append(view.text)

                canAddDecimal = false
            } else
                binding.workingsTV.append(view.text)

            canAddOperation = true
        }
    }

    private fun operationAction(view: View) {
        if (view is AppCompatButton && canAddOperation) {
            binding.workingsTV.append(view.text)
            canAddOperation = false
            canAddDecimal = true
        }
    }

    private fun allClearAction() {
        binding.workingsTV.text = ""
        binding.resultsTV.text = ""
    }

    private fun backSpaceAction() {
        val length = binding.workingsTV.length()
        if (length > 0)
            binding.workingsTV.text = binding.workingsTV.text.subSequence(0, length - 1)
    }

    private fun equalsAction() {
        binding.resultsTV.text = calculateResults()
    }

    private fun calculateResults(): String {
        val digitsOperators = digitsOperators()
        Log.d("HitungHasil", "DigitsOperator: $digitsOperators")
        if (digitsOperators.isEmpty()) return ""

        val timesDivision = timesDivisionCalculate(digitsOperators)
        Log.d("HitungHasil", "TimesDivision: $timesDivision")
        if (timesDivision.isEmpty()) return ""

        val result = addSubtractCalculate(timesDivision)
        Log.d("HitungHasil", "result: $result")
        return result.toString()
    }

    private fun addSubtractCalculate(passedList: MutableList<Any>): Float {
        var result = passedList[0] as Float
        Log.d("addSubtractCalculate", "Begin: $passedList")
        Log.d("addSubtractCalculate", "Indices: ${passedList.indices}")
        Log.d("addSubtractCalculate", "LastIndex: ${passedList.lastIndex}")

        for (i in passedList.indices) {
            Log.d("addSubtractCalculate", "For run: $i - data: ${passedList[i]}")
            if (passedList[i] is Char && i != passedList.lastIndex) {
                Log.d("addSubtractCalculate", "1")
                val operator = passedList[i]
                Log.d("addSubtractCalculate", "operator: $operator")
                val nextDigit = passedList[i + 1] as Float
                Log.d("addSubtractCalculate", "nextDigit: $nextDigit")
                if (operator == '+')
                    result += nextDigit
                if (operator == '-')
                    result -= nextDigit

                Log.d("addSubtractCalculate", "result: $result")
            }
        }
        Log.d("addSubtractCalculate", "FinalResult: $result")
        return result
    }

    private fun timesDivisionCalculate(passedList: MutableList<Any>): MutableList<Any> {
        var list = passedList
        while (list.contains('x') || list.contains('/')) {
            Log.d("timesDivisionCalculate", "While run")
            Log.d("timesDivisionCalculate", "List: $list")
            list = calcTimesDiv(list)
        }
        Log.d("timesDivisionCalculate", "result: $list")
        return list
    }

    private fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any> {
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size

        Log.d("calcTimesDiv", "Indices: ${passedList.indices}")
        Log.d("calcTimesDiv", "restartIndex: $restartIndex")
        for (i in passedList.indices) {
            Log.d("calcTimesDiv", "For run: $i - data: ${passedList[i]}")
            if (passedList[i] is Char && i != passedList.lastIndex && i < restartIndex) {
                Log.d("calcTimesDiv", "1")
                val operator = passedList[i]
                Log.d("calcTimesDiv", "operator: $operator")
                val prevDigit = passedList[i - 1] as Float
                Log.d("calcTimesDiv", "prevDigit: $prevDigit")
                val nextDigit = passedList[i + 1] as Float
                Log.d("calcTimesDiv", "nextDigit: $nextDigit")
                when (operator) {
                        'x' -> {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i + 1
                    }
                    '/' -> {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i + 1
                    }
                    else -> {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }
            }

            if (i > restartIndex) {
                Log.d("calcTimesDiv", "2 - index: $i")
                Log.d("calcTimesDiv", "restartIndex: $restartIndex")
                newList.add(passedList[i])
                Log.d("calcTimesDiv", "newList: $newList")
            }
        }

        Log.d("calcTimesDiv", "newList: $newList")
        return newList
    }

    private fun digitsOperators(): MutableList<Any> {
        Log.d("OperasiDigit", "Begin: ${binding.workingsTV.text}")
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for (character in binding.workingsTV.text) {
            Log.d("OperasiDigit", "Char: $character")
            if (character.isDigit() || character == '.') {
                currentDigit += character
                Log.d("OperasiDigit", "1 - currentDigit: $currentDigit")
            } else {
                Log.d("OperasiDigit", "0 - currentDigit: $currentDigit")
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
                Log.d("OperasiDigit", "list: $list")
            }
        }

        if (currentDigit != "") {
            Log.d("OperasiDigit", "2 - currentDigit: $currentDigit")
            list.add(currentDigit.toFloat())
            Log.d("OperasiDigit", "list: $list")
        }

        Log.d("OperasiDigit", "Final Result: $list")
        return list
    }
}