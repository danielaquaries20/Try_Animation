package com.example.tryanimation.try_architecture_code.ui.session_activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.tryanimation.R
import com.example.tryanimation.try_architecture_code.data.SharedPreferences
import com.example.tryanimation.try_architecture_code.utils.Constants

class SessionActivity : AppCompatActivity() {

    private var oldName: String? = null
    private var oldEmail: String? = null
    private var oldPassword: String? = null
    private var oldAge: Int = 0

    private lateinit var session: SharedPreferences

    private lateinit var ivBack: ImageView

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etAge: EditText

    private lateinit var btnSave: Button
    private lateinit var btnDelete: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_session)

        session = SharedPreferences(this)

        initData()
        initView()
        initClick()
    }

    private fun initData() {
        oldName = session.getValue(Constants.KEY_USER_NAME, "")
        oldEmail = session.getValue(Constants.KEY_USER_EMAIL, "")
        oldPassword = session.getValue(Constants.KEY_USER_PASSWORD, "")
        oldAge = session.getValue(Constants.KEY_USER_AGE, 0)
//        val oldUmur = session.getValue(Constants.KEY_USER_AGE, "")
//        Toast.makeText(this, "Umur: $oldUmur", Toast.LENGTH_SHORT).show()
    }

    private fun initView() {
        ivBack = findViewById(R.id.ivBack)

        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etAge = findViewById(R.id.etAge)

        btnSave = findViewById(R.id.btnSave)
        btnDelete = findViewById(R.id.btnDelete)

        etName.setText(oldName)
        etEmail.setText(oldEmail)
        etPassword.setText(oldPassword)
        var theAge = ""
        if (oldAge != 0) {
            theAge = oldAge.toString()
        }
        etAge.setText(theAge)

    }

    private fun initClick() {
        ivBack.setOnClickListener { onBackPressed() }
        btnSave.setOnClickListener { validateForm() }
        btnDelete.setOnClickListener {
            if (oldName.isNullOrEmpty() && oldEmail.isNullOrEmpty() && oldPassword.isNullOrEmpty() && oldAge == 0) {
                Toast.makeText(this, "Tidak ada data yang bisa dihapus", Toast.LENGTH_SHORT)
                    .show()
            } else {
                deleteConfirmation()
            }
        }
    }

    private fun validateForm() {
        val name = etName.text.trim().toString()
        val email = etEmail.text.trim().toString()
        val password = etPassword.text.trim().toString()
        val age = etAge.text.trim().toString()

        if (name.isEmpty()) {
            Toast.makeText(this, "Nama harus diisi", Toast.LENGTH_SHORT).show()
            return
        }

        if (email.isEmpty()) {
            Toast.makeText(this, "Email harus diisi", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Kata Sandi harus diisi", Toast.LENGTH_SHORT).show()
            return
        }

        if (age.isEmpty()) {
            Toast.makeText(this, "Umur harus diisi", Toast.LENGTH_SHORT).show()
            return
        }

        if (oldName.isNullOrEmpty() && oldEmail.isNullOrEmpty() && oldPassword.isNullOrEmpty() && oldAge == 0) {
            saveData(name, email, password, age.toInt())
        } else {
            if (name == oldName && email == oldEmail && password == oldPassword && age == oldAge.toString()) {
                Toast.makeText(this, "Tidak ada perubahan data", Toast.LENGTH_SHORT).show()
                return
            }
            saveConfirmation(name, email, password, age.toInt())
        }

    }

    private fun saveData(name: String, email: String, password: String, age: Int) {
        session.setValue(Constants.KEY_USER_NAME, name)
        session.setValue(Constants.KEY_USER_EMAIL, email)
        session.setValue(Constants.KEY_USER_PASSWORD, password)
        session.setValue(Constants.KEY_USER_AGE, age)
        Toast.makeText(this, "Berhasil simpan data", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun deleteData() {
        session.clearAllValue()
        Toast.makeText(this, "Berhasil hapus data", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun saveConfirmation(name: String, email: String, password: String, age: Int) {
        val alertBuilder = AlertDialog.Builder(this).apply {
            setPositiveButton("Simpan") { _, _ ->
                saveData(name, email, password, age)
            }
            setNegativeButton("Batalkan") { _, _ -> }
            setTitle("Konfirmasi Simpan")
            setMessage("Apakah anda yakin ingin menyimpan perubahan?")
        }
        alertBuilder.create()
        alertBuilder.show()
    }

    private fun deleteConfirmation() {
        val alertBuilder = AlertDialog.Builder(this).apply {
            setPositiveButton("Hapus") { _, _ ->
                deleteData()
            }
            setNegativeButton("Batalkan") { _, _ -> }
            setTitle("Konfirmasi Hapus")
            setMessage("Apakah anda yakin ingin menghapus data ini?\nJika dihapus, maka data tidak dapat dikembalikan")
        }
        alertBuilder.create()
        alertBuilder.show()
    }

}