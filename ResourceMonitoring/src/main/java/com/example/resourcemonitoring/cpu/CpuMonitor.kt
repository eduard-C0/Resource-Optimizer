package com.example.resourcemonitoring.cpu

import android.content.Context
import kotlinx.coroutines.flow.Flow

interface CpuMonitor {
    fun startMonitoring(context: Context)
    fun stopMonitoring()
    val cpuUsage: Flow<String?>
}