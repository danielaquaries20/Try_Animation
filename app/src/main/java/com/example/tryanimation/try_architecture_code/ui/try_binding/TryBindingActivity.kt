package com.example.tryanimation.try_architecture_code.ui.try_binding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.tryanimation.R
import com.example.tryanimation.databinding.ActivityTryBindingBinding
import com.example.tryanimation.try_architecture_code.model.Post
import com.example.tryanimation.try_architecture_code.utils.BindingHelper
import com.example.tryanimation.try_architecture_code.utils.LocationAddress

class TryBindingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTryBindingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = ActivityTryBindingBinding.inflate(layoutInflater)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_try_binding)
        setContentView(binding.root)

        initView()
        initClick()
    }

    private fun initView() {
        binding.tvTitle.text = "Coba Binding (just view)"

        binding.helper = BindingHelper(this)
        binding.post = Post(1,
            1,
            "Saya suka main games",
            "Main game adalah aktivitas yang menyenangkan.\n\nTapi terkadang juga ada yang membuat marah.")
    }

    private fun initClick() {
        binding.ivBack.setOnClickListener { onBackPressed() }
        binding.btnShowAddress.setOnClickListener {
            getAddress()
        }
        /*binding.btnShowTitle.setOnClickListener {
            Toast.makeText(this, "Title: ${binding.post?.title}", Toast.LENGTH_SHORT).show()
        }*/
    }

    private fun getAddress() {
        val latitude = 36.1252285
        val longitude = -115.4551946
        val locationAddress = LocationAddress()
        binding.address = locationAddress.getAddressFromLocation(latitude, longitude, this)
    }
}