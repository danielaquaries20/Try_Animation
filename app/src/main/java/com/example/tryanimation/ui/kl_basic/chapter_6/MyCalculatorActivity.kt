package com.example.tryanimation.ui.kl_basic.chapter_6

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import com.example.tryanimation.R
import com.example.tryanimation.databinding.ActivityMyCalculatorBinding

class MyCalculatorActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMyCalculatorBinding

    private var firstNumber: Double? = null
    private var secondNumber: Double? = null
    private var resultNumber: Double? = null
    private var operators: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_calculator)
        setContentView(binding.root)

        changeNumberListener()
        initClick()
    }

    private fun changeNumberListener() {
        binding.etFirstNumber.doAfterTextChanged {
            binding.tvResult.text = ""
            firstNumber = try {
                if (it.isNullOrEmpty()) {
                    null
                } else {
                    val number = it.toString().toDouble()
                    number
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        binding.etSecondNumber.doAfterTextChanged {
            binding.tvResult.text = ""
            secondNumber = try {
                if (it.isNullOrEmpty()) {
                    null
                } else {
                    val number = it.toString().toDouble()
                    number
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

    }

    private fun initClick() {
        binding.btnAllClear.setOnClickListener(this)
        binding.btnEquals.setOnClickListener(this)
        binding.btnDivider.setOnClickListener(this)
        binding.btnMultiplied.setOnClickListener(this)
        binding.btnPlus.setOnClickListener(this)
        binding.btnMinus.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnAllClear -> clearAll()
            binding.btnEquals -> calculateNumber()
            else -> setOperators(v)
        }
    }

    private fun calculateNumber() {
        if (firstNumber == null) {
//            Snackbar.make(binding.root, "Isi angka pertama!", Snackbar.LENGTH_LONG).show()
            binding.tvResult.text = "Isi angka pertama!"
            return
        }

        if (secondNumber == null) {
//            Snackbar.make(binding.root, "Isi angka kedua!", Snackbar.LENGTH_LONG).show()
            binding.tvResult.text = "Isi angka kedua!"
            return
        }

        if (operators.isEmpty()) {
//            Snackbar.make(binding.root, "Isi operasi matematika!", Snackbar.LENGTH_LONG).show()
            binding.tvResult.text = "Isi operasi matematika!"
            return
        }

        val first = firstNumber!!
        val second = secondNumber!!
        val result = when (operators) {
            "+" -> first + second
            "-" -> first - second
            ":" -> first / second
            else -> first * second
        }

        resultNumber = result
        binding.tvResult.text = result.toString()
    }

    private fun clearAll() {
        binding.etFirstNumber.setText("")
        binding.etSecondNumber.setText("")
        binding.tvOperator.text = ""
        binding.tvResult.text = ""
        firstNumber = null
        secondNumber = null
        operators = ""
        resultNumber = null
    }

    private fun setOperators(view: View?) {
        binding.tvResult.text = ""
        view?.let { v ->
            if (v is AppCompatButton) {
                val op = v.text.toString()
                operators = op
                binding.tvOperator.text = op
            }
        }
    }


}