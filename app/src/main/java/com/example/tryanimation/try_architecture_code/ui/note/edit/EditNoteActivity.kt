package com.example.tryanimation.try_architecture_code.ui.note.edit

import android.os.Bundle
import com.crocodic.core.api.ApiStatus
import com.crocodic.core.base.activity.CoreActivity
import com.crocodic.core.extension.textOf
import com.crocodic.core.extension.tos
import com.example.tryanimation.R
import com.example.tryanimation.databinding.ActivityEditBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditNoteActivity :
    CoreActivity<ActivityEditBinding, EditNoteViewModel>(R.layout.activity_edit) {

    private var idNote: String? = null
    private var oldTitle: String? = null
    private var oldNote: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observe()
    }

    override fun onBackPressed() {
        validateForm()
    }

    override fun onResume() {
        super.onResume()
        idNote = intent.getStringExtra("id_note")
        oldTitle = intent.getStringExtra("title")
        oldNote = intent.getStringExtra("note")

        binding.etTitle.setText(oldTitle ?: "")
        binding.etNote.setText(oldNote ?: "")
        binding.ivBack.setOnClickListener { onBackPressed() }
        binding.ivDelete.setOnClickListener {
            idNote?.let { id -> viewModel.deleteNote(id) }
        }
    }

    private fun observe() {
        viewModel.editNoteResponse.observe(this) { response ->
            when (response.status) {
                ApiStatus.SUCCESS -> {
                    loadingDialog.dismiss()
                    setResult(102)
                    tos("Catatan diupdate")
                    finish()
                }
                ApiStatus.LOADING -> {
                    loadingDialog.show("Loading..")
                }
                else -> {
                    tos("Gagal mengupdate Catatan, coba lagi!")
                    finish()
                }
            }
        }

        viewModel.deleteNoteResponse.observe(this) { response ->
            when (response.status) {
                ApiStatus.SUCCESS -> {
                    loadingDialog.dismiss()
                    setResult(102)
                    tos("Catatan dihapus")
                    finish()
                }
                ApiStatus.LOADING -> {
                    loadingDialog.show("Loading..")
                }
                else -> {
                    tos("Gagal menghapus Catatan, coba lagi!")
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

        if (title == oldTitle && note == oldNote) {
            finish()
            return
        }

        idNote?.let { viewModel.editNote(it, title, note) }
    }
}