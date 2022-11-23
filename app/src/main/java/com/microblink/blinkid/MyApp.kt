package com.microblink.blinkid

import android.app.Application
import com.microblink.MicroblinkSDK

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        MicroblinkSDK.setLicenseFile("com.microblink.blinkid.mblic", this)
    }
}