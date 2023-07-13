package com.example.tryanimation.try_bluetooth_connection.bluetooth_trial.ble_library

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
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
import java.util.*
import kotlin.collections.ArrayList

class BLEDetailServiceActivity :
    NoViewModelActivity<ActivityBledetailServiceBinding>(R.layout.activity_bledetail_service) {

    private lateinit var expandableListAdapter: AdapterELVServiceCharacteristic

    private var bleDevice: BleDevice? = null

    private var isReconnect = true

    private val listService = ArrayList<BluetoothGattService>()
    private var mapCharacteristic: MutableMap<BluetoothGattService, List<BluetoothGattCharacteristic>> =
        mutableMapOf()

    private val listDescriptor = ArrayList<BluetoothGattDescriptor>()

    private var bluetoothGatt: BluetoothGatt? = null
    private var isConnectGatt = false
    private var isRead = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bleDevice = intent.getParcelableExtra(BLELibsImplMainActivity.KEY_DEVICE)

        initView()
    }

    override fun onResume() {
        super.onResume()
        isReconnect = true
    }

    override fun onStop() {
        super.onStop()

        if (isConnectGatt) {
            closeGatt()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isReconnect = false
        BleManager.getInstance().disconnect(bleDevice)
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

//        connectGatt()
        connectGattManual()
    }

    private fun initExpandableList() {
        expandableListAdapter = AdapterELVServiceCharacteristic(listService,
            mapCharacteristic) { service, characteristic ->
            Timber.tag(TAG)
                .d("SERVICE: $service, CHARACTERISTIC: $characteristic")
//            readCharacteristic(service, characteristic!!)
            readCharacteristicManual(characteristic!!)
        }
        binding.elvServiceCharacteristic.setAdapter(expandableListAdapter)
    }

    /*region BLE Manual*/

    private val bluetoothGattCallback = object : BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> {
                    bluetoothGatt?.discoverServices()
//                    binding.root.snacked("Gatt Connected", null, Snackbar.LENGTH_SHORT)
                    Timber.tag(TAG).d("STATE_CONNECTED")
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    Timber.tag(TAG).d("STATE_DISCONNECTED")
                }
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            when (status) {
                BluetoothGatt.GATT_SUCCESS -> {
//                    binding.root.snacked("Service Discovered: ${gatt?.services}")
                    Timber.tag(TAG).d("GATT_SUCCESS: $status")
                    Timber.tag(TAG).d("GATT: ${gatt?.services}")
                    displayGattService(gatt?.services)
                }
                else -> Timber.tag(TAG).d("onServicesDiscovered_received: $status")
            }
        }

        override fun onCharacteristicRead(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int,
        ) {
            Timber.tag("CharacteristicGatt").d("CHARACTERISTIC: $characteristic")
            isRead = true
            loadingDialog.dismiss()
            val data: ByteArray? = characteristic?.value
            if (data?.isNotEmpty() == true) {
                val hexString: String = data.joinToString(separator = " ") {
                    String.format("%02X", it)
                }
                Timber.tag(TAG_CHARACTERISTIC_READ).d("CHAR_VALUE: $data and $hexString")
                Timber.tag(TAG_CHARACTERISTIC_READ).d("CHAR_UUID: ${characteristic.uuid}")
                binding.root.snacked("CHAR_VALUE: $data and $hexString")
            }

            /*if (characteristic != null) {
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
            }*/
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
        ) {
//            super.onCharacteristicChanged(gatt, characteristic)
            Timber.tag("CharacteristicGattChanged").d("CharacteristicChanged")
            val data: ByteArray? = characteristic?.value
            if (data?.isNotEmpty() == true) {
                val hexString: String = data.joinToString(separator = " ") {
                    String.format("%02X", it)
                }
                Timber.tag("CharacteristicGattChanged").d("CHAR_VALUE: $data and $hexString")
                binding.root.snacked("CHAR_VALUE: $data and $hexString")
            }
        }

        override fun onDescriptorRead(
            gatt: BluetoothGatt?,
            descriptor: BluetoothGattDescriptor?,
            status: Int,
        ) {
            super.onDescriptorRead(gatt, descriptor, status)
            Timber.tag(TAG).d("DESCRIPTOR: ${descriptor?.value}")
//            binding.root.snacked("DESCRIPTOR: ${descriptor?.value}")

            val data: ByteArray? = descriptor?.value
            if (data?.isNotEmpty() == true) {
                val hexString: String = data.joinToString(separator = " ") {
                    String.format("%02X", it)
                }
                Timber.tag(TAG).d("DESC_VAL: $data and $hexString")
                binding.root.snacked("DESCRIPTOR: $data and $hexString")

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
                    .d("DESC_VAL = (Nilai1: $nilai1, Nilai2: $nilai2, Nilai3: $nilai3, Nilai4: $nilai4, Nilai5: $nilai5, Nilai6: $nilai6, Nilai7: $nilai7, Nilai8: $nilai8, Nilai9: $nilai9)")
            }
        }

    }

    private fun connectGattManual() {
        Timber.tag("ConnectGatt").d("Test 1")
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED
        ) {
            tos("Permission BLUETOOTH_ADMIN is not granted")
            finish()
        }
        Timber.tag("ConnectGatt").d("Test 2")

        bleDevice?.let { dev ->
            isConnectGatt = true
            bluetoothGatt = dev.device.connectGatt(this, true, bluetoothGattCallback)
        }

    }

    private fun closeGatt() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        bluetoothGatt?.close()
        bluetoothGatt = null
        isConnectGatt = false
    }

    private fun displayGattService(gattServices: List<BluetoothGattService>?) {
        if (gattServices == null || gattServices.isEmpty()) {
            binding.root.snacked("There is no any Service")
            return
        }

        Timber.tag(TAG).d("SERVICES: $gattServices")
        listService.addAll(gattServices)

        val listCharacteristic = ArrayList<BluetoothGattCharacteristic>()

        gattServices.forEach { bluetoothGattService ->
            val characteristics =
                BleManager.getInstance().getBluetoothGattCharacteristics(bluetoothGattService)
            mapCharacteristic[bluetoothGattService] = characteristics
            Timber.tag(TAG).d("CHARACTERISTICS: $characteristics")
            listCharacteristic.addAll(characteristics)
        }
        Timber.tag(TAG).d("MAP_CHARACTERISTIC: $mapCharacteristic")

        initExpandableList()

        listCharacteristic.forEach { char ->
            listDescriptor.addAll(char.descriptors)
        }

        binding.ivReadDesc.apply {
            visibility = View.VISIBLE
            setOnClickListener { checkUUID(listCharacteristic) }
        }

    }

    private fun checkUUID(chars: List<BluetoothGattCharacteristic>) {
        val uuidHearRate = UUID.fromString(STRING_UUID_HEART_RATE_CHAR)
        val charHearRate = chars.firstOrNull{char ->
            char.uuid == uuidHearRate
        }

        binding.root.snacked("CHAR_HEART_RATE: $charHearRate, ${charHearRate?.uuid}")
        Timber.tag(TAG).d("CHAR_HEART_RATE: $charHearRate, ${charHearRate?.uuid}")
    }

    private fun readDesc() {
        Timber.tag(TAG).d("LIST_DESCRIPTOR: $listDescriptor")
        Timber.tag(TAG).d("LIST_DESCRIPTOR_LAST_INDEX: ${listDescriptor.lastIndex}")

        val desc = listDescriptor[1]

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED
        ) {
            bluetoothGatt?.readDescriptor(desc)
        } else {
            tos("Permission BLUETOOTH_ADMIN not granted")
        }
    }

    @SuppressLint("MissingPermission")
    fun readCharacteristicManual(characteristic: BluetoothGattCharacteristic) {
        Timber.d("CheckReadChar: $bluetoothGatt")
        isRead = false
        loadingDialog.show("Read $characteristic")
        bluetoothGatt?.readCharacteristic(characteristic)
        Handler(Looper.getMainLooper()).postDelayed({
            if (!isRead) {
                loadingDialog.setResponse("Can't read $characteristic")
            }
        }, TIME_LOADING)
    }
    /*endregion*/


    /*region Library Fast BLE*/

    @SuppressLint("MissingPermission")
    private fun connectGatt() {
        if (bleDevice != null) {
            val connectGattCallback = object : BleGattCallback() {
                override fun onStartConnect() {
                    Timber.tag(BLELibsImplMainActivity.CONNECT_DEVICE).d("start_connect")
                    loadingDialog.show("Connecting Device")
                }

                override fun onConnectFail(device: BleDevice?, exception: BleException?) {
                    Timber.tag(BLELibsImplMainActivity.CONNECT_DEVICE)
                        .d("failed_connect: ${device?.device?.name} caused $exception")
//                    loadingDialog.setResponse("Failed to Connect Device ${bleDevice?.device?.name} caused $exception")
                    BleManager.getInstance().disconnect(device)
                }

                override fun onConnectSuccess(
                    device: BleDevice?,
                    gatt: BluetoothGatt?,
                    status: Int,
                ) {
                    Timber.tag(BLELibsImplMainActivity.CONNECT_DEVICE)
                        .d("success_connect: ${device?.device?.name}")
                    loadingDialog.dismiss()
                    binding.root.snacked("Success to connect Device ${device?.device?.name}")
                    getService()
                }

                override fun onDisConnected(
                    isActiveDisConnected: Boolean,
                    device: BleDevice?,
                    gatt: BluetoothGatt?,
                    status: Int,
                ) {
                    Timber.tag(BLELibsImplMainActivity.CONNECT_DEVICE)
                        .d("disconnected: ${device?.device?.name}")

                    if (isReconnect) {
                        connectGatt()
                    }
                }
            }
            BleManager.getInstance().connect(bleDevice, connectGattCallback)
        } else {
            finish()
            tos("Device is Empty")
        }

    }

    private fun getService() {
        listService.clear()
        mapCharacteristic.clear()

        initExpandableList()
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
                /*val flag = characteristic.properties
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

                binding.root.snacked("VALUE: $heartRate")*/


                if (data?.isNotEmpty() == true) {
                    val hexString: String = data.joinToString(separator = " ") {
                        String.format("%02X", it)
                    }
                Timber.tag(TAG_CHARACTERISTIC_READ).d("CHAR_VALUE: ${data.contentToString()} and $hexString")
                Timber.tag(TAG_CHARACTERISTIC_READ).d("CHAR_UUID: ${characteristic.uuid}")
                    binding.root.snacked("CHAR_VALUE: $data and $hexString")
                }
            }

            override fun onReadFailure(exception: BleException?) {
                loadingDialog.setResponse("Error to Read: $exception")
                /*if (exception?.description?.contains("This device is not connected") == true ||
                    exception?.description?.contains("gatt readCharacteristic fail") == true
                ) {
                    loadingDialog.setResponse("Error to Read: $exception").onDismiss { finish() }
                } else {
                    loadingDialog.setResponse("Error to Read: $exception")
                }*/
            }
        }

        loadingDialog.show("Read $characteristic")
        BleManager.getInstance().read(bleDevice, service.uuid.toString(),
            characteristic.uuid.toString(), readCallback)

    }

    /*endregion*/

    companion object {
        const val TAG = "BLEDetailServiceActivity_TAG"
        const val TAG_CHARACTERISTIC_READ = "CHARACTERISTIC_READ"

        const val TIME_LOADING = 10000L

        const val STRING_UUID_HEART_RATE_CHAR = "00002A37-0000-1000-8000-00805F9B34FB"
        const val STRING_UUID_SPO_2_CHAR = "00002A5C-0000-1000-8000-00805F9B34FB"
    }
}