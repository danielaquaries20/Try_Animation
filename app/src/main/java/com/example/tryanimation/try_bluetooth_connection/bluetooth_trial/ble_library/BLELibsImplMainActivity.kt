package com.example.tryanimation.try_bluetooth_connection.bluetooth_trial.ble_library

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.clj.fastble.BleManager
import com.clj.fastble.callback.BleGattCallback
import com.clj.fastble.callback.BleScanCallback
import com.clj.fastble.data.BleDevice
import com.clj.fastble.exception.BleException
import com.clj.fastble.scan.BleScanRuleConfig
import com.crocodic.core.base.activity.NoViewModelActivity
import com.crocodic.core.base.adapter.CoreListAdapter
import com.crocodic.core.extension.checkLocationPermission
import com.crocodic.core.extension.openActivity
import com.crocodic.core.extension.snacked
import com.crocodic.core.extension.tos
import com.example.tryanimation.R
import com.example.tryanimation.databinding.ActivityBluetoothConnectionBinding
import com.example.tryanimation.databinding.ItemBluetoothDetectedBinding
import com.example.tryanimation.try_bluetooth_connection.bluetooth_trial.MainBluetoothActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class BLELibsImplMainActivity :
    NoViewModelActivity<ActivityBluetoothConnectionBinding>(R.layout.activity_bluetooth_connection) {

    /*region Varible we need*/
    private lateinit var bleManager: BleManager
    private var bluetoothAdapter: BluetoothAdapter? = null

    //    private var bluetoothLeScanner: BluetoothLeScanner? = null
    private var isScanning = false

    //    private var bluetoothGatt: BluetoothGatt? = null
    private var isConnectGatt = false

    private val listBoundedDevice = ArrayList<BluetoothDevice?>()
    private val listScannedDevice = ArrayList<BluetoothDevice?>()

    @SuppressLint("MissingPermission")
    private val adapterBoundedDevices =
        CoreListAdapter<ItemBluetoothDetectedBinding, BluetoothDevice>(R.layout.item_bluetooth_detected).initItem(
            listBoundedDevice) { _, data ->
            val bleDevice = bleManager.convertBleDevice(data)
            Timber.tag(CONNECT_DEVICE).d("Device: ${data?.name}")
            Timber.tag(CONNECT_DEVICE).d("BleDevice: ${bleDevice?.device?.name}")
            connectGatt(bleDevice)
        }

    @SuppressLint("MissingPermission")
    private val adapterScannedDevices =
        CoreListAdapter<ItemBluetoothDetectedBinding, BluetoothDevice>(R.layout.item_bluetooth_detected).initItem(
            listScannedDevice) { _, data ->
            data?.createBond()
        }
    /*endregion*/

    /*region Lifecycle Activity*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initState()
    }

    override fun onStop() {
        super.onStop()
        if (::bleManager.isInitialized && isScanning) {
            bleManager.cancelScan()
            isScanning = false
        }

        if (isConnectGatt) {
            isConnectGatt = false
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if (::bleManager.isInitialized) {
            bleManager.destroy()
        }
    }
    /*endregion*/

    /*region Init Function*/
    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            binding.fabCast -> tos("Cast")
            binding.fabCheckHeart -> tos("Check Heart Rate")
            binding.fabCheckSport -> tos("Check Sport")
        }

    }

    private fun initState() {
        Timber.tag(TAG).d("INIT_STATE")
        checkLocationPermission {
            listenLocationChange()
            initView()
            initBleManager()
        }
    }

    private fun initView() {
        Timber.tag(TAG).d("INIT_VIEW")

        binding.rvBoundedDeviceList.adapter = adapterBoundedDevices
        binding.rvScannedDeviceList.adapter = adapterScannedDevices
        binding.fabCast.setOnClickListener(this)
        binding.fabCheckHeart.setOnClickListener(this)
        binding.fabCheckSport.setOnClickListener(this)

        binding.tvEmptyData.visibility = View.GONE
    }

    private fun initRefresh() {
        Timber.tag(TAG).d("INIT_REFRESH")
        binding.refreshData.setOnRefreshListener {
            getDevices()
            Handler(Looper.getMainLooper()).postDelayed({
                binding.refreshData.isRefreshing = false
            }, MainBluetoothActivity.TIME_REFRESH)

        }
    }

    private fun initBleManager() {
        Timber.tag(TAG).d("INIT_BLE_MANAGER")
        bleManager = BleManager.getInstance()
//        bleManager.init(application)

        bleManager
            .enableLog(true)
            .setReConnectCount(1, 5000)
            .setSplitWriteNum(20)
            .setConnectOverTime(10000)
            .operateTimeout = 5000

        val scanRuleConfig = BleScanRuleConfig.Builder()
            .setAutoConnect(false)
            .setScanTimeOut(MainBluetoothActivity.TIME_SCANNING)
            .build()
        bleManager.initScanRule(scanRuleConfig)

        checkBluetooth()
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
                    MainBluetoothActivity.REQUEST_PERMISSION_BLUETOOTH
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
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MainBluetoothActivity.REQUEST_PERMISSION_BLUETOOTH -> {
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
        bleManager.scan(object : BleScanCallback() {
            override fun onScanStarted(success: Boolean) {
                isScanning = true
                loadingDialog.show("Scanning nearby devcices")
            }

            override fun onScanning(bleDevice: BleDevice?) {
                Timber.tag(SCAN_DEVICE).d("Result_on_scanning: ${bleDevice?.device?.name}")
            }

            override fun onScanFinished(scanResultList: MutableList<BleDevice>?) {
                isScanning = false
                loadingDialog.dismiss()
                listScannedDevice.clear()
                adapterScannedDevices.notifyDataSetChanged()
                scanResultList?.forEach { data ->
                    Timber.tag(SCAN_DEVICE).d("Result_finish_scan: ${data.device.name}")
                    listScannedDevice.add(data.device)
                }
                adapterScannedDevices.notifyItemInserted(0)
            }
        })
    }
    /*endregion*/

    /*region Connect to Gatt*/

    @SuppressLint("MissingPermission")
    private fun connectGatt(device: BleDevice) {
        val connectGattCallback = object : BleGattCallback() {
            override fun onStartConnect() {
                Timber.tag(CONNECT_DEVICE).d("start_connect")
                loadingDialog.show("Connecting Device")
            }

            override fun onConnectFail(bleDevice: BleDevice?, exception: BleException?) {
                Timber.tag(CONNECT_DEVICE).d("failed_connect: ${bleDevice?.device?.name} caused $exception")
                loadingDialog.setResponse("Failed to Connect Device ${bleDevice?.device?.name} caused $exception")
            }

            override fun onConnectSuccess(
                bleDevice: BleDevice?,
                gatt: BluetoothGatt?,
                status: Int,
            ) {
                isConnectGatt = true
                Timber.tag(CONNECT_DEVICE).d("success_connect: ${bleDevice?.device?.name}")
                loadingDialog.dismiss()
                binding.root.snacked("Success to connect Device ${bleDevice?.device?.name}")
                val services = bleManager.getBluetoothGattServices(bleDevice)
                Timber.tag(CONNECT_DEVICE).d("LIST_SERVICE: $services")
                openActivity<BLEDetailServiceActivity> {
                    putExtra(KEY_DEVICE, bleDevice)
                }
            }

            override fun onDisConnected(
                isActiveDisConnected: Boolean,
                device: BleDevice?,
                gatt: BluetoothGatt?,
                status: Int,
            ) {
                Timber.tag(CONNECT_DEVICE).d("disconnected: ${device?.device?.name}")
            }
        }
        bleManager.connect(device, connectGattCallback)

    }

    /*endregion*/


    companion object {
        const val TAG = "BLELibsImplMainActivity"
        const val SCAN_DEVICE = "SCAN_DEVICE"
        const val CONNECT_DEVICE = "CONNECT_DEVICE"

        const val KEY_DEVICE = "device"
    }
}