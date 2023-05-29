package com.example.tryanimation.try_architecture_code.ui.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.crocodic.core.api.ApiStatus
import com.crocodic.core.base.activity.CoreActivity
import com.crocodic.core.extension.snacked
import com.crocodic.core.extension.text
import com.crocodic.core.extension.textOf
import com.crocodic.core.helper.DateTimeHelper
import com.crocodic.core.helper.StringHelper
import com.example.tryanimation.R
import com.example.tryanimation.databinding.ActivityEditProfileBinding
import com.example.tryanimation.try_architecture_code.database.user.UserEntity
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

@AndroidEntryPoint
class EditProfileActivity :
    CoreActivity<ActivityEditProfileBinding, EditProfileViewModel>(R.layout.activity_edit_profile) {

    private var myUser: UserEntity? = null
//    private var myUser: User2? = null

    private var filePhoto: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observe()

        binding.ivBack.setOnClickListener(this)
        binding.ivPhotoProfile.setOnClickListener(this)
        binding.btnSave.setOnClickListener(this)
    }

    private fun observe() {
        viewModel.users.observe(this) { user ->
//            myUser = user
            if (user != null) {
                binding.etUsername.text(user.name)
                val avatar = StringHelper.generateTextDrawable(
                    StringHelper.getInitial(user.name?.trim()),
                    ContextCompat.getColor(this, R.color.teal_200),
                    binding.ivPhotoProfile.measuredWidth
                )
                if (user.photo.isNullOrEmpty()) {
                    binding.ivPhotoProfile.setImageDrawable(avatar)
                } else {
                    val requestOption = RequestOptions().placeholder(avatar).circleCrop()
                    Glide
                        .with(this)
                        .load(StringHelper.validateEmpty(user.photo))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .apply(requestOption)
                        .error(avatar)
                        .into(binding.ivPhotoProfile)
                }
            } else {
                binding.etUsername.text("Username")
            }
        }

        viewModel.updateProfileResponse.observe(this) { response ->
            when (response.status) {
                ApiStatus.SUCCESS -> {
                    binding.root.snacked("Berhasil Update Profile")
                    loadingDialog.dismiss()
                    finish()
                }
                ApiStatus.LOADING -> {
                    loadingDialog.show("Loading...")
                }
                else -> {
                    loadingDialog.dismiss()
                    binding.root.snacked("Ada masalah saat Update Profile, silahkan coba lagi")
                }
            }
        }



        viewModel.user.observe(this) { user ->
            myUser = user
            if (user != null) {
                val username = user.firstName.toString()
                binding.etUsername.setText(username)
                val avatar = StringHelper.generateTextDrawable(
                    StringHelper.getInitial(username.trim()),
                    ContextCompat.getColor(this, R.color.teal_200),
                    binding.ivPhotoProfile.measuredWidth
                )
                binding.ivPhotoProfile.setImageDrawable(avatar)

                if (user.lastName.isNullOrEmpty()) {
                    binding.ivPhotoProfile.setImageDrawable(avatar)
                } else {
                    val requestOption = RequestOptions().placeholder(avatar).circleCrop()
                    Glide
                        .with(this)
                        .load(StringHelper.validateEmpty(user.lastName))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .apply(requestOption)
                        .error(avatar)
                        .into(binding.ivPhotoProfile)
                }

            }
        }


    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            binding.ivBack -> onBackPressed()
            binding.btnSave -> validateForm()
            binding.ivPhotoProfile -> checkGallery()
        }
    }

    private fun checkGallery() {
        if (checkPermissionGallery()) {
            openGalleryResult()
        } else {
            requestPermissionGallery()
        }
    }

    private fun validateForm() {
        val username = binding.etUsername.textOf()
        val oldName = myUser?.firstName ?: ""

        if (filePhoto == null) {
            if (username.isEmpty()) {
                binding.root.snacked("Username tidak boleh kosong")
                return
            }
            if (username == oldName) {
                binding.root.snacked("Tidak ada perubahan data")
                return
            }
            viewModel.updateProfileName(username)
        } else {
            viewModel.updateProfile(username, filePhoto!!)
        }


    }

    /*region All we need for Gallery*/
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 110) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGalleryResult()
                binding.root.snacked("Permission Granted")
            } else {
                binding.root.snacked("Permission Denied")
            }
        }
    }

    private fun checkPermissionGallery(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun openGalleryResult() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activityLauncher.launch(galleryIntent) { activityResult ->
            activityResult.data?.data?.let {
                generateFileImage(it)
            }
        }
    }

    private fun requestPermissionGallery() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
            110
        )
    }

    private fun generateFileImage(uri: Uri) {
        try {
            val parcelFileDescriptor = contentResolver.openFileDescriptor(uri, "r")
            val fileDescriptor = parcelFileDescriptor?.fileDescriptor
            val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
            parcelFileDescriptor?.close()

            val orientation = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                getOrientation2(uri)
            } else {
                getOrientation(uri)
            }

            val file = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                createImageFile()
            } else {
                //File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)}" + File.separator + "BurgerBangor", getNewFileName())
                File(externalCacheDir?.absolutePath, getNewFileName())
            }

            val fos = FileOutputStream(file)
            var bitmap = image

            if (orientation != -1 && orientation != 0) {

                val matrix = Matrix()
                when (orientation) {
                    6 -> matrix.postRotate(90f)
                    3 -> matrix.postRotate(180f)
                    8 -> matrix.postRotate(270f)
                    else -> matrix.postRotate(orientation.toFloat())
                }
                bitmap =
                    Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            }

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            Glide
                .with(this)
                .load(bitmap)
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(RequestOptions.circleCropTransform())
                .into(binding.ivPhotoProfile)
//            binding.ivPhotoProfile.setImageBitmap(bitmap)
            filePhoto = file
        } catch (e: Exception) {
            e.printStackTrace()
            binding.root.snacked("File ini tidak dapat digunakan")
        }
    }

    @SuppressLint("Range")
    private fun getOrientation(shareUri: Uri): Int {
        val orientationColumn = arrayOf(MediaStore.Images.Media.ORIENTATION)
        val cur = contentResolver.query(
            shareUri,
            orientationColumn,
            null,
            null,
            null
        )
        var orientation = -1
        if (cur != null && cur.moveToFirst()) {
            if (cur.columnCount > 0) {
                orientation = cur.getInt(cur.getColumnIndex(orientationColumn[0]))
            }
            cur.close()
        }
        return orientation
    }

    @SuppressLint("NewApi")
    private fun getOrientation2(shareUri: Uri): Int {
        val inputStream = contentResolver.openInputStream(shareUri)
        return getOrientation3(inputStream)
    }

    @SuppressLint("NewApi")
    private fun getOrientation3(inputStream: InputStream?): Int {
        val exif: ExifInterface
        var orientation = -1
        inputStream?.let {
            try {
                exif = ExifInterface(it)
                orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return orientation
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = DateTimeHelper().createAtLong().toString()
        val storageDir =
            getAppSpecificAlbumStorageDir(Environment.DIRECTORY_DOCUMENTS, "Attachment")
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        )
    }

    private fun getNewFileName(isPdf: Boolean = false): String {
        val timeStamp = DateTimeHelper().createAtLong().toString()
        return if (isPdf) "PDF_${timeStamp}_.pdf" else "JPEG_${timeStamp}_.jpg"
    }

    private fun getAppSpecificAlbumStorageDir(albumName: String, subAlbumName: String): File {
        // Get the pictures directory that's inside the app-specific directory on
        // external storage.
        val file = File(getExternalFilesDir(albumName), subAlbumName)
        if (!file.mkdirs()) {
            //Log.e("fssfsf", "Directory not created")
        }
        return file
    }
    /*endregion*/

}