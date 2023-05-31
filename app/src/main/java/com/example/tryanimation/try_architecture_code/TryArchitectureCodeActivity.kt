package com.example.tryanimation.try_architecture_code

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.tryanimation.R
import com.example.tryanimation.try_architecture_code.api.ApiDummyRepository
import com.example.tryanimation.try_architecture_code.database.user.UserEntity
import com.example.tryanimation.try_architecture_code.database.user.UserRepository
import com.example.tryanimation.try_architecture_code.model.Post
import com.example.tryanimation.try_architecture_code.ui.list_post.ListPostActivity
import com.example.tryanimation.try_architecture_code.ui.list_user.ListUserActivity
import com.example.tryanimation.try_architecture_code.ui.login.LoginActivity
import com.example.tryanimation.try_architecture_code.ui.session_activity.SessionActivity
import com.example.tryanimation.try_architecture_code.ui.try_binding.TryBindingActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TryArchitectureCodeActivity : AppCompatActivity() {

    private lateinit var viewModel: TryArchitectureViewModel
    private val apiDummyRepository = ApiDummyRepository()

    @Inject
    lateinit var userRepository: UserRepository

    private lateinit var btnTambah: Button
    private lateinit var btnKurang: Button
    private lateinit var btnListPost: Button
    private lateinit var btnGetPost: Button
    private lateinit var btnGetSpecificPost: Button
    private lateinit var btnCreatePost: Button
    private lateinit var btnCreatePostByJson: Button
    private lateinit var btnToListUser: Button
    private lateinit var btnToSession: Button
    private lateinit var btnTryBinding: Button
    private lateinit var btnTryLogin: Button
    private lateinit var btnTryAddUser: Button

    private lateinit var tvAngka: TextView
    private lateinit var tvBoolean: TextView

    private lateinit var tvUserId: TextView
    private lateinit var tvId: TextView
    private lateinit var tvTitle: TextView
    private lateinit var tvContent: TextView
    private lateinit var tvListPost: TextView

    private lateinit var frameLoading: FrameLayout

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
        btnCreatePost = findViewById(R.id.btnCreatePost)
        btnCreatePostByJson = findViewById(R.id.btnCreatePostByJson)
        btnTryBinding = findViewById(R.id.btnTryBinding)
        btnTryLogin = findViewById(R.id.btnTryLogin)
        btnTryAddUser = findViewById(R.id.btnTryAddUser)

        tvAngka = findViewById(R.id.tvLiveInt)
        tvBoolean = findViewById(R.id.tvLiveBoolean)

        tvUserId = findViewById(R.id.tvUserId)
        tvId = findViewById(R.id.tvId)
        tvTitle = findViewById(R.id.tvTitle)
        tvContent = findViewById(R.id.tvContent)
        tvListPost = findViewById(R.id.tvListPost)

        frameLoading = findViewById(R.id.frameLoading)
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

            frameLoading.visibility = View.GONE

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
            frameLoading.visibility = View.VISIBLE
            viewModel.getFinalPost()
        }

        btnGetSpecificPost.setOnClickListener {
            if (number in 1..100) {
                frameLoading.visibility = View.VISIBLE
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

        btnCreatePost.setOnClickListener {
            createFinalPost()
        }

        btnCreatePostByJson.setOnClickListener {
            createFinalPostByJson()
        }

        btnTryBinding.setOnClickListener {
            startActivity(Intent(this, TryBindingActivity::class.java))
//            createFinalPostByJson()
        }

        btnTryLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        btnTryAddUser.setOnClickListener {
            addUser()
        }

    }

    private fun addUser() {
        lifecycleScope.launch {
            val user = UserEntity(1, "Daniel", "Pratama", 19, "Hello My Name is Daniel")
            userRepository.addUser(user)
            Toast.makeText(this@TryArchitectureCodeActivity,
                "Berhasil menambahkan User",
                Toast.LENGTH_SHORT).show()
        }
    }

    private fun createFinalPost() {
        frameLoading.visibility = View.VISIBLE
        val userId = 11
        val id = 101
        val title = "Try Post 1"
        val body = "Hello there, this is just try post from Form Url Encoded."

        viewModel.createPost(userId, id, title, body)
    }

    private fun createFinalPostByJson() {
        frameLoading.visibility = View.VISIBLE

        val post = Post(11, 101, "Try Post 2", "Hey there, do you read this? This is from Json.")

        viewModel.createPostByJson(post)
    }

}