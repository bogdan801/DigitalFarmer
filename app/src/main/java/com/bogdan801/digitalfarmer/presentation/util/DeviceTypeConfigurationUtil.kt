package com.bogdan801.digitalfarmer.presentation.util

import android.content.res.Configuration

data class DeviceConfig(
    val type: DeviceType,
    val orientation: DeviceOrientation
)

enum class DeviceType{
    Phone,
    Tablet
}

enum class DeviceOrientation{
    Portrait,
    Landscape
}

fun getDeviceConfiguration(config: Configuration): DeviceConfig{
    return when(config.orientation){
        Configuration.ORIENTATION_LANDSCAPE -> {
            if(config.screenWidthDp > 900) DeviceConfig(DeviceType.Tablet, DeviceOrientation.Landscape)
            else DeviceConfig(DeviceType.Phone, DeviceOrientation.Landscape)
        }
        Configuration.ORIENTATION_PORTRAIT -> {
            if(config.screenWidthDp > 600) DeviceConfig(DeviceType.Tablet, DeviceOrientation.Portrait)
            else DeviceConfig(DeviceType.Phone, DeviceOrientation.Portrait)
        }
        else -> DeviceConfig(DeviceType.Phone, DeviceOrientation.Portrait)
    }
}