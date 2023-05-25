package com.example.tryanimation.try_architecture_code.ui.note.edit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tryanimation.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditNoteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
    }
}