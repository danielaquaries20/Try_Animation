package com.example.tryanimation.try_architecture_code.ui.list_post

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.tryanimation.R
import com.example.tryanimation.try_architecture_code.api.ApiDummyRepository
import com.example.tryanimation.try_architecture_code.database.user.UserEntity
import com.example.tryanimation.try_architecture_code.ui.list_user.AdapterRVListUser
import kotlinx.coroutines.launch

class ListPostActivity : AppCompatActivity() {

    private lateinit var viewModel: ListPostViewModel
    private val apiDummyRepository = ApiDummyRepository()

    private lateinit var ivBack: ImageView

    private lateinit var tvTitle: TextView
    private lateinit var tvDataEmpty: TextView

    private lateinit var progressBar: ProgressBar

    private lateinit var rvPost: RecyclerView

    private var idUserGet: Int? = null

    private var adapterListPost: AdapterRVListUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_post)

        val viewModelFactory = ListPostViewModelFactory(apiDummyRepository)
        viewModel = ViewModelProvider(this, viewModelFactory)[ListPostViewModel::class.java]

        initView()
        initClick()

        observe()
    }

    private fun initView() {
        idUserGet = intent.getIntExtra("idUser", 0)

        ivBack = findViewById(R.id.ivBack)
        tvTitle = findViewById(R.id.tvTitle)
        tvDataEmpty = findViewById(R.id.tvDataEmpty)
        progressBar = findViewById(R.id.progressBar)
        rvPost = findViewById(R.id.rvPost)

        initRV()

        tvTitle.text = "Daftar Postingan - UserId: $idUserGet"
        getListPost()
    }

    private fun initRV() {
        adapterListPost = AdapterRVListUser(this) { data, _ ->
            Toast.makeText(this, "Click Title: ${data.firstName}", Toast.LENGTH_SHORT).show()
        }
        rvPost.adapter = adapterListPost
    }

    private fun initClick() {
        ivBack.setOnClickListener { onBackPressed() }
    }

    private fun observe() {
        lifecycleScope.launch {
            viewModel.listPost.observe(this@ListPostActivity) { list ->
                if (list.isNotEmpty()) {
                    tvDataEmpty.visibility = View.GONE
                    val listPost = ArrayList<UserEntity>()
                    list.forEach { post ->
                        val newPost = UserEntity(post.id ?: 0, post.title, null, null, post.content)
                        listPost.add(newPost)
                    }
                    adapterListPost?.setData(listPost)
                    progressBar.visibility = View.GONE
                } else {
                    tvDataEmpty.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun getListPost() {
        progressBar.visibility = View.VISIBLE
        viewModel.getListPostByUserId(idUserGet.toString())
    }


}