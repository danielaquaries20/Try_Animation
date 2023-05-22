package com.example.tryanimation.try_evaluate_math

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.tryanimation.R
import com.example.tryanimation.databinding.ActivityTryEvaluateMathBinding
import com.google.android.material.snackbar.Snackbar
import net.objecthunter.exp4j.ExpressionBuilder

class TryEvaluateMathActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTryEvaluateMathBinding

    private var work: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_try_evaluate_math)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_try_evaluate_math)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        work = "(1 + 2) % (2 + 3) * (2 + 3 + 5 * 8 - 5)"
        binding.tvWork.text = work

        binding.btnEvaluate.setOnClickListener {
            evaluateWork()
        }
    }

    private fun evaluateWork() {
        try {
            val expression = ExpressionBuilder(work).build()
            val result = expression.evaluate()
            Snackbar.make(this, binding.root, "Hasil: $result", Snackbar.LENGTH_LONG).show()
        } catch (e:Exception) {
            e.printStackTrace()
            Snackbar.make(this, binding.root, "Error: ${e.message}", Snackbar.LENGTH_LONG).show()
        }
    }
}