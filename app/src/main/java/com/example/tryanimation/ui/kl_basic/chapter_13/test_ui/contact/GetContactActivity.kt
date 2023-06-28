package com.example.tryanimation.ui.kl_basic.chapter_13.test_ui.contact

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.tryanimation.databinding.ActivityGetContactBinding
import com.example.tryanimation.ui.kl_basic.chapter_13.adapter.AdapterRvContact

class GetContactActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGetContactBinding

    private val contacts = ArrayList<String>()

    private var adapter: AdapterRvContact? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGetContactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = AdapterRvContact(contacts)
        binding.rvContacts.adapter = adapter

        binding.btnGetContact.setOnClickListener {
            askPermission()
        }

    }

    private fun askPermission() {
        val permissionResult =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)

        if (permissionResult == PackageManager.PERMISSION_GRANTED) {
            getContact()
        } else {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_CONTACTS),
                REQUEST_PERMISSION_READ_CONTACTS)
        }
    }

    private fun getContact() {
        val listContact = ArrayList<String>()
        val cur =
            contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)

        if ((cur?.count ?: 0) > 0) {
            while (cur?.moveToNext() == true) {
                val name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                    ?: return)
                listContact.add(name)
            }
        }

        cur?.close()

        contacts.clear()
        adapter?.notifyDataSetChanged()
        contacts.addAll(listContact)
        adapter?.notifyItemInserted(0)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_READ_CONTACTS) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                getContact()
            }
        }
    }

    companion object {
        const val REQUEST_PERMISSION_READ_CONTACTS = 100
    }
}