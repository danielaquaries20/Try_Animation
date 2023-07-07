package com.example.tryanimation.try_bluetooth_connection.bluetooth_trial.ble_library

import com.clj.fastble.BleManager
import com.clj.fastble.data.BleDevice

data class DataBLEManagerExtra(
    var bleManager: BleManager?= null,
    var bleDevice: BleDevice?= null,
)
