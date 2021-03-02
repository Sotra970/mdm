package tkamul.ae.mdmcontrollers.service.MDMService

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import tkamul.ae.mdmcontrollers.R
import tkamul.ae.mdmcontrollers.domain.core.Config

/**
 * Created by sotra@altakamul.tr on 2/24/2021.
 */
object MDMServiceNotification {


    @TargetApi(Build.VERSION_CODES.O)
    private fun getNotificationChannel(context: Context): NotificationChannel {
        val channel  =  NotificationChannel(Config.SericeNotification.MDM_NOTIFICATION_CHANNNEL_ID, Config.SericeNotification.MDM_NOTIFICATION_CHANNNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
        (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).run {
                createNotificationChannel(channel)
        }
       return channel
    }


    internal fun getNotificationBuilder(
        context: Context,
        title: String, body: String?
    ): NotificationCompat.Builder {
        val channelID :String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getNotificationChannel(
                context
            ).id
        }else {
            Config.SericeNotification.MDM_NOTIFICATION_CHANNNEL_ID
        }
        return NotificationCompat.Builder(context, channelID).apply {
            setAutoCancel(false)
            setContentText(body)
            setContentTitle(title)
            setSmallIcon(R.drawable.ic_mdm)
        }
    }
}