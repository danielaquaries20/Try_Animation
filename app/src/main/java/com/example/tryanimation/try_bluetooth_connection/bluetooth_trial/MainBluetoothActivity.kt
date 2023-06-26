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
            binding.fabCheckHeart -> binding.root.snacked("Check Heart")
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
                }
                else -> Timber.tag("ConnectGatt").d("onServicesDiscovered_received: $status")
            }
        }

        override fun onCharacteristicRead(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int,
        ) {
            Timber.tag("ConnectGatt").d("onCharacteristicRead: $status")
        }
    }
    /*endregion*/

    companion object {
        const val REQUEST_PERMISSION_BLUETOOTH = 200
        const val TIME_REFRESH = 1000L
        const val TIME_SCANNING = 12000L
    }


}