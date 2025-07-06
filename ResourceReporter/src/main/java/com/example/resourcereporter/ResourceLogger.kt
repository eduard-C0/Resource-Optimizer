package com.example.resourcereporter

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import java.io.File

object ResourceLogger {
    private val logs = mutableListOf<ResourceLogEntry>()

    fun logEntry(entry: ResourceLogEntry) {
        logs.add(entry)
        Log.d("ResourceLogger", "Logged: $entry")
    }

    fun getLogs(): List<ResourceLogEntry> = logs.toList()

    fun clearLogs() {
        logs.clear()
    }

    fun exportToJson(context: Context, filename: String = "resource_logs.json") {
        val json = Gson().toJson(logs)
        val file = File(context.filesDir, filename)
        file.writeText(json)
        Log.i("ResourceLogger", "Logs exported to ${file.absolutePath}")
    }
}