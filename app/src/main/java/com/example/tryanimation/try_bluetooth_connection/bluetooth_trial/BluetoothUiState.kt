package com.example.tryanimation.try_bluetooth_connection.bluetooth_trial

import com.example.tryanimation.try_bluetooth_connection.bluetooth_trial.domain.bluetooth.BluetoothDevice

data class BluetoothUiState(
    val scannedDevices: List<BluetoothDevice> = emptyList(),
    val pairedDevices: List<BluetoothDevice> = emptyList()
)
