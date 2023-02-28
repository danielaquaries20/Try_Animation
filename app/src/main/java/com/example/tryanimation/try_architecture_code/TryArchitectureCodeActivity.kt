package com.example.tryanimation.try_architecture_code

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.tryanimation.R
import com.example.tryanimation.try_architecture_code.api.ApiDummyRepository
import com.example.tryanimation.try_architecture_code.ui.list_post.ListPostActivity
import com.example.tryanimation.try_architecture_code.ui.list_user.ListUserActivity
import com.example.tryanimation.try_architecture_code.ui.session_activity.SessionActivity

class TryArchitectureCodeActivity : AppCompatActivity() {

    private lateinit var viewModel: TryArchitectureViewModel
    private val apiDummyRepository = ApiDummyRepository()

    private lateinit var btnTambah: Button
    private lateinit var btnKurang: Button
    private lateinit var btnListPost: Button
    private lateinit var btnGetPost: Button
    private lateinit var btnGetSpecificPost: Button
    private lateinit var btnToListUser: Button
    private lateinit var btnToSession: Button

    private lateinit var tvAngka: TextView
    private lateinit var tvBoolean: TextView

    private lateinit var tvUserId: TextView
    private lateinit var tvId: TextView
    private lateinit var tvTitle: TextView
    private lateinit var tvContent: TextView
    private lateinit var tvListPost: TextView

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
        btnListPost = findViewById(R.id.btnListPost)
        btnGetPost = findViewById(R.id.btnGetPost)
        btnGetSpecificPost = findViewById(R.id.btnGetSpecificPost)
        btnToListUser = findViewById(R.id.btnToListUser)
        btnToSession = findViewById(R.id.btnToSharedPreferences)

        tvAngka = findViewById(R.id.tvLiveInt)
        tvBoolean = findViewById(R.id.tvLiveBoolean)

        tvUserId = findViewById(R.id.tvUserId)
        tvId = findViewById(R.id.tvId)
        tvTitle = findViewById(R.id.tvTitle)
        tvContent = findViewById(R.id.tvContent)
        tvListPost = findViewById(R.id.tvListPost)
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
//            Toast.makeText(this, "Called: ${response.content}", Toast.LENGTH_SHORT).show()
            if (response.id != 0) {
                tvUserId.text = "User Id: ${response.userId.toString()}"
                tvId.text = "Post Id: ${response.id.toString()}"
                tvTitle.text = "Title: ${response.title.toString()}"
                tvContent.text = "Content: ${response.content.toString()}"
            } else {
                Toast.makeText(this, "${response.title}: ${response.content}", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        viewModel.listPost.observe(this) { listResp ->
            val postResponse = listResp[0]
            if (postResponse.id == 0) {
                tvListPost.visibility = View.GONE
                Toast.makeText(this,
                    "${postResponse.title}: ${postResponse.content}",
                    Toast.LENGTH_SHORT).show()
            } else {
                tvListPost.visibility = View.VISIBLE
                tvListPost.text = "Data: $listResp"
            }
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

        btnGetSpecificPost.setOnClickListener {
            if (number in 1..100) {
                viewModel.getSpecificPost(number.toString())
            } else {
                Toast.makeText(this, "Tidak ada data untuk Id tersebut", Toast.LENGTH_SHORT).show()
            }
        }

        btnListPost.setOnClickListener {
            val goToListPost = Intent(this, ListPostActivity::class.java).apply {
                putExtra("idUser", number)
            }
            startActivity(goToListPost)
            /*if (number in 1..10) {
                viewModel.getListPostByUserId(number.toString())
            } else {
                Toast.makeText(this, "Tidak ada data untuk Id tersebut", Toast.LENGTH_SHORT).show()
            }*/
        }

        btnToListUser.setOnClickListener {
            startActivity(Intent(this, ListUserActivity::class.java))
        }

        btnToSession.setOnClickListener {
            startActivity(Intent(this, SessionActivity::class.java))
        }

    }

}