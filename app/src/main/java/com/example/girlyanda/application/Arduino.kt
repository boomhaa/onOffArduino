package com.example.girlyanda.application

import android.hardware.usb.UsbManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.hoho.android.usbserial.driver.UsbSerialPort
import com.hoho.android.usbserial.driver.UsbSerialProber


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class Arduino(manager: UsbManager) {

    private lateinit var port: UsbSerialPort

    init {
        try {
            val availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager)
            if (availableDrivers.isNotEmpty()) {
                for (driver in availableDrivers) {
                    if (driver.device.vendorId != 1155) {

                        val con = manager.openDevice(driver.device)
                        if (con != null) {
                            port = driver.ports[0]
                            port.open(con)
                            Log.d("Arduino", "Порт открыт")
                            port.setParameters(
                                9600,
                                8,
                                UsbSerialPort.STOPBITS_1,
                                UsbSerialPort.PARITY_NONE
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.d("Arduino", "Exception: $e")

        }
    }


    fun turnArduinoOn(){
        try {
            port.write("1\n".toByteArray(), 1000)
            Log.d("Arduino", "Отправлен 1")
        }catch (e: Exception) {
            Log.d("Arduino", "Exception: $e")
        }
    }

    fun turnArduinoOff(){
        try {
            port.write("0\n".toByteArray(), 1000)
            Log.d("Arduino", "Отправлен 1")
        }catch (e: Exception) {
            Log.d("Arduino", "Exception: $e")
        }

    }
}