package com.example.tryanimation.try_bluetooth_connection.bluetooth_trial

import com.crocodic.core.base.viewmodel.CoreViewModel
import com.example.tryanimation.try_bluetooth_connection.bluetooth_trial.domain.bluetooth.BluetoothController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class MainBluetoothViewModel @Inject constructor(
    private val bluetoothController: BluetoothController,
) : CoreViewModel() {
    override fun apiLogout() {}

    override fun apiRenewToken() {}

    private val _state = MutableStateFlow(BluetoothUiState())


}