package com.example.resourcemonitoring.cpu

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.sqrt

object CpuLoaderTest {

    private var stressJob: Job? = null

    // Function to start high CPU usage
    fun startCpuStressTest(cores: Int = Runtime.getRuntime().availableProcessors()) {
        stopCpuStressTest() // Ensure no other stress test is running

        stressJob = CoroutineScope(Dispatchers.Default).launch {
            repeat(cores) { coreIndex ->
                launch {
                    while (isActive) {
                        calculatePrimeNumbers()
                    }
                }
            }
        }
    }

    // Function to stop CPU stress test
    fun stopCpuStressTest() {
        stressJob?.cancel()
    }

    // Heavy mathematical computation (CPU-intensive)
    private fun calculatePrimeNumbers() {
        val primes = mutableListOf<Int>()
        for (i in 1..100_000) {
            if (isPrime(i)) primes.add(i)
        }
    }

    private fun isPrime(n: Int): Boolean {
        if (n < 2) return false
        for (i in 2..sqrt(n.toDouble()).toInt()) {
            if (n % i == 0) return false
        }
        return true
    }
}