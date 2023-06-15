package com.example.tryanimation.try_bluetooth_connection

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.bluetooth.le.BluetoothLeScanner
import android.companion.AssociationRequest
import android.companion.BluetoothDeviceFilter
import android.companion.CompanionDeviceManager
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.crocodic.core.base.activity.NoViewModelActivity
import com.crocodic.core.base.adapter.ReactiveListAdapter
import com.crocodic.core.extension.checkLocationPermission
import com.crocodic.core.extension.snacked
import com.crocodic.core.extension.text
import com.crocodic.core.extension.tos
import com.example.tryanimation.R
import com.example.tryanimation.databinding.ActivityBluetoothConnectionBinding
import com.example.tryanimation.databinding.ItemBluetoothDetectedBinding
import timber.log.Timber
import java.io.IOException
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class BluetoothConnectionActivity :
    NoViewModelActivity<ActivityBluetoothConnectionBinding>(R.layout.activity_bluetooth_connection) {

    private val MyUuid: UUID? = UUID.fromString("074490f8-4fbc-4c9d-810c-1d59da5317f9")

    private val listBoundedDevice = ArrayList<String>()
    private val listScannedDevice = ArrayList<String>()

    private var bluetoothAdapter: BluetoothAdapter? = null
    private var bluetoothLeScanner: BluetoothLeScanner? = null
    private var scanning = false
    private val handler = Handler(Looper.getMainLooper())
    private var isHaveReceiver = false

    private val SCAN_PERIOD: Long = 20000
    private val REQUEST_BLUETOOTH_PERMISSION: Int = 1
    private val REQUEST_BLUETOOTH_ADMIN_PERMISSION: Int = 2
    private val SELECT_DEVICE_REQUEST_CODE: Int = 3
//    val REQUEST_BLUETOOTH_ENABLE: Int = 2

    private val adapterBoundedDevices =
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

    private val adapterScannedDevices =
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


    private val deviceManager: CompanionDeviceManager by lazy(LazyThreadSafetyMode.NONE) {
        getSystemService(CompanionDeviceManager::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkLocationPermission {
            listenLocationChange()
            initView()
            checkBluetooth()
        }
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
        binding.rvBoundedDeviceList.adapter = adapterBoundedDevices
        binding.rvScannedDeviceList.adapter = adapterScannedDevices
        notifyList()
        binding.fabCast.setOnClickListener(this)
        binding.fabCheckHeart.setOnClickListener(this)
        binding.fabCheckSport.setOnClickListener(this)
    }

    private fun notifyList() {
        if (listBoundedDevice.isNullOrEmpty() && listScannedDevice.isNullOrEmpty()) {
            binding.tvEmptyData.visibility = View.VISIBLE
            binding.linearBoundedDevices.visibility = View.GONE
            binding.linearScannedDevices.visibility = View.GONE
        } else {
            binding.tvEmptyData.visibility = View.GONE
            if (!listBoundedDevice.isNullOrEmpty()) {
                binding.linearBoundedDevices.visibility = View.VISIBLE
                adapterBoundedDevices.submitList(listBoundedDevice)
            } else {
                binding.linearBoundedDevices.visibility = View.GONE
            }

            if (!listScannedDevice.isNullOrEmpty()) {
                binding.linearScannedDevices.visibility = View.VISIBLE
                adapterScannedDevices.submitList(listScannedDevice)
            } else {
                binding.linearScannedDevices.visibility = View.GONE
            }
        }

    }

    private fun registerBroadcast() {
        // Register for broadcasts when a device is discovered.
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND).apply {
            addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
            addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        }
        registerReceiver(receiver, filter)
        isHaveReceiver = true
    }

    private fun PackageManager.missingSystemFeature(name: String): Boolean = !hasSystemFeature(name)

    private fun checkBluetooth() {
        packageManager.takeIf { it.missingSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE) }
            ?.also {
                tos("Device does not support Bluetooth therefore this application cannot run.")
                return
            }

        val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
        bluetoothAdapter = bluetoothManager.adapter

        if (bluetoothAdapter == null) {
            tos("Device does not have a Bluetooth adapter therefore this application cannot run.")
            return
        }

        bluetoothConnect()
    }

    private fun bluetoothConnect() {
        if (Build.VERSION.SDK_INT >= 31) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED
            ) {
                bluetoothEnabled()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.BLUETOOTH),
                    REQUEST_BLUETOOTH_PERMISSION
                )
            }
        } else {
            registerBroadcast()
            bluetoothEnabled()
//            connectingSupportDevice()
        }

    }

    private fun bluetoothEnabled() {
        if (bluetoothAdapter?.isEnabled == false) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            activityLauncher.launch(enableBtIntent) { result ->
                if (result.resultCode == RESULT_OK) {
                    discoveringDevice()
                    getDevices()
//                        scanLeDevice()
                } else {
                    tos("This application cannot run because Bluetooth is not enabled, please enable your bluetooth")
                    finish()
                }
            }
        } else {
            discoveringDevice()
            getDevices()
//                scanLeDevice()
        }
    }

    private fun discoveringDevice() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED
        ) {
            loadingDialog.show("Discovering Devices")
            if (bluetoothAdapter!!.isDiscovering) {
                Timber.tag("TestBluetoothFlow").d("Test 17")
                bluetoothAdapter!!.cancelDiscovery()

//                bluetoothAdapter!!.startDiscovery()
//                val intentActionFound = IntentFilter(BluetoothDevice.ACTION_FOUND)
//                registerReceiver(receiver, intentActionFound)
//                isHaveReceiver = true
            } else {
                bluetoothAdapter!!.startDiscovery()
//                val intentActionFound = IntentFilter(BluetoothDevice.ACTION_FOUND)
//                registerReceiver(receiver, intentActionFound)
//                isHaveReceiver = true
            }
            handler.postDelayed({
                bluetoothAdapter!!.cancelDiscovery()
                loadingDialog.dismiss()
            }, SCAN_PERIOD)
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.BLUETOOTH_ADMIN),
                REQUEST_BLUETOOTH_ADMIN_PERMISSION
            )

        }
    }

    @SuppressLint("MissingPermission")
    private fun getDevices() {
        bluetoothAdapter?.bondedDevices?.forEach { devices ->
            val data = "Name: ${devices.name}\nAddress: ${devices.address}"
            listBoundedDevice.add(data)
        }
        notifyList()
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action.toString()) {
                BluetoothDevice.ACTION_FOUND -> {
                    context?.let {
                        if (ContextCompat.checkSelfPermission(context,
                                Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED
                        ) {
                            bluetoothAdapter!!.cancelDiscovery()
                            loadingDialog.dismiss()
                            val device: BluetoothDevice? =
                                intent?.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                            val deviceName = device?.name
                            val deviceHardwareAddress = device?.address // MAC address
                            listScannedDevice.add("Name: $deviceName\nAddress: $deviceHardwareAddress")
                            notifyList()
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
                    binding.root.snacked("Else Branch: ${intent?.action}")
                }
            }
        }

    }

    private fun connectingSupportDevice() {
        val deviceFilter: BluetoothDeviceFilter = BluetoothDeviceFilter.Builder().build()
        val pairingRequest: AssociationRequest = AssociationRequest.Builder()
            .addDeviceFilter(deviceFilter)
            .setSingleDevice(true)
            .build()

        deviceManager.associate(pairingRequest,
            object : CompanionDeviceManager.Callback() {
                override fun onDeviceFound(chooseLauncher: IntentSender?) {
                    startIntentSenderForResult(chooseLauncher,
                        SELECT_DEVICE_REQUEST_CODE,
                        null,
                        0,
                        0,
                        0)
                }

                override fun onFailure(error: CharSequence?) {
                    Timber.tag("CompanionDeviceFailure").e("Error: $error")
                }
            }, null)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isHaveReceiver) unregisterReceiver(receiver)
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SELECT_DEVICE_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val deviceToPair: BluetoothDevice? =
                        data?.getParcelableExtra(CompanionDeviceManager.EXTRA_DEVICE)
                    if (ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED
                    ) {
                        Timber.tag("ActivityResult").d("BLUETOOTH_CONNECT is not Granted")
                        return
                    }
                    Timber.tag("ActivityResult").d("BLUETOOTH_CONNECT Granted: $deviceToPair")
                    deviceToPair?.createBond()
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private inner class ConnectThread(device: BluetoothDevice, context: Context) : Thread() {

        private val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
            if (ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
            ) {
                device.createRfcommSocketToServiceRecord(MyUuid)
            } else {
                null
            }
        }

        override fun run() {
            // Cancel discovery because it otherwise slows down the connection.
            bluetoothAdapter?.cancelDiscovery()

            mmSocket?.let { socket ->
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                if (bluetoothAdapter?.isDiscovering == true) {
                    bluetoothAdapter?.cancelDiscovery()
                }
                socket.connect()

                // The connection attempt succeeded. Perform work associated with
                // the connection in a separate thread.
//                manageMyConnectedSocket(socket)
                handler.postDelayed({
                    Timber.tag("ConnectThread").d("StatusConnected: ${socket.isConnected}")
                }, SCAN_PERIOD)
            }
        }

        fun cancel() {
            try {
                mmSocket?.close()
            } catch (e: IOException) {
                Timber.tag("ConnectThread").e("Error to Connect Device: ${e.message}")
            }
        }
    }


    /*region Scanning BLE*/
    /*@SuppressLint("MissingPermission")
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

    }*/

    // Device scan callback.
    /*private val leScanCallback: ScanCallback = object : ScanCallback() {
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
    }*/
    /*endregion*/


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