package com.example.resourcemonitoring.memory

import android.content.Context
import kotlinx.coroutines.flow.Flow

interface MemoryMonitor {
    fun startMonitoring(context: Context, memoryUnit: MemoryUnit)
    fun stopMonitoring()
    val memoryLevel: Flow<MemoryReport?>
}