package com.example.girlyanda.application

import DeviceManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.derivedStateOf

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.girlyanda.data.model.ConnectionStatus


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class AppAnalyzer(val context: Context){
    var connectionStatus by mutableStateOf(ConnectionStatus.Waiting)
    private val _deviceManager = DeviceManager(
        context = context,
        onStatusUpdated = { connectionStatus = it },
    )

    val connectedDevice by derivedStateOf { _deviceManager.connectedDevice }

}
