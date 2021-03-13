package ae.tkamul

import android.app.Application
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.multidex.MultiDex
import dagger.hilt.android.HiltAndroidApp
import tkamul.ae.mdmcontrollers.service.MDMService.MDMService


/**
 * Created by sotra@altakamul.tr on 2/17/2021.
 */
@HiltAndroidApp
 class MDMApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        startMDMService()
    }

    // start mdm service to sync with socket channel and controllers
    private fun startMDMService() {
        Intent(this, MDMService::class.java).also { intent ->
            ContextCompat.startForegroundService(this,intent)
        }
    }
}