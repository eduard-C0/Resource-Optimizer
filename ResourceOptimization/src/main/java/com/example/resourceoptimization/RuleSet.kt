package com.example.resourceoptimization

data class RuleSet(
    val lowBatteryThreshold: Int = 20,
    val highCpuThreshold: Float = 80f,
    val highMemoryThresholdKb: Long = 300000
)