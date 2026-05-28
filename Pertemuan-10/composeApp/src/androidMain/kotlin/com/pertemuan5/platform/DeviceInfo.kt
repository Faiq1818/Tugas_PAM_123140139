package com.pertemuan5.platform

import android.content.Context
import android.os.Build

actual class DeviceInfo(context: Context) {
    actual val deviceName: String = "${Build.MANUFACTURER} ${Build.MODEL}"
    actual val osVersion: String = Build.VERSION.RELEASE
    actual val sdkVersion: Int = Build.VERSION.SDK_INT
    actual val appVersion: String = try {
        context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: "Unknown"
    } catch (e: Exception) {
        "Unknown"
    }
}
