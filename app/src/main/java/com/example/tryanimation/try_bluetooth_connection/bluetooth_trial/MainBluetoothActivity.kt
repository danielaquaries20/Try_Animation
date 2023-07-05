package com.example.tryanimation.try_bluetooth_connection.bluetooth_trial

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.*
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.crocodic.core.base.activity.CoreActivity
import com.crocodic.core.base.adapter.CoreListAdapter
import com.crocodic.core.extension.checkLocationPermission
import com.crocodic.core.extension.snacked
import com.crocodic.core.extension.tos
import com.example.tryanimation.R
import com.example.tryanimation.databinding.ActivityBluetoothConnectionBinding
import com.example.tryanimation.databinding.ItemBluetoothDetectedBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class MainBluetoothActivity :
    CoreActivity<ActivityBluetoothConnectionBinding, MainBluetoothViewModel>(R.layout.activity_bluetooth_connection) {

    /*region Varible we need*/
    private var bluetoothAdapter: BluetoothAdapter? = null

    private var bluetoothLeScanner: BluetoothLeScanner? = null
    private var scanning = false

    private var bluetoothGatt: BluetoothGatt? = null
    private var isConnectGatt = false

    private val listBoundedDevice = ArrayList<BluetoothDevice?>()
    private val listScannedDevice = ArrayList<BluetoothDevice?>()

    @SuppressLint("MissingPermission")
    private val adapterBoundedDevices =
        CoreListAdapter<ItemBluetoothDetectedBinding, BluetoothDevice>(R.layout.item_bluetooth_detected).initItem(
            listBoundedDevice) { _, data ->
            data?.let { connectGatt(it) }
        }

    @SuppressLint("MissingPermission")
    private val adapterScannedDevices =
        CoreListAdapter<ItemBluetoothDetectedBinding, BluetoothDevice>(R.layout.item_bluetooth_detected).initItem(
            listScannedDevice) { _, data ->
            data?.createBond()
        }

    /*endregion*/

    /*region Activtiy Lifecycles*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    
        initState()

    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            binding.fabCast -> binding.root.snacked("Casting")
            binding.fabCheckHeart -> readStaticChar()
            binding.fabCheckSport -> binding.root.snacked("Check Sport")
        }
    }

    @SuppressLint("MissingPermission")
    override fun onStop() {
        super.onStop()
        if (scanning) {
            bluetoothLeScanner?.stopScan(leScanCallback)
            scanning = false
        }

        if (isConnectGatt) {
            closeGatt()
            isConnectGatt = false
        }
    }
    /*endregion*/

    /*region Init*/
    private fun initState() {
        checkLocationPermission {
            listenLocationChange()
            initView()
            checkBluetooth()
        }
    }

    private fun initRefresh() {
        binding.refreshData.setOnRefreshListener {
            getDevices()
            Handler(Looper.getMainLooper()).postDelayed({
                binding.refreshData.isRefreshing = false
            }, TIME_REFRESH)

        }
    }

    private fun initView() {
        binding.rvBoundedDeviceList.adapter = adapterBoundedDevices
        binding.rvScannedDeviceList.adapter = adapterScannedDevices
        binding.fabCast.setOnClickListener(this)
        binding.fabCheckHeart.setOnClickListener(this)
        binding.fabCheckSport.setOnClickListener(this)

        binding.tvEmptyData.visibility = View.GONE
    }
    /*endregion*/

    /*region Check and Enable Bluetooth*/
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
                    REQUEST_PERMISSION_BLUETOOTH
                )
            }
        } else {
            bluetoothEnabled()
        }
    }

    private fun bluetoothEnabled() {
        if (bluetoothAdapter?.isEnabled == false) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            activityLauncher.launch(enableBtIntent) { result ->
                if (result.resultCode == RESULT_OK) {
                    getDevices()
                    scanLeDevice()
                } else {
                    tos("This application cannot run because Bluetooth is not enabled, please enable your bluetooth")
                    finish()
                }
            }
        } else {
            getDevices()
            scanLeDevice()
        }

        initRefresh()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_PERMISSION_BLUETOOTH -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    bluetoothEnabled()
                } else {
                    tos("This application cannot run because it does not have Bluetooth permission.")
                    finish()
                }
            }
        }
    }
    /*endregion*/

    /*region Get paired and scanned Device*/
    @SuppressLint("MissingPermission")
    private fun getDevices() {
        listBoundedDevice.clear()
        adapterBoundedDevices.notifyDataSetChanged()
        bluetoothAdapter?.bondedDevices?.forEach { devices ->
            val data = "Name: ${devices.name}\nAddress: ${devices.address}"
            Timber.d("Bounded_GetDevice: $data")
            listBoundedDevice.add(devices)
        }
        adapterBoundedDevices.notifyItemInserted(0)
    }

    @SuppressLint("MissingPermission")
    private fun scanLeDevice() {
        bluetoothLeScanner = bluetoothAdapter?.bluetoothLeScanner
        if (bluetoothLeScanner != null) {
            listScannedDevice.clear()
            scanning = true
            binding.refreshData.isRefreshing = true
            loadingDialog.show("Scan nearby devices")
            bluetoothLeScanner?.startScan(leScanCallback)
            Handler(mainLooper).postDelayed({
                bluetoothLeScanner?.stopScan(leScanCallback)
                scanning = false
                binding.refreshData.isRefreshing = false
                loadingDialog.dismiss()
            }, TIME_SCANNING)
        } else {
            tos("Failed to Scan nearby devices")
        }
        binding.refreshData.isRefreshing = false
    }

    private val leScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            Timber.tag("ScanLeDevices").d("Result: ${result.device}")
            if (!listScannedDevice.contains(result.device)) {
                listScannedDevice.add(result.device)
                adapterScannedDevices.notifyItemInserted(listScannedDevice.size - 1)
                Timber.tag("ScanLeDevices").d("SizeList: ${listScannedDevice.size}")
            }
        }

    }
    /*endregion*/

    /*region Connect to Gatt*/
    private fun connectGatt(device: BluetoothDevice) {
        Timber.tag("ConnectGatt").d("Test 1")
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        Timber.tag("ConnectGatt").d("Test 2")

        isConnectGatt = true
        bluetoothGatt = device.connectGatt(this, true, bluetoothGattCallback)

    }

    private fun closeGatt() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        bluetoothGatt?.close()
        bluetoothGatt = null
    }

    private val bluetoothGattCallback = object : BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> {
                    bluetoothGatt?.discoverServices()
                    binding.root.snacked("Gatt Connected", null, Snackbar.LENGTH_SHORT)
                    Timber.tag("ConnectGatt").d("STATE_CONNECTED")
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    Timber.tag("ConnectGatt").d("STATE_DISCONNECTED")
                }
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            when (status) {
                BluetoothGatt.GATT_SUCCESS -> {
                    binding.root.snacked("Service Discovered: ${gatt?.services}")
                    Timber.tag("ConnectGatt").d("GATT_SUCCESS: $status")
                    Timber.tag("ConnectGatt").d("GATT: ${gatt?.services}")
                    displayGattService(gatt?.services)
                }
                else -> Timber.tag("ConnectGatt").d("onServicesDiscovered_received: $status")
            }
        }

        override fun onCharacteristicRead(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int,
        ) {
            Timber.tag("CharacteristicGatt").d("onCharacteristicRead: $status")
            val data: ByteArray? = characteristic?.value
            if (data?.isNotEmpty() == true) {
                val hexString: String = data.joinToString(separator = " ") {
                    String.format("%02X", it)
                }
                Timber.tag("CharacteristicGatt").d("CHAR_VALUE: $data and $hexString")
                binding.root.snacked("CHAR_VALUE: $data and $hexString")
            }
        }

    }
    /*endregion*/

    /*region Read Characteristic*/
    private lateinit var mGattCharacteristics: MutableList<BluetoothGattCharacteristic>

    private fun displayGattService(gattServices: List<BluetoothGattService>?) {
        if (gattServices == null || gattServices.isEmpty()) {
            binding.root.snacked("There is no any Service")
            return
        }

        var uuid: String?
        val gattServiceData: MutableList<HashMap<String, String>> = mutableListOf()
        val gattCharacteristicData: MutableList<ArrayList<HashMap<String, String>>> = mutableListOf()
        mGattCharacteristics = mutableListOf()

        gattServices.forEach { gattService ->
            val currentServiceData = HashMap<String, String>()
            uuid = gattService.uuid.toString()
            currentServiceData[LIST_NAME] = gattService.toString()
            currentServiceData[LIST_UUID] = uuid!!
            gattServiceData += currentServiceData

            val gattCharacteristicGroupData: ArrayList<HashMap<String, String>> = arrayListOf()
            val gattCharacteristics = gattService.characteristics
            val charas: MutableList<BluetoothGattCharacteristic> = mutableListOf()

            gattCharacteristics.forEach { gattCharacteristic ->
                charas += gattCharacteristic
                val currentCharaData: HashMap<String, String> = hashMapOf()
                uuid = gattCharacteristic.uuid.toString()
                currentCharaData[LIST_NAME] = gattCharacteristic.toString()
                currentCharaData[LIST_UUID] = uuid!!
                gattCharacteristicGroupData += currentCharaData
            }

            mGattCharacteristics += charas
            gattCharacteristicData += gattCharacteristicGroupData
        }

        Timber.tag("displayGattService").d("GATT_CHARACTERISTICS: $mGattCharacteristics")
        Timber.tag("displayGattService").d("GATT_CHARACTERISTICS_DATA: $gattCharacteristicData")


    }

    @SuppressLint("MissingPermission")
    fun readCharacteristic(characteristic: BluetoothGattCharacteristic) {
        bluetoothGatt?.readCharacteristic(characteristic)
    }

    private fun readStaticChar() {
        if(::mGattCharacteristics.isInitialized) {
            Timber.tag("ReadCharacteristic").d("CHARS_LAST_INDEX: ${mGattCharacteristics.lastIndex}")
            Timber.tag("ReadCharacteristic").d("CHARS: $mGattCharacteristics")
            if (mGattCharacteristics.isEmpty()) {
                binding.root.snacked("No Characteristic available")
            } else {
                val characteristic = mGattCharacteristics[0]
                Timber.tag("ReadCharacteristic").d("CHAR: $characteristic")
                readCharacteristic(characteristic)
            }
        } else {
            binding.root.snacked("No Characteristic available")
        }
        //Last Index: 25
        //Can't read: Index -> [3, 14, 15, 18, 19, 21, 22, 23, 24, 25]

        //Read: Index 0 -> {CHAR_VALUE: [B@ee33ab8 and 4D 36}
        //Read: Index 1 -> {CHAR_VALUE: [B@353e07b and 00 00}
        //Read: Index 2 -> {CHAR_VALUE: [B@d9c8e65 and 0F 00 0F 00 00 00 E8 03}
        //Read: Index 4 -> {CHAR_VALUE: [B@1617617 and 03 00 00 00 B4 A5 FF 1F} -> Comparing
        //Read: Index 4 -> {CHAR_VALUE: [B@3ca2d7 and 03 00 00 00 B4 A5 FF 1F} -> Comparing
        //Read: Index 5 -> {CHAR_VALUE: [B@bbd3b7a and 4D 6F 64 65 6C 20 4E 75 6D 62 65 72}
        //Read: Index 6 -> {CHAR_VALUE: [B@aa41194 and 53 65 72 69 61 6C 20 4E 75 6D 62 65 72}
        //Read: Index 7 -> {CHAR_VALUE: [B@3a76063 and 46 69 72 6D 77 61 72 65 20 52 65 76 69 73 69 6F 6E}
        //Read: Index 8 -> {CHAR_VALUE: [B@c8e1f39 and 48 61 72 64 77 61 72 65 20 52 65 76 69 73 69 6F 6E}
        //Read: Index 9 -> {CHAR_VALUE: [B@4de67b5 and 53 6F 66 74 77 61 72 65 20 52 65 76 69 73 69 6F 6E}
        //Read: Index 10 -> {CHAR_VALUE: [B@32fd503 and 4D 61 6E 75 66 61 63 74 75 72 65 72 20 4E 61 6D 65}
        //Read: Index 11 -> {CHAR_VALUE: [B@3e0d193 and FE 00 65 78 70 65 72 69 6D 65 6E 74 61 6C}
        //Read: Index 12 -> {CHAR_VALUE: [B@871e786 and 01 04 05 00 00 10 01}
        //Read: Index 13 -> {CHAR_VALUE: [B@a7323da and 5F}
        //Read: Index 16 -> {CHAR_VALUE: [B@528615e and AC 04 09 90 75 0D 00 01 01 31 97 00 00 00 00 00 00 00 00 00}
        //Read: Index 17 -> {CHAR_VALUE: [B@5d48113 and 5A 14 12 00 17 07 03 00 00 08 2A 00 00 05 10 00 00 B6 40 96}
        //Read: Index 20 -> {CHAR_VALUE: [B@ab425f3 and 01}

    }
    /*endregion*/

    companion object {
        const val REQUEST_PERMISSION_BLUETOOTH = 200
        const val TIME_REFRESH = 1000L
        const val TIME_SCANNING = 12000L
        const val LIST_NAME = "name"
        const val LIST_UUID = "uuid"
    }


}