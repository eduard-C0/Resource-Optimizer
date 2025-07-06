package com.example.resourcemonitoring.memory

import android.app.ActivityManager
import android.content.Context
import android.os.Debug
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MemoryMonitorImpl : MemoryMonitor {

    private val _memoryLevel: MutableStateFlow<MemoryReport?> = MutableStateFlow(null)
    override val memoryLevel: Flow<MemoryReport?> = _memoryLevel

    private var monitoringJob: Job? = null

    override fun startMonitoring(context: Context, memoryUnit: MemoryUnit) {
        monitoringJob = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                getFullMemoryUsage(context, memoryUnit)
                delay(5000)
            }
        }
    }

    override fun stopMonitoring() {
        monitoringJob?.cancel()
    }

    private suspend fun getFullMemoryUsage(context: Context, memoryUnit: MemoryUnit) {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)

        val systemUsed =
            MemoryConverter.convertMemory(memoryInfo.totalMem - memoryInfo.availMem, memoryUnit)
        val systemTotal = MemoryConverter.convertMemory(memoryInfo.totalMem, memoryUnit)

        val runtime = Runtime.getRuntime()
        val usedHeap = MemoryConverter.convertMemory(
            runtime.totalMemory() - runtime.freeMemory(),
            memoryUnit
        )

        val debugMemoryInfo = Debug.MemoryInfo()
        Debug.getMemoryInfo(debugMemoryInfo)
        val nativeUsed =
            MemoryConverter.convertMemory(debugMemoryInfo.nativePss * 1024L, memoryUnit)

        _memoryLevel.emit(
            MemoryReport(
                systemUsed.toFloat(),
                systemTotal.toFloat(),
                usedHeap.toFloat(),
                nativeUsed.toFloat(),
                memoryUnit
            )
        )
    }
}