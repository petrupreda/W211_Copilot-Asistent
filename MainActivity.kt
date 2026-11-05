package com.w211.copilot

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var statusText: TextView
    private lateinit var btnStart: Button
    private lateinit var btnStop: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val layout = android.widget.LinearLayout(this).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            setPadding(50, 50, 50, 50)
        }

        statusText = TextView(this).apply {
            text = "W211 Copilot: Oprit"
            textSize = 20f
            setPadding(0, 0, 0, 30)
        }

        btnStart = Button(this).apply {
            text = "Pornește Asistentul"
            setOnClickListener { startCarService() }
        }

        btnStop = Button(this).apply {
            text = "Oprește"
            setOnClickListener { stopCarService() }
        }

        layout.addView(statusText)
        layout.addView(btnStart)
        layout.addView(btnStop)
        setContentView(layout)

        checkPermissions()
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN), 1)
            }
        }
    }

    private fun startCarService() {
        val serviceIntent = Intent(this, CarService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }
        statusText.text = "W211 Copilot: Pornit și activ în fundal"
    }

    private fun stopCarService() {
        stopService(Intent(this, CarService::class.java))
        statusText.text = "W211 Copilot: Oprit"
    }
}
