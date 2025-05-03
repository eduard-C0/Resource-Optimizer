package com.example.resourcemonitoring.battery

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class BatteryMonitorImpl : BatteryMonitor {

    private val _batteryLevel: MutableStateFlow<BatteryInfo?> = MutableStateFlow(null)
    override val batteryLevel: Flow<BatteryInfo?> = _batteryLevel

    private var monitoringJob: Job? = null

    override fun startMonitor(context: Context){
        monitoringJob = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                monitorBatteryStatus(context = context)
                delay(5000)
            }
        }
    }

    override fun stopMonitor(){
        monitoringJob?.cancel()
    }

    private suspend fun monitorBatteryStatus(context: Context) {
        val intent: Intent? =
            context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val level = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale = intent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
        val temperature = intent?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1) ?: -1

        val batteryPct = if (scale > 0) (level * 100) / scale else -1
        val batteryTemp = temperature / 10.0 // Convert to Celsius

        _batteryLevel.emit(BatteryInfo(batteryPct, batteryTemp))
    }

}