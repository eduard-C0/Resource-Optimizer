package com.example.resourcemonitoring.memory

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

object MemoryLoaderTest {
    private var stressJob: Job? = null

    fun startMemoryStressTest(memoryUnit: MemoryUnit = MemoryUnit.MB, memorySizePerAllocation: Int = 10) {
        stopMemoryStressTest() // Ensure no other stress test is running

        stressJob = CoroutineScope(Dispatchers.Default).launch {
            val allocatedMemory = mutableListOf<ByteArray>()

            // Continuously allocate memory
            while (isActive) {
                try {
                    // Allocate memory in chunks (memorySizePerAllocation per allocation)
                    val memoryBlock = ByteArray(memorySizePerAllocation * 1024 * 1024) // Allocate in MB
                    allocatedMemory.add(memoryBlock)

                    // Log memory usage periodically
                    if (allocatedMemory.size % 10 == 0) {
                        Log.d("MEMORY_STRESS", "Allocated memory: ${allocatedMemory.size * memorySizePerAllocation} MB")
                    }
                } catch (e: OutOfMemoryError) {
                    Log.e("MEMORY_STRESS", "Out of memory error: ${e.message}")
                    break
                }
            }
        }
    }

    fun stopMemoryStressTest() {
        stressJob?.cancel()  // Stop the stress test
        stressJob = null
    }
}