package com.example.resourcemonitoring.memory

import android.annotation.SuppressLint

object MemoryConverter {

    @SuppressLint("DefaultLocale")
    fun formatMemorySize(bytes: Long, unit: MemoryUnit): String {
        val kb = bytes / 1024.0
        val mb = kb / 1024.0
        val gb = mb / 1024.0

        return when (unit) {
            MemoryUnit.BYTES -> "$bytes Bytes"
            MemoryUnit.KB -> String.format("%.2f KB", kb)
            MemoryUnit.MB -> String.format("%.2f MB", mb)
            MemoryUnit.GB -> String.format("%.2f GB", gb)
        }
    }

    fun convertMemory(bytes: Long, unit: MemoryUnit): Double {
        val kb = bytes / 1024.0
        val mb = kb / 1024.0
        val gb = mb / 1024.0

        return when (unit) {
            MemoryUnit.BYTES -> bytes.toDouble()
            MemoryUnit.KB -> kb
            MemoryUnit.MB -> mb
            MemoryUnit.GB -> gb
        }
    }
}