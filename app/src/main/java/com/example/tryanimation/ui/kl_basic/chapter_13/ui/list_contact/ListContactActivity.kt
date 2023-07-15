package com.example.tryanimation.ui.kl_basic.chapter_13.ui.list_contact

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.tryanimation.R
import com.example.tryanimation.databinding.ActivityListContactBinding
import com.example.tryanimation.ui.kl_basic.chapter_13.test_ui.contact.GetContactActivity

class ListContactActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListContactBinding

    private val listContact = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_list_contact)

        binding.activity = this
        binding.adapter = AdapterRvListContact(listContact)

        initCheck()
    }

    fun initCheck() {
        val permissionResult =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)

        if (permissionResult == PackageManager.PERMISSION_GRANTED) {
            getContact()
        } else {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_CONTACTS),
                REQUEST_PERMISSION_FOR_READ_CONTACT)
        }
    }

    private fun getContact() {
        val contacts = ArrayList<String>()
        val cur =
            contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)

        if ((cur?.count ?: 0) > 0) {
            while (cur?.moveToNext() == true) {
                val name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                    ?: return)
                contacts.add(name)
            }
        }

        cur?.close()

        listContact.clear()
        binding.adapter?.notifyDataSetChanged()
        listContact.addAll(contacts)
        binding.adapter?.notifyItemInserted(0)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_PERMISSION_FOR_READ_CONTACT) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                getContact()
            }
        }
    }


    companion object {
        const val REQUEST_PERMISSION_FOR_READ_CONTACT = 100
    }

}