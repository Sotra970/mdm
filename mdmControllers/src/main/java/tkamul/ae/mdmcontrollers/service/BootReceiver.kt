package tkamul.ae.mdmcontrollers.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.content.ContextCompat.startForegroundService
import tkamul.ae.mdmcontrollers.domain.core.Logger.logd
import tkamul.ae.mdmcontrollers.service.MDMService.MDMService

/**
 * Created by sotra@appgain.io on 5/1/2018.
 */
class BootReceiver : BroadcastReceiver() {
    //    adb shell am broadcast -a android.intent.action.ACTION_BOOT_COMPLETED  -p ae.tkamul.mdm
    override fun onReceive(context: Context, intent: Intent) {
        logd("SampleBootReceiver  ${intent.action}")
            Intent(context, MDMService::class.java).also { intent ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(context , intent)
                } else {
                    context.startService(intent)
                }
            }
    }
}