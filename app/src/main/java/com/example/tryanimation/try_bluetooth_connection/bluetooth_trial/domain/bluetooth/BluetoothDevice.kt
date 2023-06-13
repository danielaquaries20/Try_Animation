package com.example.tryanimation.try_bluetooth_connection.bluetooth_trial.domain.bluetooth

typealias BluetoothDeviceDomain = BluetoothDevice

data class BluetoothDevice(
    val name : String?,
    val address : String,
)
