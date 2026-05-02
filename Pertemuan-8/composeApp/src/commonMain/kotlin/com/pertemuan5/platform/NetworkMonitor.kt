package com.pertemuan5.platform

import kotlinx.coroutines.flow.StateFlow

expect class NetworkMonitor {
    val isConnected: StateFlow<Boolean>
}
