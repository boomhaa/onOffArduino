import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.girlyanda.application.Arduino

import com.example.girlyanda.data.model.ConnectionStatus

import kotlinx.coroutines.*

const val LOGGER_TAG = "DeviceManager"
const val ACTION_USB_PERMISSION = "com.example.sdr_analyzer.USB_PERMISSION"

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@SuppressLint("UnspecifiedRegisterReceiverFlag")
class DeviceManager(
    private val context: Context,
    private val onStatusUpdated: (ConnectionStatus) -> Unit,
) {
    private var usbManager: UsbManager = context.getSystemService(Context.USB_SERVICE) as UsbManager
    var connectedDevice:Arduino? by mutableStateOf(null)
        private set
    val isConnected: Boolean
        get() = connectedDevice != null

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val permissionReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (ACTION_USB_PERMISSION == intent.action) {
                synchronized(this) {
                    val device = intent.getParcelableExtra<UsbDevice>(UsbManager.EXTRA_DEVICE)
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        device?.let {
                            Log.d(LOGGER_TAG, "Permission granted for device ${device.deviceName}")
                            detectAndConnect()
                        }
                    } else {
                        Log.d(LOGGER_TAG, "Permission denied for device ${device?.deviceName}")
                        onStatusUpdated(ConnectionStatus.Failed)
                    }
                }
            }
        }
    }
    private val detachedReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            if (UsbManager.ACTION_USB_DEVICE_DETACHED == intent.action) {
                Log.d(LOGGER_TAG, "Device detached")
                connectedDevice = null
                onStatusUpdated(ConnectionStatus.Waiting)
            }
        }
    }

    init {
        context.registerReceiver(
            detachedReceiver,
            IntentFilter(UsbManager.ACTION_USB_DEVICE_DETACHED)
        )
        context.registerReceiver(permissionReceiver, IntentFilter(ACTION_USB_PERMISSION))
        scope.launch {
            while (true) {
                if (!isConnected) {
                    detectAndConnect()
                }
                delay(3000)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun detectAndConnect() {
        val device = usbManager.deviceList?.values?.firstOrNull() ?: return

        if (!usbManager.hasPermission(device)) {
            Log.d(LOGGER_TAG, "Permission missing, requesting...")
            usbManager.requestPermission(
                device, PendingIntent.getBroadcast(
                    context, 0, Intent(ACTION_USB_PERMISSION), PendingIntent.FLAG_IMMUTABLE
                )
            )
            return
        }

        connectedDevice = Arduino(usbManager)
        onStatusUpdated(ConnectionStatus.Connected)
    }
}
