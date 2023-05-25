package com.example.tryanimation.try_architecture_code.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tryanimation.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
    }
}