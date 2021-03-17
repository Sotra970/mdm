package tkamul.ae.mdmcontrollers.domain.interactors.notification

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import tkamul.ae.mdmcontrollers.R
import tkamul.ae.mdmcontrollers.domain.core.Config
import javax.inject.Inject

/**
 * Created by sotra@altakamul.tr on 3/16/2021.
 */
class NotificationInteractor @Inject constructor(
        val context: Context
) {

    fun invoke(title:String, body: String) = getNewNotificationBuilder(title  , body)

    @TargetApi(Build.VERSION_CODES.O)
    private fun createChannel(): NotificationChannel {
        val channel  =  NotificationChannel(System.currentTimeMillis().toString(), Config.SericeNotification.MDM_NOTIFICATION_CHANNNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
        (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).run {
            createNotificationChannel(channel)
        }
        return channel
    }


    internal fun getNewNotificationBuilder(
            title: String, body: String
    ): NotificationCompat.Builder {
        val channelID :String = getNewChannelId()
        return NotificationCompat.Builder(context, channelID).apply {
            setAutoCancel(false)
            setContentText(body)
            setContentTitle(title)
            setSmallIcon(R.drawable.ic_mdm)
        }
    }

    private fun getNewChannelId(): String {
       return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel().id
        }else {
            System.currentTimeMillis().toString()
        }
    }
}