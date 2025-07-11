package com.example.resourcemonitoring.cpu

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

class CpuMonitorImpl: CpuMonitor {
    private var monitoringJob: Job? = null

    private val _cpuUsage: MutableStateFlow<Float?> = MutableStateFlow(null)
    override val cpuUsage: Flow<Float?> = _cpuUsage

    override fun startMonitoring(context: Context) {
        monitoringJob = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                withContext(Dispatchers.Main) {
                    _cpuUsage.emit(getNormalizedAppCpuUsage())
                }
                delay(5000)
            }
        }
    }

    override fun stopMonitoring() {
        monitoringJob?.cancel()
    }

    private fun getNormalizedAppCpuUsage(): Float {
        val pid = android.os.Process.myPid().toString()
        val process = Runtime.getRuntime().exec("top -n 1")
        val reader = BufferedReader(InputStreamReader(process.inputStream))
        val rawOutput = reader.readText()
        reader.close()

        val cleanOutput = stripAnsiCodes(rawOutput)
        val lines = cleanOutput.lines()

        for (line in lines) {
            if (line.contains(pid)) {
                val tokens = line.trim().split(Regex("\\s+"))
                val cpuStr = tokens.getOrNull(8)
                val rawCpu = cpuStr?.toFloatOrNull() ?: return -1f
                val cores = Runtime.getRuntime().availableProcessors()
                val normalizedCpu = rawCpu / cores

                // Round to 1 decimal place and return
                return "%.1f".format(normalizedCpu).toFloat()
            }
        }

        return -1f
    }

    fun stripAnsiCodes(input: String): String {
        val ansiRegex = Regex("\u001B\\[[;\\d]*[A-Za-z]")
        return ansiRegex.replace(input, "")
    }
}