package com.example.resourceoptimizer.appmodule

import android.content.Context
import com.example.resourcemonitoring.battery.BatteryMonitor
import com.example.resourcemonitoring.battery.BatteryMonitorImpl
import com.example.resourcemonitoring.cpu.CpuMonitor
import com.example.resourcemonitoring.cpu.CpuMonitorImpl
import com.example.resourcemonitoring.memory.MemoryMonitor
import com.example.resourcemonitoring.memory.MemoryMonitorImpl
import com.example.resourcemonitoring.network.NetworkMonitor
import com.example.resourcemonitoring.network.NetworkMonitorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBatteryMonitor(@ApplicationContext context: Context): BatteryMonitor = BatteryMonitorImpl()

    @Provides
    @Singleton
    fun provideMemoryMonitor(@ApplicationContext context: Context): MemoryMonitor = MemoryMonitorImpl()

    @Provides
    @Singleton
    fun provideCpuMonitor(@ApplicationContext context: Context): CpuMonitor = CpuMonitorImpl()

    @Provides
    @Singleton
    fun provideNetworkMonitor(@ApplicationContext context: Context): NetworkMonitor = NetworkMonitorImpl()
}