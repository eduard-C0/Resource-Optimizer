package com.example.resourceoptimizer

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.IOException

object NetworkLoaderTest {
    private val client = OkHttpClient()
    private var loadJob: Job? = null

    /**
     * Starts simulated network traffic using OkHttp GET and POST requests
     * @param repeats Number of requests per thread
     * @param threads Number of concurrent threads (coroutines)
     */
    fun startStressTest(
        url: String = "https://httpbin.org/get",
        repeats: Int = 10,
        threads: Int = 5
    ) {
        stopStressTest()

        loadJob = CoroutineScope(Dispatchers.IO).launch {
            repeat(threads) {
                launch {
                    repeat(repeats) {
                        runGetRequest(url)
                        delay(100) // add small delay to simulate real usage
                    }
                }
            }
        }
    }

    fun stopStressTest() {
        loadJob?.cancel()
        loadJob = null
    }

    private fun runGetRequest(url: String) {
        val request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("❌ Request failed: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                response.close() // we just simulate load, so discard data
                println("✅ GET ${response.code}")
            }
        })
    }

    fun runPostRequest(url: String, bodySizeKB: Int = 256) {
        val data = "x".repeat(bodySizeKB * 1024)
        val requestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), data)

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("❌ POST failed: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                response.close()
                println("✅ POST ${response.code}")
            }
        })
    }
}