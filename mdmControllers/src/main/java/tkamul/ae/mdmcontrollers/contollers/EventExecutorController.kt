package tkamul.ae.mdmcontrollers.contollers
import tkamul.ae.mdmcontrollers.PrinterModule.models.config.LinePrintingStatus
import tkamul.ae.mdmcontrollers.data.gateways.socketModels.argsResponse.NameValuePairs
import tkamul.ae.mdmcontrollers.data.gateways.socketModels.argsResponse.NameValuePairsX
import tkamul.ae.mdmcontrollers.data.gateways.socketModels.sendingObject.Args
import tkamul.ae.mdmcontrollers.data.gateways.socketModels.sendingObject.DeviceInfo2SocketPayload
import tkamul.ae.mdmcontrollers.data.gateways.socketModels.sendingObject.InstallInfo2SocketPayload
import tkamul.ae.mdmcontrollers.data.gateways.socketModels.sendingObject.UnInstallInfo2SocketPayload
import tkamul.ae.mdmcontrollers.domain.core.Config
import tkamul.ae.mdmcontrollers.domain.core.DownloadUtils
import tkamul.ae.mdmcontrollers.domain.entities.MDMInfo
import tkamul.ae.mdmcontrollers.domain.useCases.CSUseCases.PrintUseCase
import tkamul.ae.mdmcontrollers.domain.useCases.CSUseCases.*
import tkamul.ae.mdmcontrollers.domain.useCases.CSUseCases.InstallApkUsecase
import tkamul.ae.mdmcontrollers.domain.useCases.remote.MDMSocketChannelUseCase
import javax.inject.Inject
import kotlin.concurrent.thread

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
        val bluetoothController  : BluetoothUseCase,
        val locationController  : LocationUseCase,
        val mdmInfoController  : MDMInfoUseCase,
        val mobileDataController  : MobileDataUseCase,
        val nfcController  : NFCUseCase,
        val rebootController  : RebootUseCase,
        val shutdownController  : ShutdownUseCase,
        val wifiController : WifiUseCase,
        val printController : PrintUseCase,
        val installApkController : InstallApkUsecase,
        val unInstallApkController : UnInstallApkUsecase,
        val sendInfoController: SendInfoController
){




    /**
     * event router : routing from a string event come from socket to a use case
     */
    fun invokProcess(pairs : NameValuePairs) {
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
                   rebootController.invoke()
                }
                Config.Events.POWERR_OFF_EVENT -> {
                    shutdownController.invoke()
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

            }
    }
    /**
     * invoke print use case
     */
    private fun invokeInstallApk(pairs: NameValuePairs) {
        installApkController.invoke(
                url = pairs.args.nameValuePairs.url!!,
                packageName = pairs.args.nameValuePairs.packageName!!,
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
    private fun invokeUnInstallApk(pairs: NameValuePairs) {
        unInstallApkController.invoke(
                packageName = pairs.args.nameValuePairs.packageName!!,
                onFinish = {
                    sendInfoController.sendUnInstallStatusToSocket(pairs,it)
                }
        )
    }

    /**
     * invoke print use case
     */
    private fun invokePrint(pairs: NameValuePairs) {
        val lastLineStatus = printController.invoke(pairs.args.nameValuePairs.printText?:"null")
        sendInfoController.setPrintStatusToSocket(pairs , lastLineStatus)
    }

    /**
     *  invoke location use case
      */
    private fun invokeLocation(enable: Boolean, pairs: NameValuePairs) {
        locationController.invoke(enable){
             sendInfoController.sendStatusToSocket(pairs)
        }
    }

    /**
     * invoke nfc use case
     */
    private fun invokeNFC(enable: Boolean, pairs: NameValuePairs) {
        nfcController.invoke(enable){
             sendInfoController.sendStatusToSocket(pairs)
        }
    }

    /**
     * invoke bluetooth use case
     */
    private fun invokeBluetooth(enable: Boolean, pairs: NameValuePairs) {
        bluetoothController.invoke(enable){
            sendInfoController.sendStatusToSocket(pairs)
        }
    }

    /**
     * invoke mobile data use case
     */
    private fun invokedata(enable: Boolean, pairs: NameValuePairs) {
        mobileDataController.invoke(enable){
             sendInfoController.sendStatusToSocket(pairs)
        }
    }


    /**
     * invoke wifi use case
     */
    fun invokeWifi(enable: Boolean, pairs: NameValuePairs){
        wifiController.invoke(enable) {
             sendInfoController.sendStatusToSocket(pairs)
        }
     }


    /**
     * pass mdm info obj to mdmInfoListener
     * used From service to get serial number to be a query parameter for socket channel
     */
    fun invokeMDMInfo(mdmInfoListener: (MDMInfo) -> Unit) {
        mdmInfoController.invoke {
            mdmInfoListener(it)
        }
    }


}