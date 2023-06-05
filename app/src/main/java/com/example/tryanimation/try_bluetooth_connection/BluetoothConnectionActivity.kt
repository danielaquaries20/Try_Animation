package com.example.tryanimation.try_bluetooth_connection

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.crocodic.core.base.activity.NoViewModelActivity
import com.crocodic.core.base.adapter.ReactiveListAdapter
import com.crocodic.core.extension.snacked
import com.crocodic.core.extension.text
import com.example.tryanimation.R
import com.example.tryanimation.databinding.ActivityBluetoothConnectionBinding
import com.example.tryanimation.databinding.ItemBluetoothDetectedBinding
import timber.log.Timber

class BluetoothConnectionActivity :
    NoViewModelActivity<ActivityBluetoothConnectionBinding>(R.layout.activity_bluetooth_connection) {

    var bt: BluetoothAdapter? = null
    var bts: BluetoothSocket? = null
    val REQUEST_BLUETOOTH_PERMISSION: Int = 1
    val REQUEST_BLUETOOTH_ENABLE: Int = 2

    private val adapter =
        object :
            ReactiveListAdapter<ItemBluetoothDetectedBinding, String>(R.layout.item_bluetooth_detected) {
            override fun onBindViewHolder(
                holder: ItemViewHolder<ItemBluetoothDetectedBinding, String>,
                position: Int,
            ) {
                val item = getItem(position)
                holder.binding.tvBluetoothCast.text(item)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        checkBluetooth()
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            binding.fabCast -> binding.root.snacked("Casting")
            binding.fabCheckHeart -> binding.root.snacked("Check Heart")
            binding.fabCheckSport -> binding.root.snacked("Check Sport")
        }
    }

    private fun initView() {
        binding.rvBluetoothList.adapter = adapter
        notifyList()
        binding.fabCast.setOnClickListener(this)
        binding.fabCheckHeart.setOnClickListener(this)
        binding.fabCheckSport.setOnClickListener(this)
    }

    private fun checkBluetooth() {
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            informationDialog.setMessage("Bluetooth Error",
                "Device does not support Bluetooth therefore this application cannot run.",
                R.drawable.icon_note).show()
            return
        }
        bt = BluetoothAdapter.getDefaultAdapter()
        if (bt == null) {
            // This device does not have Bluetooth.
            informationDialog.setMessage("Bluetooth Error",
                "Device does not have a Bluetooth adapter therefore this application cannot run.",
                R.drawable.icon_note).show()
            return
        }

        bluetoothConnect()
    }

    private fun bluetoothConnect() {
        if (ContextCompat.checkSelfPermission(
                this, // What is this? It's not explained at https://developer.android.com/training/permissions/requesting
                Manifest.permission.BLUETOOTH
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Timber.tag("BluetoothCheck").d("Check 1")
            if (!bt!!.isEnabled) { // Error: Missing permission required by BluetoothAdapter.isEnabled: android.permission.BLUETOOTH.
                Timber.tag("BluetoothCheck").d("Check 2")
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, REQUEST_BLUETOOTH_ENABLE)
            } else {
                Timber.tag("BluetoothCheck").d("Check 3")
                val listDevice = ArrayList<String>()
                val pairedDevices: Set<BluetoothDevice>? = bt!!.bondedDevices
                pairedDevices?.forEach { device ->
                    listDevice.add(device.name)
                    val deviceName = device.name
                    val deviceHardwareAddress = device.address
                    Timber.tag("BluetoothCheck").d("DeviceName: $deviceName")
                    Timber.tag("BluetoothCheck").d("Address: $deviceHardwareAddress")
                }
                notifyList(listDevice)
            }
        } else {
            // Request permission. That will call back to onActivityResult which in the case of success will call this method again.
            // Ask for permission.
            Timber.tag("BluetoothCheck").d("Check 4")
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.BLUETOOTH),
                REQUEST_BLUETOOTH_PERMISSION
            )
        }
    }

    private fun notifyList(items: ArrayList<String>? = null) {
        if (!items.isNullOrEmpty()) {
            binding.rvBluetoothList.visibility = View.VISIBLE
            binding.tvEmptyData.visibility = View.GONE
            adapter.submitList(items)
        } else {
            binding.rvBluetoothList.visibility = View.INVISIBLE
            binding.tvEmptyData.visibility = View.VISIBLE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_BLUETOOTH_PERMISSION) {
            if (resultCode == RESULT_OK) {
                bluetoothConnect()
            } else {
                informationDialog.setMessage("Bluetooth Error",
                    "This application cannot run because it does not have Bluetooth permission.",
                    R.drawable.icon_note).show()
            }
        } else if (requestCode == REQUEST_BLUETOOTH_ENABLE) {
            if (resultCode == RESULT_OK) {
                // try again
                bluetoothConnect()
            } else {
                informationDialog.setMessage("Bluetooth Error",
                    "This application cannot run because Bluetooth is not enabled and could not be enabled.",
                    R.drawable.icon_note).show()
            }
        }
    }


    private fun dummyData() {
        val data = ArrayList<String>()
        for (i in 1..15) {
            data.add("Bluetooth $i")
        }
        adapter.submitList(data)
    }

}