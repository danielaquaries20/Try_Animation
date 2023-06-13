package com.example.tryanimation.try_bluetooth_connection

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.crocodic.core.base.activity.NoViewModelActivity
import com.crocodic.core.base.adapter.ReactiveListAdapter
import com.crocodic.core.extension.snacked
import com.crocodic.core.extension.text
import com.crocodic.core.extension.tos
import com.example.tryanimation.R
import com.example.tryanimation.databinding.ActivityBluetoothConnectionBinding
import com.example.tryanimation.databinding.ItemBluetoothDetectedBinding
import timber.log.Timber

class BluetoothConnectionActivity :
    NoViewModelActivity<ActivityBluetoothConnectionBinding>(R.layout.activity_bluetooth_connection) {

    private var bluetoothAdapter: BluetoothAdapter? = null
    private var bluetoothLeScanner: BluetoothLeScanner? = null
    private var scanning = false
    private val handler = Handler(Looper.getMainLooper())

    private val SCAN_PERIOD: Long = 10000
    private val REQUEST_BLUETOOTH_PERMISSION: Int = 1
    private val REQUEST_BLUETOOTH_ADMIN_PERMISSION: Int = 2
//    val REQUEST_BLUETOOTH_ENABLE: Int = 2

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
        registerBroadcast()

        checkBluetooth()
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            binding.fabCast -> discoveringDevice()
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

    private fun PackageManager.missingSystemFeature(name: String): Boolean = !hasSystemFeature(name)

    private fun checkBluetooth() {
        Timber.tag("TestBluetoothFlow").d("Test 1")
        packageManager.takeIf { it.missingSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE) }
            ?.also {
                tos("Device does not support Bluetooth therefore this application cannot run.")
                return
            }

        val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
        bluetoothAdapter = bluetoothManager.adapter

        Timber.tag("TestBluetoothFlow").d("Test 2")
        if (bluetoothAdapter == null) {
            Timber.tag("TestBluetoothFlow").d("Test 3")
            tos("Device does not have a Bluetooth adapter therefore this application cannot run.")
            return
        }

        bluetoothConnect()
    }

    private fun bluetoothConnect() {
        Timber.tag("TestBluetoothFlow").d("Test 4")
        if (Build.VERSION.SDK_INT >= 31) {
            Timber.tag("TestBluetoothFlow").d("Test 5")
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED
            ) {
                Timber.tag("TestBluetoothFlow").d("Test 6")
                bluetoothEnabled()
            } else {
                Timber.tag("TestBluetoothFlow").d("Test 7")
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.BLUETOOTH),
                    REQUEST_BLUETOOTH_PERMISSION
                )
            }
        } else {
            Timber.tag("TestBluetoothFlow").d("Test 8")

            bluetoothEnabled()
        }

    }

    private fun bluetoothEnabled() {
        Timber.tag("TestBluetoothFlow").d("Test 9")
        if (bluetoothAdapter?.isEnabled == false) {
            Timber.tag("TestBluetoothFlow").d("Test 10")
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            activityLauncher.launch(enableBtIntent) { result ->
                Timber.tag("TestBluetoothFlow").d("Test 11")
                if (result.resultCode == RESULT_OK) {
                    Timber.tag("TestBluetoothFlow").d("Test 12")
                    discoveringDevice()
//                        getDevices()
//                        scanLeDevice()
                } else {
                    Timber.tag("TestBluetoothFlow").d("Test 13")
                    tos("This application cannot run because Bluetooth is not enabled, please enable your bluetooth")
                    finish()
                }
            }
        } else {
            Timber.tag("TestBluetoothFlow").d("Test 14")
            discoveringDevice()
//                getDevices()
//                scanLeDevice()
        }
    }

    private fun discoveringDevice() {
        Timber.tag("TestBluetoothFlow").d("Test 15")
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED
        ) {
            Timber.tag("TestBluetoothFlow").d("Test 15")
            if (bluetoothAdapter!!.isDiscovering) {
                Timber.tag("TestBluetoothFlow").d("Test 16")
                bluetoothAdapter!!.cancelDiscovery()

                bluetoothAdapter!!.startDiscovery()
                val intentActionFound = IntentFilter(BluetoothDevice.ACTION_FOUND)
                registerReceiver(receiver, intentActionFound)
            } else {
                Timber.tag("TestBluetoothFlow").d("Test 17")
                bluetoothAdapter!!.startDiscovery()
                val intentActionFound = IntentFilter(BluetoothDevice.ACTION_FOUND)
                registerReceiver(receiver, intentActionFound)
            }
        } else {
            Timber.tag("TestBluetoothFlow").d("Test 18")
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.BLUETOOTH_ADMIN),
                REQUEST_BLUETOOTH_ADMIN_PERMISSION
            )

        }
    }

    @SuppressLint("MissingPermission")
    private fun scanLeDevice() {
        bluetoothLeScanner = bluetoothAdapter!!.bluetoothLeScanner
        if (bluetoothLeScanner == null) {
            tos("Device does not have a Bluetooth LE Scanner therefore this application cannot run.")
            finish()
            return
        }
        if (!scanning) { // Stops scanning after a pre-defined scan period.
            handler.postDelayed({
                scanning = false
                loadingDialog.dismiss()
                bluetoothLeScanner!!.stopScan(leScanCallback)
            }, SCAN_PERIOD)
            scanning = true
            loadingDialog.show("Scanning BLE")
            bluetoothLeScanner!!.startScan(leScanCallback)
        } else {
            scanning = false
            loadingDialog.dismiss()
            bluetoothLeScanner!!.stopScan(leScanCallback)
        }

    }

    // Device scan callback.
    private val leScanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            Timber.tag("ScanBluetoothLE").d("FailedScan")
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            super.onBatchScanResults(results)
            Timber.tag("ScanBluetoothLE").d("BatchResult: ${results}")
        }

        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            Timber.tag("ScanBluetoothLE").d("Result: ${result.device}")
            loadingDialog.setResponse("Result: ${result.device}",
                R.drawable.ic_baseline_cast_connected)
//            leDeviceListAdapter.addDevice(result.device)
//            leDeviceListAdapter.notifyDataSetChanged()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getDevices() {
        val listDevices = ArrayList<String>()
        bluetoothAdapter?.bondedDevices?.forEach { devices ->
            val data = "Name: ${devices.name}\nAddress: ${devices.address}"
            listDevices.add(data)
        }
        notifyList(listDevices)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_BLUETOOTH_PERMISSION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    bluetoothConnect()
                } else {
                    tos("This application cannot run because it does not have Bluetooth permission.")
                    finish()
                }
            }
            REQUEST_BLUETOOTH_ADMIN_PERMISSION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    discoveringDevice()
                } else {
                    tos("This application cannot run because it does not have Bluetooth Admin permission.")
                    finish()
                }
            }
        }

    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Timber.tag("CheckReceiver").d("onReceive")
            when (intent?.action.toString()) {
                BluetoothDevice.ACTION_FOUND -> {
                    context?.let {
                        if (ContextCompat.checkSelfPermission(context,
                                Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED
                        ) {
                            // Discovery has found a device. Get the BluetoothDevice
                            // object and its info from the Intent.
                            val device: BluetoothDevice? =
                                intent?.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                            val deviceName = device?.name
                            val deviceHardwareAddress = device?.address // MAC address
                            Timber.tag("CheckReceiver").d("DeviceName: $deviceName")
                            Timber.tag("CheckReceiver").d("DeviceAddress: $deviceHardwareAddress")
                        }
                    }
                }
                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                    Timber.tag("CheckReceiver").d("ACTION_DISCOVERY_STARTED")
                }
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    Timber.tag("CheckReceiver").d("ACTION_DISCOVERY_FINISHED")
                }
                else -> {
                    Timber.tag("CheckReceiver").d("Else Branch")
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    private fun registerBroadcast() {
        // Register for broadcasts when a device is discovered.
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND).apply {
            addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
            addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        }
        registerReceiver(receiver, filter)
    }

    /*private fun dummyData() {
        val data = ArrayList<String>()
        for (i in 1..15) {
            data.add("Bluetooth $i")
        }
        adapter.submitList(data)
    }*/


    /*region TRIAL 1*/
    /*var bt: BluetoothAdapter? = null
    var bts: BluetoothSocket? = null*/

    /*private fun checkBluetooth() {
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
    }*/

    /*private fun bluetoothConnect() {
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
    }*/

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_BLUETOOTH_PERMISSION) {
            if (resultCode == RESULT_OK) {
                bluetoothConnect()
            } else {
                informationDialog.setMessage("Bluetooth Error",
                    "This application cannot run because it does not have Bluetooth permission.",
                    R.drawable.icon_note).show()
            }
        }
        else if (requestCode == REQUEST_BLUETOOTH_ENABLE) {
            if (resultCode == RESULT_OK) {
                // try again
                bluetoothConnect()
            } else {
                informationDialog.setMessage("Bluetooth Error",
                    "This application cannot run because Bluetooth is not enabled and could not be enabled.",
                    R.drawable.icon_note).show()
            }
        }
    }*/
    /*endregion*/


}