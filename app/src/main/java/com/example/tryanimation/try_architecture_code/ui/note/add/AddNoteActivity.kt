package com.example.tryanimation.try_architecture_code.ui.note.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.crocodic.core.api.ApiStatus
import com.crocodic.core.base.activity.CoreActivity
import com.crocodic.core.extension.snacked
import com.crocodic.core.extension.textOf
import com.crocodic.core.extension.tos
import com.example.tryanimation.R
import com.example.tryanimation.databinding.ActivityAddBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddNoteActivity : CoreActivity<ActivityAddBinding, AddNoteViewModel>(R.layout.activity_add) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observe()
        binding.ivBack.setOnClickListener { onBackPressed() }
    }

    override fun onBackPressed() {
        validateForm()
    }

    private fun observe() {
        viewModel.addNoteResponse.observe(this) { response ->
            when (response.status) {
                ApiStatus.SUCCESS -> {
                    loadingDialog.dismiss()
                    setResult(100)
                    tos("Catatan ditambahkan")
                    finish()
                }
                ApiStatus.LOADING -> {
                    loadingDialog.show("Loading..")
                }
                else -> {
                    tos("Gagal menambahkan Catatan, coba lagi!")
                    finish()
                }
            }

        }
    }

    private fun validateForm() {
        val title = binding.etTitle.textOf()
        val note = binding.etNote.textOf()

        if (title.isEmpty()) {
            finish()
            return
        }

        if (note.isEmpty()) {
            finish()
            return
        }

        viewModel.addNote(title, note)
    }
}