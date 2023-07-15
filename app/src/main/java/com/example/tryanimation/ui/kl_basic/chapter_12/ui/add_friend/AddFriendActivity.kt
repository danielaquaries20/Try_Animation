package com.example.tryanimation.ui.kl_basic.chapter_12.ui.add_friend

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tryanimation.databinding.ActivityAddFriendBinding
import com.example.tryanimation.ui.kl_basic.chapter_12.data.database.FriendEntity
import com.example.tryanimation.ui.kl_basic.chapter_12.data.database.MyDatabase
import com.example.tryanimation.ui.kl_basic.chapter_12.ui.test_ui.test_helper.TestBitmapHelper
import com.example.tryanimation.ui.kl_basic.chapter_12.utils.MyBitmapHelper
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

class AddFriendActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddFriendBinding

    private lateinit var myDatabase: MyDatabase

    private var dataFriend : FriendEntity? = null

    private lateinit var photoFile : File
    private var photo : String? = null

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

                photo = MyBitmapHelper().encodeToBase64(takenImage)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFriendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myDatabase = MyDatabase.getDatabase(this)

        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.trim().toString()
            val school = binding.etSchool.text.trim().toString()
            val hobby = binding.etHobby.text.trim().toString()

            if (name.isEmpty()) {
                binding.etName.error = "Fill blank form"
                return@setOnClickListener
            }

            if (school.isEmpty()) {
                binding.etSchool.error = "Fill blank form"
                return@setOnClickListener
            }

            if (hobby.isEmpty()) {
                binding.etHobby.error = "Fill blank form"
                return@setOnClickListener
            }

            if (isEdit()) {
                if (name == dataFriend?.name && hobby == dataFriend?.hobby && school == dataFriend?.school) {
                    Toast.makeText(this, "Tidak ada perubahan data", Toast.LENGTH_SHORT).show()
                } else {
                    val updatedFriend = FriendEntity(name, school, hobby, photo).apply {
                        id = dataFriend?.id!!
                    }
                    lifecycleScope.launch {
                        myDatabase.friendDao().update(updatedFriend)
                        Toast.makeText(this@AddFriendActivity, "Updated", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            } else {
                val newFriend = FriendEntity(name, school, hobby, photo)
                lifecycleScope.launch {
                    myDatabase.friendDao().insert(newFriend)
                    Toast.makeText(this@AddFriendActivity, "Inserted", Toast.LENGTH_SHORT).show()
                    finish()
                }

            }

        }
        binding.ivPhoto.setOnClickListener { openGallery() }

        val name = intent.getStringExtra("name") ?: ""
        val school = intent.getStringExtra("school") ?: ""
        val hobby = intent.getStringExtra("hobby") ?: ""
        photo = intent.getStringExtra("photo")
        val idFriend = intent.getIntExtra("id", 0)
        dataFriend = FriendEntity(name, school, hobby).apply {
            id = idFriend
        }

        photoFile = try {
            creteImageFile()
        } catch (ex: IOException) {
            Toast.makeText(this, "Cannot use Camera", Toast.LENGTH_SHORT).show()
            return
        }

        if (isEdit()) {
            val bitmapPhoto = MyBitmapHelper().decodeBase64(this, photo)
            if (bitmapPhoto != null) binding.ivPhoto.setImageBitmap(bitmapPhoto)

            binding.etHobby.setText(hobby)
            binding.etName.setText(name)
            binding.etSchool.setText(school)
            binding.btnDelete.visibility = View.VISIBLE

            binding.btnDelete.setOnClickListener {
                deleteFriend()
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
                    myDatabase.friendDao().delete(friend)
                    dialog.dismiss()
                    Toast.makeText(this@AddFriendActivity, "Deleted", Toast.LENGTH_SHORT).show()
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
    
    private fun isEdit(): Boolean {
        return dataFriend != null && dataFriend?.id != 0
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

    @Throws(IOException::class)
    private fun creteImageFile(): File {
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("PHOTO_", ".jpg", storageDir)
    }

}