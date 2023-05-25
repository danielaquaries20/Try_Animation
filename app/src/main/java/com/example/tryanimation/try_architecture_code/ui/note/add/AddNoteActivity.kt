package com.example.tryanimation.try_architecture_code.ui.note.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tryanimation.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddNoteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
    }
}