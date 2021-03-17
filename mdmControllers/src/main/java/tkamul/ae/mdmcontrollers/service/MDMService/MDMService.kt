package tkamul.ae.mdmcontrollers.service.MDMService

import android.app.Service
import android.content.Intent
import android.os.IBinder
import dagger.hilt.android.AndroidEntryPoint
import tkamul.ae.mdmcontrollers.contollers.EventExecutorController
import tkamul.ae.mdmcontrollers.data.gateways.socketgateway.socketRemoteModels.sendingObject.Args
import tkamul.ae.mdmcontrollers.data.gateways.socketgateway.socketRemoteModels.sendingObject.DeviceInfo2SocketPayload
import tkamul.ae.mdmcontrollers.data.gateways.socketgateway.SocketEventCallbacks
import tkamul.ae.mdmcontrollers.domain.core.Config
import tkamul.ae.mdmcontrollers.domain.core.extentionFunction.toArgsResponse
import tkamul.ae.mdmcontrollers.domain.entities.MDMInfo
import tkamul.ae.mdmcontrollers.domain.interactors.remote.SocketRepo
import javax.inject.Inject

@AndroidEntryPoint
class MDMService : Service()  , MDMServiceEventInterface {
    private var startMode: Int = START_REDELIVER_INTENT // indicates how to behave if the service is killed
    private var allowRebind: Boolean = true   // indicates whether onRebind should be used

    @Inject
    lateinit var socketInteractor: SocketRepo

    @Inject
    lateinit var eventExecutorController: EventExecutorController


    private val mdmServiceImplementer = MDMServiceImplementer(this)




    override fun onCreate() {
        super.onCreate()
        showNotification(Config.SericeNotification.MDM_NOTIFICATION_ATTACHED_BODY)
        kotlin.runCatching {
          eventExecutorController.invokeMDMInfo {
              startListingOnSocketEvents(it)
          }
        }.onFailure {
          showNotification(it.toString() , 404)
        }
    }


    @Throws(Exception::class)
    private fun startListingOnSocketEvents(info: MDMInfo) {
        socketInteractor.observe(info.deviceInfo.serial_number , object : SocketEventCallbacks {
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
        kotlin.runCatching {
            eventExecutorController.invokeMDMInfo {
                socketInteractor.send(DeviceInfo2SocketPayload(
                    args = Args("onConnect"),
                    device = it,
                    event = Config.Events.SET_DEVICE_INFO_EVENT
                ))
            }
        }.onFailure {
            showNotification(it.toString() , 404)
        }
    }



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
        socketInteractor.disconnect()
       showNotification(Config.SericeNotification.MDM_NOTIFICATION_STOPED_BODY)
    }


    fun showNotification(message : String , notificationId: Int = System.currentTimeMillis().toInt() ) {
        val notificationBuilder =
            MDMServiceNotification.getNotificationBuilder(
                this,
                title = Config.SericeNotification.MDM_NOTIFICATION_TITLE,
                body = message
            )
        startForeground(notificationId, notificationBuilder.build());
    }

   override fun completeEvent(rayId : String  , eventId: String?) {
      eventId?.let {
          kotlin.runCatching {
              eventExecutorController.invokeMDMInfo {
                  it.executedEvent = eventId
                  socketInteractor.send(DeviceInfo2SocketPayload(
                      args = Args(rayId),
                      device = it,
                      event = Config.Events.SET_DEVICE_INFO_EVENT
                  ))
              }
          }.onFailure {
              showNotification(it.toString() , 404)
          }
      }
    }
}