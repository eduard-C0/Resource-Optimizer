package com.example.resourcemonitoring.battery

import android.content.Context
import kotlinx.coroutines.flow.Flow

interface BatteryMonitor {
    val batteryLevel: Flow<BatteryInfo?>
    fun startMonitor(context: Context)
    fun stopMonitor()
}