package com.example.resourceoptimization

import android.content.Context
import android.util.Log
import com.example.resourcemonitoring.battery.BatteryMonitor
import com.example.resourcemonitoring.battery.BatteryMonitorImpl
import com.example.resourcemonitoring.cpu.CpuMonitor
import com.example.resourcemonitoring.cpu.CpuMonitorImpl
import com.example.resourcemonitoring.memory.MemoryMonitor
import com.example.resourcemonitoring.memory.MemoryMonitorImpl
import com.example.resourcemonitoring.memory.MemoryUnit
import com.example.resourcemonitoring.network.NetworkMonitor
import com.example.resourcemonitoring.network.NetworkMonitorImpl
import com.example.resourcereporter.ResourceLogEntry
import com.example.resourcereporter.ResourceLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn

class ResourceOptimizerImpl : ResourceOptimizer {
    private lateinit var cpuMonitor: CpuMonitor
    private lateinit var memoryMonitor: MemoryMonitor
    private lateinit var batteryMonitor: BatteryMonitor
    private lateinit var networkMonitor: NetworkMonitor

    override fun init(context: Context) {
        cpuMonitor = CpuMonitorImpl()
        memoryMonitor = MemoryMonitorImpl()
        batteryMonitor = BatteryMonitorImpl()
        networkMonitor = NetworkMonitorImpl()
        startMonitoring(context = context)
    }

    private fun startMonitoring(context: Context) {
        cpuMonitor.startMonitoring(context)
        memoryMonitor.startMonitoring(context, MemoryUnit.MB)
        batteryMonitor.startMonitor(context)
        networkMonitor.startMonitoring(context)

        val optimizer = OptimizerEngine()

        combine(
            cpuMonitor.cpuUsage,
            memoryMonitor.memoryLevel,
            batteryMonitor.batteryLevel,
            networkMonitor.networkDataUsage
        ) { cpu, memory, battery, network ->
            val entry = ResourceLogEntry(
                batteryLevel = battery?.level,
                batteryTemp = battery?.temperature,
                cpuUsage = cpu,
                memoryUsedKb = memory?.systemUsed,
                networkSentKb = network?.transmittedBytes,
                networkReceivedKb = network?.receivedBytes
            )
            ResourceLogger.logEntry(entry)
            optimizer.evaluateAndOptimize(entry)
            Log.e(
                "MYERROR", "cpu: $cpu \n" +
                        "memory: $memory \n" +
                        "battery: $battery \n" +
                        "network: $network \n"
            )
            Log.e("MYERROR", "entry: $entry")
        }.launchIn(CoroutineScope(Dispatchers.IO))
    }

    private fun stopMonitoring() {
        cpuMonitor.stopMonitoring()
        memoryMonitor.stopMonitoring()
        batteryMonitor.stopMonitor()
        networkMonitor.stopMonitoring()
    }
}