package com.example.resourcereporter

data class ResourceLogEntry(
    val timestamp: Long = System.currentTimeMillis(),
    val batteryLevel: Int?,
    val batteryTemp: Double?,
    val cpuUsage: Float?,
    val memoryUsedKb: Float?,
    val networkReceivedKb: Long?,
    val networkSentKb: Long?,
    val notes: String =  ""
)
