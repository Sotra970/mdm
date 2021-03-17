package tkamul.ae.mdmcontrollers.contollers
import tkamul.ae.mdmcontrollers.data.gateways.socketgateway.socketRemoteModels.argsResponse.NameValuePairs
import tkamul.ae.mdmcontrollers.domain.core.Config
import tkamul.ae.mdmcontrollers.domain.entities.MDMInfo
import tkamul.ae.mdmcontrollers.domain.interactors.printer.PrintInteractor
import tkamul.ae.mdmcontrollers.domain.interactors.CSUseCases.*
import tkamul.ae.mdmcontrollers.domain.interactors.CSUseCases.InstallApkInteractor
import tkamul.ae.mdmcontrollers.domain.interactors.broadcasting.ExecuteCommandInteractor
import tkamul.ae.mdmcontrollers.domain.interactors.notification.NotificationInteractor
import java.lang.RuntimeException
import javax.inject.Inject

/**
 * Created by sotra@altakamul.tr on 2/17/2021.
 */
/**
 *  class to inject all mdm controllers
 *  app client should use this class to access on mdm usecases
 */

/**
 * don't access any use case from class variables
 *  invoke use cases via MDMControllers only
 */
class EventExecutorController  @Inject constructor(
        private val bluetoothInteractor : BluetoothInteractor,
        val locationInteractor : LocationInteractor,
        val mdmInfoInteractor : MDMInfoInteractor,
        val mobileDataInteractor : MobileDataInteractor,
        val nfcInteractor : NFCInteractor,
        val rebootInteractor : RebootInteractor,
        val shutdownInteractor : ShutdownInteractor,
        val wifiInteractor: WifiInteractor,
        val printInteractor: PrintInteractor,
        val installApkInteractor: InstallApkInteractor,
        val unInstallApkInteractor: UnInstallApkInteractor,
        val executeCommandInteractor: ExecuteCommandInteractor,
        val notificationInteractor: NotificationInteractor ,
        val sendInfoController: SendInfoController
){




    /**
     * event router : routing from a string event come from socket to a use case
     */
    fun invokProcess(pairs : NameValuePairs) {
         kotlin.runCatching {
             when(pairs.event){
                 Config.Events.WIFI_EVENT_ON -> {
                     invokeWifi( true,pairs)
                 }
                 Config.Events.WIFI_EVENT_OFF -> {
                     invokeWifi( false,pairs)
                 }
                 Config.Events.DATA_EVENT_ON -> {
                     invokedata( true,pairs)
                 }
                 Config.Events.DATA_EVENT_OFF -> {
                     invokedata( false,pairs)
                 }
                 Config.Events.BLUETHOOTH_EVENT_ON -> {
                     invokeBluetooth( true,pairs)
                 }
                 Config.Events.BLUETHOOTH_EVENT_OFF -> {
                     invokeBluetooth( false,pairs)
                 }
                 Config.Events.NFC_EVENT_ON -> {
                     invokeNFC( true,pairs)
                 }
                 Config.Events.NFC_EVENT_OFF -> {
                     invokeNFC( false,pairs)
                 }
                 Config.Events.REBOOT_EVENT -> {
                     rebootInteractor.invoke()
                 }
                 Config.Events.POWERR_OFF_EVENT -> {
                     shutdownInteractor.invoke()
                 }
                 Config.Events.LOCATION_EVENT_ON -> {
                     invokeLocation( true,pairs)
                 }
                 Config.Events.LOCATION_EVENT_OFF -> {
                     invokeLocation( false,pairs)
                 }
                 Config.Events.PRINT_EVENT -> {
                     invokePrint(pairs)
                 }
                 Config.Events.INSTALL_EVENT -> {
                     invokeInstallApk(pairs)
                 }
                 Config.Events.UNINSTALL_EVENT -> {
                     invokeUnInstallApk(pairs)
                 }

                 Config.Events.EXECUTE_REMOTE_COMMAND_EVENT -> {
                     invokeExcecuteRemoteCommand(pairs)
                 }

                 Config.Events.NOTIFICATION_EVENT -> {
                     invokeNotification(pairs)
                 }

             }
         }.onFailure {
             // TODO: 3/14/2021  send error to socket
             throw it
         }
    }

    private fun invokeNotification(pairs: NameValuePairs) {
        notificationInteractor.invoke(pairs.args.nameValuePairs.title!! , pairs.args.nameValuePairs.body!!)
    }

    @Throws(RuntimeException::class)
    private fun invokeExcecuteRemoteCommand(pairs: NameValuePairs) {
        executeCommandInteractor.invoke(pairs.args.nameValuePairs.commandId!!,pairs.args.nameValuePairs.ray_id)
    }

    /**
     * invoke print use case
     */
    @Throws(RuntimeException::class)
    private fun invokeInstallApk(pairs: NameValuePairs) {
        installApkInteractor.invoke(
                url = pairs.args.nameValuePairs.url!!,
                downloadStatusListener = {
                    sendInfoController.sendInstallStatusToSocket(pairs,it,false)
                },
                installStatusListener = { downloadStatus, installStatus->
                    sendInfoController.sendInstallStatusToSocket(pairs,downloadStatus,installStatus)
                }
        )
    }
    /**
     * invoke print use case
     */
    @Throws(RuntimeException::class)
    private fun invokeUnInstallApk(pairs: NameValuePairs) {
        unInstallApkInteractor.invoke(
                packageName = pairs.args.nameValuePairs.packageName!!,
                onFinish = {
                    sendInfoController.sendUnInstallStatusToSocket(pairs,it)
                }
        )
    }

    /**
     * invoke print use case
     */
    @Throws(RuntimeException::class)
    private fun invokePrint(pairs: NameValuePairs) {
        printInteractor.invoke(pairs.args.nameValuePairs.printText?:"null"){ lastLineStatus->
            sendInfoController.setPrintStatusToSocket(pairs , lastLineStatus)
        }

    }

    /**
     *  invoke location use case
      */
    @Throws(RuntimeException::class)
    private fun invokeLocation(enable: Boolean, pairs: NameValuePairs) {
        locationInteractor.invoke(enable){
             sendInfoController.sendStatusToSocket(pairs)
        }
    }

    /**
     * invoke nfc use case
     */
    @Throws(RuntimeException::class)
    private fun invokeNFC(enable: Boolean, pairs: NameValuePairs) {
        nfcInteractor.invoke(enable){
             sendInfoController.sendStatusToSocket(pairs)
        }
    }

    /**
     * invoke bluetooth use case
     */
    @Throws(RuntimeException::class)
    private fun invokeBluetooth(enable: Boolean, pairs: NameValuePairs) {
        bluetoothInteractor.invoke(enable){
            sendInfoController.sendStatusToSocket(pairs)
        }
    }

    /**
     * invoke mobile data use case
     */
    @Throws(RuntimeException::class)
    private fun invokedata(enable: Boolean, pairs: NameValuePairs) {
        mobileDataInteractor.invoke(enable){
             sendInfoController.sendStatusToSocket(pairs)
        }
    }


    /**
     * invoke wifi use case
     */
    @Throws(RuntimeException::class)
    fun invokeWifi(enable: Boolean, pairs: NameValuePairs){
        wifiInteractor.invoke(enable) {
             sendInfoController.sendStatusToSocket(pairs)
        }
     }


    /**
     * pass mdm info obj to mdmInfoListener
     * used From service to get serial number to be a query parameter for socket channel
     */
    @Throws(RuntimeException::class)
    fun invokeMDMInfo(mdmInfoListener: (MDMInfo) -> Unit) {
        mdmInfoInteractor.invoke {
            mdmInfoListener(it)
        }
    }


}