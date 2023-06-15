package com.example.tryanimation.try_bluetooth_connection.bluetooth_trial

import android.os.Bundle
import com.crocodic.core.base.activity.CoreActivity
import com.crocodic.core.extension.checkLocationPermission
import com.crocodic.core.extension.snacked
import com.example.tryanimation.R
import com.example.tryanimation.databinding.ActivityMainBluetoothBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainBluetoothActivity :
    CoreActivity<ActivityMainBluetoothBinding, MainBluetoothViewModel>(R.layout.activity_main_bluetooth) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initState()

    }

    private fun initState() {
        checkLocationPermission {
            binding.root.snacked("Welcome to Main Activity")
        }
    }
}