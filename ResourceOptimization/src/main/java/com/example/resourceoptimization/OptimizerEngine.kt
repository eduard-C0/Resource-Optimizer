package com.example.resourceoptimization

import android.util.Log
import com.example.resourcereporter.ResourceLogEntry

class OptimizerEngine(private val rules: RuleSet = RuleSet()) {
    fun evaluateAndOptimize(logEntry: ResourceLogEntry) {
        val actions = mutableListOf<String>()
        val batteryLevel = logEntry.batteryLevel
        val cpuUsage = logEntry.cpuUsage
        val memoryUsedKb = logEntry.memoryUsedKb

        if (batteryLevel != null && batteryLevel < rules.lowBatteryThreshold) {
            OptimizationActions.reduceBatteryUsage()
            actions.add("Battery optimization")
        }

        if (cpuUsage != null && cpuUsage > rules.highCpuThreshold) {
            OptimizationActions.throttleCpuTasks()
            actions.add("CPU optimization")
        }

        if (memoryUsedKb != null && memoryUsedKb > rules.highMemoryThresholdKb) {
            OptimizationActions.clearCache()
            actions.add("Memory optimization")
        }

        if (actions.isNotEmpty()) {
            Log.i("OptimizerEngine", "Applied: ${actions.joinToString()}")
        }
    }
}