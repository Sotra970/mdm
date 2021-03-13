package tkamul.ae.mdmcontrollers.service.MDMService

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import dagger.hilt.android.AndroidEntryPoint
import tkamul.ae.mdmcontrollers.contollers.EventExecutorController
import tkamul.ae.mdmcontrollers.data.gateways.socketModels.sendingObject.Args
import tkamul.ae.mdmcontrollers.data.gateways.socketModels.sendingObject.DeviceInfo2SocketPayload
import tkamul.ae.mdmcontrollers.data.gateways.socketgateway.SocketEventListener
import tkamul.ae.mdmcontrollers.domain.core.Config
import tkamul.ae.mdmcontrollers.domain.core.Logger
import tkamul.ae.mdmcontrollers.domain.core.extentionFunction.toArgsResponse
import tkamul.ae.mdmcontrollers.domain.entities.MDMInfo
import tkamul.ae.mdmcontrollers.domain.useCases.CSUseCases.MDMInfoUseCase
import tkamul.ae.mdmcontrollers.domain.useCases.remote.MDMSocketChannelUseCase
import javax.inject.Inject

class MDMService : Service()  , MDMServiceEventInterface {
    private var startMode: Int = START_REDELIVER_INTENT // indicates how to behave if the service is killed
    private var allowRebind: Boolean = true   // indicates whether onRebind should be used

    @Inject
    lateinit var mdmSocketChannelUseCase: MDMSocketChannelUseCase
    @Inject
    lateinit var mdmInfoController: MDMInfoUseCase


    @Inject
    lateinit var eventExecutorController: EventExecutorController


    val mdmServiceImplementer = MDMServiceImplementer(this)




    override fun onCreate() {
        super.onCreate()
        showNotification(Config.SericeNotification.MDM_NOTIFICATION_ATTACHED_BODY)
       /* eventExecutorController.invokeMDMInfo {
            startListingOnSocketEvents(it)
        }*/
    }


    @SuppressLint("CheckResult")
    private fun startListingOnSocketEvents(info: MDMInfo) {
        mdmSocketChannelUseCase.observe(info.deviceInfo.serial_number , object : SocketEventListener {
            override fun onConnect(args: Any) {
                sendDeviceInfo()
                showNotification(Config.SericeNotification.MDM_NOTIFICATION_RUNNING_BODY)
            }
            override fun onDisconnect(vararg args: Any) {
                showNotification(Config.SericeNotification.MDM_NOTIFICATION_DISCONECTED_BODY)
            }
            override fun onNewMessage(vararg args: Any) {
                eventExecutorController.invokProcess(args.toArgsResponse())
            }
        })
    }

    private fun sendDeviceInfo() {
        mdmInfoController.invoke {
            mdmSocketChannelUseCase.send(DeviceInfo2SocketPayload(
                    args = Args("onConnect"),
                    device = it,
                    event = Config.Events.ON_CONNECT
            ))
        }
    }

    /* private fun sendDeviceInfo(info: MDMInfo, pairs:NameValuePairs) {
         mdmSocketChannelUseCase.send(
             DeviceInfo2SocketPayload(
                     args = Args(pairs.args.nameValuePairs.ray_id),
                     device = info,
                     event = Config.Events.ON_CONNECT
             ))
     }*/


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // The service is starting, due to a call to startService()
        return startMode
    }
    // A client is binding to the service with bindService()
    override fun onBind(intent: Intent): IBinder {
        return mdmServiceImplementer
    }

    override fun onUnbind(intent: Intent): Boolean {
        // All clients have unbound with unbindService()
        return allowRebind
    }

    override fun onDestroy() {
        // The service is no longer used and is being destroyed
       showNotification(Config.SericeNotification.MDM_NOTIFICATION_STOPED_BODY)
    }


    fun showNotification(message : String ) {
        val notificationBuilder =
            MDMServiceNotification.getNotificationBuilder(
                this,
                title = Config.SericeNotification.MDM_NOTIFICATION_TITLE,
                body = message
            )
        val notificationId = System.currentTimeMillis().toInt()
        startForeground(notificationId, notificationBuilder.build());
    }

   override fun completeEvent(eventId: String?) {
      Logger.logd(" eventId  $eventId")
    }


}