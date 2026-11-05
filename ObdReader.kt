package com.w211.copilot

import android.util.Log
import kotlin.concurrent.thread

class ObdReader {

    private var isRunning = false

    fun startReading() {
        isRunning = true
        thread(start = true) {
            while (isRunning) {
                try {
                    Log.d("W211_OBD", "Citește datele de la mufa OBD...")
                    Thread.sleep(5000)
                } catch (e: InterruptedException) {
                    break
                }
            }
        }
    }

    fun stopReading() {
        isRunning = false
    }
}
