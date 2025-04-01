package com.example.girlyanda.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.girlyanda.application.AppAnalyzer
import com.example.girlyanda.ui.components.*



@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MainScreen(app: AppAnalyzer) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .animateContentSize()
    ) {
        NavigationBar(
            connectionStatus = app.connectionStatus,

        )

        if (app.connectedDevice == null) {
            WaitingForDevice()
        } else {
            Column(modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
                ) {
                Button(onClick = { app.connectedDevice!!.turnArduinoOn()}) {
                    Text(text = "Включить")
                }
                Spacer(modifier = Modifier.padding(5.dp))
                Button(onClick = { app.connectedDevice!!.turnArduinoOff()}) {
                    Text(text = "Выключить")
                }
            }

        }
    }
}

@Composable
fun WaitingForDevice() {
    val textColor = Color(0xFF383232)
    val titleTextStyle = TextStyle(
        fontSize = 36.sp,
        color = textColor,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.SansSerif
    )
    val instructionTextStyle = TextStyle(
        fontSize = 16.sp,
        color = textColor,
        fontWeight = FontWeight.Normal,
        fontFamily = FontFamily.SansSerif
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Ожидание\nустройства",
            style = titleTextStyle,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Column(
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            Text(
                text = "1. Подключите устройство с помощью USB-кабеля",
                style = instructionTextStyle,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "2. Убедитесь что устройство включено и работает",
                style = instructionTextStyle,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "3. Разрешите доступ к устройству в диалогом окне",
                style = instructionTextStyle,
                textAlign = TextAlign.Center
            )
        }
    }
}
