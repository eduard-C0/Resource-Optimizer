package com.example.resourcemonitoring.network

import android.app.usage.NetworkStats
import android.app.usage.NetworkStatsManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.TrafficStats
import android.os.RemoteException
import android.util.Log
import com.example.resourcemonitoring.memory.MemoryConverter
import com.example.resourcemonitoring.memory.MemoryUnit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class NetworkMonitorImpl: NetworkMonitor {
    private val _networkDataUsage = MutableStateFlow<NetworkUsageData?>(null)
    override val networkDataUsage: Flow<NetworkUsageData?> get() = _networkDataUsage

    private var monitoringJob: Job? = null

    override fun startMonitoring(context: Context) {
        val uid = android.os.Process.myUid()
        var lastRx = TrafficStats.getUidRxBytes(uid)
        var lastTx = TrafficStats.getUidTxBytes(uid)
        stopMonitoring()
        monitoringJob = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                val currentRx = TrafficStats.getUidRxBytes(uid)
                val currentTx = TrafficStats.getUidTxBytes(uid)

                if (lastRx != TrafficStats.UNSUPPORTED.toLong() && lastTx != TrafficStats.UNSUPPORTED.toLong()) {
                    val deltaRx = currentRx - lastRx
                    val deltaTx = currentTx - lastTx
                    _networkDataUsage.emit(NetworkUsageData(deltaRx, deltaTx))
                }

                lastRx = currentRx
                lastTx = currentTx
                delay(1000L)
            }
        }
    }

    override fun stopMonitoring() {
        monitoringJob?.cancel()
        monitoringJob = null
    }
}

data class NetworkUsageData(val receivedBytes: Long, val transmittedBytes: Long)

