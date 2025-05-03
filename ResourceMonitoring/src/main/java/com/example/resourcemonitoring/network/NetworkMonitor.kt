package com.example.resourcemonitoring.network

import android.content.Context
import kotlinx.coroutines.flow.Flow

interface NetworkMonitor {
    fun startMonitoring(context: Context)
    fun stopMonitoring()
    val networkDataUsage: Flow<NetworkUsageData?>
}