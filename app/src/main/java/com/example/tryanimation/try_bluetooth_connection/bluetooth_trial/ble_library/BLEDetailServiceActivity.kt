package com.example.tryanimation.try_bluetooth_connection.bluetooth_trial.ble_library

import android.Manifest
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.clj.fastble.BleManager
import com.clj.fastble.callback.BleGattCallback
import com.clj.fastble.callback.BleIndicateCallback
import com.clj.fastble.callback.BleReadCallback
import com.clj.fastble.data.BleDevice
import com.clj.fastble.exception.BleException
import com.crocodic.core.base.activity.NoViewModelActivity
import com.crocodic.core.extension.snacked
import com.crocodic.core.extension.tos
import com.example.tryanimation.R
import com.example.tryanimation.databinding.ActivityBledetailServiceBinding
import timber.log.Timber

class BLEDetailServiceActivity :
    NoViewModelActivity<ActivityBledetailServiceBinding>(R.layout.activity_bledetail_service) {

    private lateinit var expandableListAdapter: AdapterELVServiceCharacteristic

    private var bleDevice: BleDevice? = null

    private val listService = ArrayList<BluetoothGattService>()
    private var mapCharacteristic: MutableMap<BluetoothGattService, List<BluetoothGattCharacteristic>> =
        mutableMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bleDevice = intent.getParcelableExtra(BLELibsImplMainActivity.KEY_DEVICE)

        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        BleManager.getInstance().destroy()
    }

    private fun initView() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED
        ) {
            tos("No Permission Bluetooth Admin")
            finish()
        }
        binding.tvTitle.text = "Device: ${bleDevice?.device?.name}"
        expandableListAdapter = AdapterELVServiceCharacteristic(listService,
            mapCharacteristic) { service, characteristic ->
            Timber.tag(TAG)
                .d("SERVICE: $service, CHARACTERISTIC: $characteristic")
            readCharacteristic(service, characteristic!!)
        }
        binding.elvServiceCharacteristic.setAdapter(expandableListAdapter)

        getService()
    }

    private fun getService() {
        val services = BleManager.getInstance().getBluetoothGattServices(bleDevice)
        Timber.tag(TAG).d("SERVICES: $services")
        listService.addAll(services)
        getCharacteristic()
    }

    private fun getCharacteristic() {
        Timber.tag(TAG).d("LIST_SERVICE: $listService")
        listService.forEach { bluetoothGattService ->
            val characteristics =
                BleManager.getInstance().getBluetoothGattCharacteristics(bluetoothGattService)
            mapCharacteristic[bluetoothGattService] = characteristics
            Timber.tag(TAG).d("CHARACTERISTICS: $characteristics")
        }
        Timber.tag(TAG).d("MAP_CHARACTERISTIC: $mapCharacteristic")
    }

    private fun indicate(
        service: BluetoothGattService,
        characteristic: BluetoothGattCharacteristic,
    ) {
        val indicateCallback = object : BleIndicateCallback() {
            override fun onIndicateSuccess() {
                Timber.tag(TAG).d("onIndicateSuccess")
            }

            override fun onIndicateFailure(exception: BleException?) {
                Timber.tag(TAG).d("onIndicateFailure: $exception")
            }

            override fun onCharacteristicChanged(data: ByteArray?) {
                Timber.tag(TAG).d("onCharacteristicChanged: $data")
            }
        }
        BleManager.getInstance().indicate(bleDevice, service.uuid.toString(),
            characteristic.uuid.toString(), indicateCallback)

    }

    private fun readCharacteristic(
        service: BluetoothGattService,
        characteristic: BluetoothGattCharacteristic,
    ) {
        val readCallback = object : BleReadCallback() {
            override fun onReadSuccess(data: ByteArray?) {
                loadingDialog.dismiss()
                val flag = characteristic.properties
                val format = when (flag and 0x01) {
                    0x01 -> {
                        Timber.tag(TAG).d("Heart rate format UINT16.")
                        BluetoothGattCharacteristic.FORMAT_UINT16
                    }
                    else -> {
                        Timber.tag(TAG).d("Heart rate format UINT8.")
                        BluetoothGattCharacteristic.FORMAT_UINT8
                    }
                }
                val heartRate = characteristic.getIntValue(format, 1)
                Timber.tag(TAG).d("Received heart rate: %d", heartRate)

                binding.root.snacked("VALUE: $heartRate")

                /*if (data?.isNotEmpty() == true) {
                    val hexString: String = data.joinToString(separator = " ") {
                        String.format("%02X", it)
                    }
                Timber.tag("CharacteristicGatt").d("CHAR_VALUE: $data and $hexString")
                    binding.root.snacked("CHAR_VALUE: $data and $hexString")

                    val nilai1 = data.toString(Charsets.UTF_8)
                    val nilai2 = data.toString(Charsets.UTF_16)
                    val nilai3 = data.toString(Charsets.UTF_32)
                    val nilai4 = data.toString(Charsets.UTF_16BE)
                    val nilai5 = data.toString(Charsets.UTF_16LE)
                    val nilai6 = data.toString(Charsets.UTF_32BE)
                    val nilai7 = data.toString(Charsets.UTF_32LE)
                    val nilai8 = data.toString(Charsets.ISO_8859_1)
                    val nilai9 = data.toString(Charsets.US_ASCII)
                    Timber.tag(TAG)
                        .d("Nilai1: $nilai1, Nilai2: $nilai2, Nilai3: $nilai3, Nilai4: $nilai4, Nilai5: $nilai5, Nilai6: $nilai6, Nilai7: $nilai7, Nilai8: $nilai8, Nilai9: $nilai9")
                }*/
            }

            override fun onReadFailure(exception: BleException?) {
                if (exception?.description?.contains("This device is not connected") == true ||
                    exception?.description?.contains("gatt readCharacteristic fail") == true
                ) {
                    loadingDialog.setResponse("Error to Read: $exception").onDismiss { finish() }
                } else {
                    loadingDialog.setResponse("Error to Read: $exception")
                }
            }
        }

        loadingDialog.show("Read $characteristic")
        BleManager.getInstance().read(bleDevice, service.uuid.toString(),
            characteristic.uuid.toString(), readCallback)

    }

    private fun reconnect() {
        loadingDialog.dismiss()
        val connectBleCallback = object : BleGattCallback() {
            override fun onStartConnect() {
                Timber.tag(TAG).d("onStartConnect")
                loadingDialog.show("Reconnecting...")
            }

            override fun onConnectFail(device: BleDevice?, exception: BleException?) {
                loadingDialog.dismiss()
                Timber.tag(TAG).d("onConnectFail: $exception")
            }

            override fun onConnectSuccess(device: BleDevice?, gatt: BluetoothGatt?, status: Int) {
                loadingDialog.dismiss()
                Timber.tag(TAG).d("onConnectSuccess: $device")
            }

            override fun onDisConnected(
                isActiveDisConnected: Boolean,
                device: BleDevice?,
                gatt: BluetoothGatt?,
                status: Int,
            ) {
                loadingDialog.dismiss()
                Timber.tag(TAG).d("onDisConnected: $device")
            }
        }

        BleManager.getInstance().connect(bleDevice, connectBleCallback)

    }

    companion object {
        const val TAG = "BLEDetailServiceActivity_TAG"
    }
}