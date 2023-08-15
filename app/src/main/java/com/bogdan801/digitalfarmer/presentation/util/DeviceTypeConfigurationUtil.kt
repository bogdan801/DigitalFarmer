package com.bogdan801.digitalfarmer.presentation.util

import android.content.res.Configuration

enum class DeviceConfig {
    PhonePortrait,
    PhoneLandscape,
    TabletPortrait,
    TabletLandscape
}

fun getDeviceConfiguration(config: Configuration): DeviceConfig{
    return when(config.orientation){
        Configuration.ORIENTATION_LANDSCAPE -> {
            if(config.screenWidthDp > 900) DeviceConfig.TabletLandscape else DeviceConfig.PhoneLandscape
        }
        Configuration.ORIENTATION_PORTRAIT -> {
            if(config.screenWidthDp > 600) DeviceConfig.TabletPortrait else DeviceConfig.PhonePortrait
        }
        else -> DeviceConfig.PhonePortrait
    }
}