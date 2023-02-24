package com.example.tryanimation.try_architecture_code

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.tryanimation.R
import com.example.tryanimation.try_architecture_code.api.ApiDummyRepository
import com.example.tryanimation.try_architecture_code.ui.list_user.ListUserActivity

class TryArchitectureCodeActivity : AppCompatActivity() {

    private lateinit var viewModel: TryArchitectureViewModel
    private val apiDummyRepository = ApiDummyRepository()

    private lateinit var btnTambah: Button
    private lateinit var btnKurang: Button
    private lateinit var btnGetPost: Button
    private lateinit var btnToListUser: Button

    private lateinit var tvAngka: TextView
    private lateinit var tvBoolean: TextView

    private lateinit var tvUserId: TextView
    private lateinit var tvId: TextView
    private lateinit var tvTitle: TextView
    private lateinit var tvContent: TextView

    private var number: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_try_architecture_code)
        val viewModelFactory = TryArchitectureViewModelFactory(apiDummyRepository)
        viewModel = ViewModelProvider(this, viewModelFactory)[TryArchitectureViewModel::class.java]

        initView()
        observe()
        initClick()
    }

    private fun initView() {
        btnTambah = findViewById(R.id.btnTambah)
        btnKurang = findViewById(R.id.btnKurang)
        btnGetPost = findViewById(R.id.btnGetPost)
        btnToListUser = findViewById(R.id.btnToListUser)

        tvAngka = findViewById(R.id.tvLiveInt)
        tvBoolean = findViewById(R.id.tvLiveBoolean)

        tvUserId = findViewById(R.id.tvUserId)
        tvId = findViewById(R.id.tvId)
        tvTitle = findViewById(R.id.tvTitle)
        tvContent = findViewById(R.id.tvContent)
    }

    private fun observe() {
        viewModel.currentNumber.observe(this) { num ->
            number = num
            tvAngka.text = num.toString()
        }
        viewModel.currentBoolean.observe(this) { bool ->
            if (bool) {
                tvBoolean.text = "Angka habis jika dibagi 2"
            } else {
                tvBoolean.text = "Angka tidak habis jika dibagi 2"
            }

            if (number == 0) {
                tvBoolean.text = "Angka masih kosong"
            }
        }

        viewModel.apiResponse.observe(this) { response ->
            Toast.makeText(this, "Called: ${response.content}", Toast.LENGTH_SHORT).show()
            /*if (response.isSuccessful) {
                tvUserId.text = "User Id: ${response.body()?.userId.toString()}"
                tvId.text = "Post Id: ${response.body()?.id.toString()}"
                tvTitle.text = "Title: ${response.body()?.title.toString()}"
                tvContent.text = "Content: ${response.body()?.content.toString()}"
            } else {
                Toast.makeText(this, "Error message: ${response.code()}", Toast.LENGTH_SHORT).show()
            }*/
        }
    }

    private fun initClick() {
        btnTambah.setOnClickListener {
            viewModel.increase(number)
        }
        btnKurang.setOnClickListener {
            viewModel.decrease(number)
        }

        btnGetPost.setOnClickListener {
            viewModel.getFinalPost()
        }

        btnToListUser.setOnClickListener {
            startActivity(Intent(this, ListUserActivity::class.java))
        }

    }

}