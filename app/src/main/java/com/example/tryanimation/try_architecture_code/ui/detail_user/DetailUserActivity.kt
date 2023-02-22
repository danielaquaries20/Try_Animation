package com.example.tryanimation.try_architecture_code.ui.detail_user

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.tryanimation.R
import kotlinx.coroutines.launch

class DetailUserActivity : AppCompatActivity() {

    private lateinit var viewModel: DetailUserViewModel

    private var isEdit: Boolean = false

    private lateinit var tvTitle: TextView
    private lateinit var ivDelete: ImageView
    private lateinit var ivBack: ImageView

    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etAge: EditText
    private lateinit var etBio: EditText

    private lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)

        val viewModelFactory = DetailUserViewModelFactory(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[DetailUserViewModel::class.java]

        isEdit = intent.getBooleanExtra("isEdit", false)

        tvTitle = findViewById(R.id.tvTitle)
        ivDelete = findViewById(R.id.icDeleteUser)
        ivBack = findViewById(R.id.ivBack)
        etFirstName = findViewById(R.id.etFirstUsername)
        etLastName = findViewById(R.id.etLastUsername)
        etAge = findViewById(R.id.etAge)
        etBio = findViewById(R.id.etBio)
        btnSave = findViewById(R.id.btnSave)

        initView()
        observe()
        initClick()
    }

    private fun observe() {
        lifecycleScope.launch {
            viewModel.response.observe(this@DetailUserActivity) { response ->
                if (response == 1) {
                    Toast.makeText(this@DetailUserActivity,
                        "Berhasil menambahkan User",
                        Toast.LENGTH_SHORT).show()
                    onBackPressed()
                } else {
                    Toast.makeText(this@DetailUserActivity,
                        "Gagal menambahkan User",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
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

    private fun initClick() {

        ivBack.setOnClickListener { onBackPressed() }

        ivDelete.setOnClickListener {
            Toast.makeText(this, "Delete User", Toast.LENGTH_SHORT).show()
        }

        btnSave.setOnClickListener { validateForm() }
    }

    private fun validateForm() {
        val firstName = etFirstName.text.trim().toString()
        val lastName = etLastName.text.trim().toString()
        val age = etAge.text.trim().toString()
        val bio = etBio.text.trim().toString()

        if (firstName.isEmpty()) {
            Toast.makeText(this, "Nama depan harus diisi", Toast.LENGTH_SHORT).show()
            return
        }

        if (lastName.isEmpty()) {
            Toast.makeText(this, "Nama belakang harus diisi", Toast.LENGTH_SHORT).show()
            return
        }

        if (age.isEmpty()) {
            viewModel.addUser(firstName, lastName, null, bio)
        } else {
            viewModel.addUser(firstName, lastName, age.toInt(), bio)
        }

    }

}