package com.example.tryanimation.try_architecture_code.ui.list_user

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.tryanimation.R
import com.example.tryanimation.try_architecture_code.ui.detail_user.DetailUserActivity
import kotlinx.coroutines.launch

class ListUserActivity : AppCompatActivity() {

    private lateinit var viewModel: ListUserViewModel

    private lateinit var ivBack: ImageView
    private lateinit var ivAddUser: ImageView
    private lateinit var ivDeleteUser: ImageView
    private lateinit var tvDataEmpty: TextView
    private lateinit var rvListUser: RecyclerView

    private var adapterListUser: AdapterRVListUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_user)

        val viewModelFactory = ListUserViewModelFactory(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[ListUserViewModel::class.java]

        ivBack = findViewById(R.id.ivBack)
        ivAddUser = findViewById(R.id.icAddUser)
        ivDeleteUser = findViewById(R.id.icDeleteUser)
        tvDataEmpty = findViewById(R.id.tvDataEmpty)
        rvListUser = findViewById(R.id.rvListUser)

        initView()
        initClick()
        observe()

    }

    private fun observe() {
        lifecycleScope.launch {
            viewModel.listUser?.observe(this@ListUserActivity) {
                if (it.isEmpty()) {
                    tvDataEmpty.visibility = View.VISIBLE
                } else {
                    tvDataEmpty.visibility = View.GONE
                }
                adapterListUser?.setData(it)
            }
        }
    }

    private fun initView() {
        adapterListUser = AdapterRVListUser(this)
        rvListUser.adapter = adapterListUser
    }

    private fun initClick() {
        ivBack.setOnClickListener { onBackPressed() }
        ivDeleteUser.setOnClickListener {
            Toast.makeText(this, "Delete All User", Toast.LENGTH_SHORT).show()
        }
        ivAddUser.setOnClickListener {
            val toAdd = Intent(this@ListUserActivity, DetailUserActivity::class.java).apply {
                putExtra("isEdit", false)
            }
            startActivity(toAdd)
        }
    }

}