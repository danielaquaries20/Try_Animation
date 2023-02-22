package com.example.tryanimation.try_architecture_code.ui.detail_user

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tryanimation.R

class DetailUserActivity : AppCompatActivity() {

    private var isEdit: Boolean = false

    private lateinit var tvTitle: TextView
    private lateinit var ivDelete: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)

        isEdit = intent.getBooleanExtra("isEdit", false)

        tvTitle = findViewById(R.id.tvTitle)
        ivDelete = findViewById(R.id.icDeleteUser)

        initView()
    }

    private fun initView() {

        if (isEdit) {
            tvTitle.text = "Detail User"
            ivDelete.visibility = View.VISIBLE
        } else {
            tvTitle.text = "Add User"
            ivDelete.visibility = View.GONE
        }

    }
}