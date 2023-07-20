package com.example.tryanimation.ui.kl_basic.chapter_12.ui.test_ui.test_db

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.example.tryanimation.databinding.ActivityInsertDatabaseBinding
import com.example.tryanimation.ui.kl_basic.chapter_12.data.test_data.room_database.TryFriendEntity
import com.example.tryanimation.ui.kl_basic.chapter_12.data.test_data.room_database.TryMyDatabase
import com.example.tryanimation.ui.kl_basic.chapter_12.ui.test_ui.test_helper.TestBitmapHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

class InsertDatabaseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInsertDatabaseBinding

    private var isEdit = false
    private var idFriend = 0

    private lateinit var myDatabase: TryMyDatabase

    private var dataFriend: TryFriendEntity? = null

    private lateinit var photoFile: File

    private var photoProfile: String? = null

    private var galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val parcelFileDescriptor = contentResolver.openFileDescriptor(result?.data?.data
                    ?: return@registerForActivityResult, "r")
                val fileDescriptor = parcelFileDescriptor?.fileDescriptor
                val inputStream = FileInputStream(fileDescriptor)

                val outputStream = FileOutputStream(photoFile)

                inputStream.use { input ->
                    outputStream.use { output ->
                        input.copyTo(output)
                    }
                }

                parcelFileDescriptor?.close()

                val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)
                binding.ivPhoto.setImageBitmap(takenImage)

                photoProfile = TestBitmapHelper().encodeToBase64(takenImage)
            }
        }

    private var cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)
                binding.ivPhoto.setImageBitmap(takenImage)

                photoProfile = TestBitmapHelper().encodeToBase64(takenImage)
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsertDatabaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isEdit = intent.getBooleanExtra("isEdit", false)
        idFriend = intent.getIntExtra("id", 0)

        myDatabase = TryMyDatabase.getInstance(this)

        initView()

    }

    private fun initView() {
        binding.btnSave.setOnClickListener { saveData() }
        binding.ivPhoto.setOnClickListener { showDialogPhoto() }

        photoFile = try {
            creteImageFile()
        } catch (ex: IOException) {
            Toast.makeText(this, "Cannot use Camera", Toast.LENGTH_SHORT).show()
            return
        }

        isEditState()
    }

    private fun isEditState() {
        if (isEdit) {
            lifecycleScope.launch {
                launch {
                    myDatabase.tryFriendDao().getDataById(idFriend).collect {
                        Log.d("CheckData", "DATA: $it")
                        dataFriend = it
                        photoProfile = it?.photo
                        val photoBitmap =
                            TestBitmapHelper().decodeBase64(this@InsertDatabaseActivity,
                                photoProfile)
                        if (photoBitmap != null) binding.ivPhoto.setImageBitmap(photoBitmap)

                        binding.etName.setText(it?.name)
                        binding.etSchool.setText(it?.school)
                        binding.etHobby.setText(it?.hobby)
                        binding.btnDelete.visibility = View.VISIBLE
                        binding.btnDelete.setOnClickListener { deleteFriend() }
                    }
                }
            }
        }

    }

    private fun deleteFriend() {
        dataFriend?.let {friend ->
            val builder = AlertDialog.Builder(this)

            builder.setTitle("Konfirmasi Delete")
            builder.setMessage("Apakah kamu yakin ingin menghapus ${friend.name}?")

            builder.setPositiveButton("Hapus") { dialog: DialogInterface, which: Int ->
                lifecycleScope.launch {
                    myDatabase.tryFriendDao().delete(friend)
                    dialog.dismiss()
                    Toast.makeText(this@InsertDatabaseActivity, "Deleted", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

            builder.setNegativeButton("Batal") { dialog: DialogInterface, which: Int ->
                dialog.dismiss()
            }

            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()
        }
    }

    private fun showDialogPhoto() {
        val builder = AlertDialog.Builder(this)
        val arrayChoose = arrayOf("Dari kamera", "Pilih di gallery")
        builder.setTitle("Memasukkan photo")
        builder.setItems(arrayChoose) { _, index ->
            when (index) {
                0 -> openCamera()
                1 -> openGallery()
            }
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    private fun saveData() {
        val name = binding.etName.text.trim().toString()
        val school = binding.etSchool.text.trim().toString()
        val hobby = binding.etHobby.text.trim().toString()

        if (name.isEmpty()) {
            binding.etName.error = "Fill the name form"
            return
        }

        if (school.isEmpty()) {
            binding.etSchool.error = "Fill the school form"
            return
        }

        if (hobby.isEmpty()) {
            binding.etHobby.error = "Fill the hobby form"
            return
        }


        if (isEdit) {
            if (dataFriend?.name == name && dataFriend?.school == school && dataFriend?.hobby == hobby) {
                Toast.makeText(this, "Tidak ada perubahan data", Toast.LENGTH_SHORT).show()
            } else {
                val updatedFriend = TryFriendEntity(name, school, hobby, photoProfile).apply {
                    id = dataFriend?.id!!
                }
                lifecycleScope.launch {
                    myDatabase.tryFriendDao().update(updatedFriend)
                    Toast.makeText(this@InsertDatabaseActivity, "Updated", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                }
            }

        } else {
            val friend = TryFriendEntity(name, school, hobby, photoProfile)
            lifecycleScope.launch {
                myDatabase.tryFriendDao().insert(friend)
                Toast.makeText(this@InsertDatabaseActivity, "Inserted", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        try {
            galleryLauncher.launch(galleryIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "Cannot use Gallery", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    private fun openCamera() {
        val photoURI =
            FileProvider.getUriForFile(this, "com.example.tryanimation.fileprovider", photoFile)

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        }

        try {
            cameraLauncher.launch(cameraIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "Cannot use Camera", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }


    @Throws(IOException::class)
    private fun creteImageFile(): File {
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("PHOTO_", ".jpg", storageDir)
    }

}