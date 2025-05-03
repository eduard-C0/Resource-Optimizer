package com.example.resourceoptimizer

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.resourcemonitoring.battery.BatteryMonitor
import com.example.resourcemonitoring.cpu.CpuLoaderTest
import com.example.resourcemonitoring.cpu.CpuMonitor
import com.example.resourcemonitoring.memory.MemoryLoaderTest
import com.example.resourcemonitoring.memory.MemoryMonitor
import com.example.resourcemonitoring.memory.MemoryReport
import com.example.resourcemonitoring.memory.MemoryUnit
import com.example.resourcemonitoring.network.NetworkMonitor
import com.example.resourceoptimizer.ui.theme.ResourceOptimizerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var batteryMonitor: BatteryMonitor

    @Inject
    lateinit var memoryMonitor: MemoryMonitor

    @Inject
    lateinit var cpuMonitor: CpuMonitor

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ResourceOptimizerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

        cpuMonitor.startMonitoring(this)
        memoryMonitor.startMonitoring(this, MemoryUnit.MB)
        batteryMonitor.startMonitor(this)
        networkMonitor.startMonitoring(this)


        cpuMonitor.cpuUsage.onEach {
            Log.e("MYERROR", "cpuMonitor: $it")
        }.launchIn(lifecycleScope)

        batteryMonitor.batteryLevel.onEach {
            Log.e("MYERROR", "batteryMonitor: $it")
        }.launchIn(lifecycleScope)

        memoryMonitor.memoryLevel.onEach {
            it?.let { memoryReport -> logMemoryUsage(memoryReport) }
        }.launchIn(lifecycleScope)

        networkMonitor.networkDataUsage.onEach {
            Log.e("MYERROR", "mobileDataUsage: $it")
        }.launchIn(lifecycleScope)
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("CPU TESTS")
        Spacer(Modifier.height(16.dp))
        TextButton(
            modifier = Modifier
                .width(250.dp)
                .height(70.dp),
            onClick = {
                CpuLoaderTest.startCpuStressTest()
            },
            colors = ButtonDefaults.textButtonColors(containerColor = Color.Green)
        ) { Text("Start CPU stress test") }
        Spacer(Modifier.height(16.dp))
        TextButton(
            modifier = Modifier
                .width(250.dp)
                .height(70.dp),
            onClick = {
                CpuLoaderTest.stopCpuStressTest()
            },
            colors = ButtonDefaults.textButtonColors(containerColor = Color.Red)
        ) { Text("Stop CPU stress test") }
        Spacer(Modifier.height(32.dp))
        Text("MEMORY TESTS")
        Spacer(Modifier.height(16.dp))
        TextButton(
            modifier = Modifier
                .width(250.dp)
                .height(70.dp),
            onClick = {
                MemoryLoaderTest.startMemoryStressTest()
            },
            colors = ButtonDefaults.textButtonColors(containerColor = Color.Green)
        ) { Text("Start MEMORY test") }
        Spacer(Modifier.height(16.dp))
        TextButton(
            modifier = Modifier
                .width(250.dp)
                .height(70.dp),
            onClick = {
                MemoryLoaderTest.stopMemoryStressTest()
            },
            colors = ButtonDefaults.textButtonColors(containerColor = Color.Red)
        ) { Text("Stop MEMORY test") }

        Spacer(Modifier.height(32.dp))
        Text("NETWORK TESTS")
        Spacer(Modifier.height(16.dp))
        TextButton(
            modifier = Modifier
                .width(250.dp)
                .height(70.dp),
            onClick = {
                NetworkLoaderTest.startStressTest()
            },
            colors = ButtonDefaults.textButtonColors(containerColor = Color.Green)
        ) { Text("Start NETWORK test") }
        Spacer(Modifier.height(16.dp))
        TextButton(
            modifier = Modifier
                .width(250.dp)
                .height(70.dp),
            onClick = {
                NetworkLoaderTest.stopStressTest()
            },
            colors = ButtonDefaults.textButtonColors(containerColor = Color.Red)
        ) { Text("Stop NETWORK test") }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ResourceOptimizerTheme {
        Greeting("Android")
    }
}

fun logMemoryUsage(memoryReport: MemoryReport) {
    Log.d("MYERROR", "==================== Memory Report ====================")

    Log.d("MYERROR", "System Memory:")
    Log.d("MYERROR", "  - Used Memory: ${memoryReport.systemUsed} ${memoryReport.memoryUnit}")
    Log.d("MYERROR", "  - Total Memory: ${memoryReport.systemTotal} ${memoryReport.memoryUnit}")

    Log.d("MYERROR", "App Memory:")
    Log.d("MYERROR", "  - Used Heap Memory: ${memoryReport.appUsedHeap} ${memoryReport.memoryUnit}")
    Log.d("MYERROR", "  - Native Memory Used: ${memoryReport.appNative} ${memoryReport.memoryUnit}")

    Log.d("MYERROR", "========================================================")
}