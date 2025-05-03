package com.example.resourcemonitoring.memory

data class MemoryReport(
    val systemUsed: Float,
    val systemTotal: Float,
    val appUsedHeap: Float,
    val appNative: Float,
    val memoryUnit: MemoryUnit
)

enum class MemoryUnit {
    BYTES, KB, MB, GB
}