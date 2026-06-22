package com.pertemuan5.platform

expect class DeviceInfo {
    val deviceName: String
    val osVersion: String
    val sdkVersion: Int
    val appVersion: String
}
