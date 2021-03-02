package tkamul.ae.mdmcontrollers.contollers
import tkamul.ae.mdmcontrollers.data.gateways.socketModels.argsResponse.NameValuePairs
import tkamul.ae.mdmcontrollers.data.gateways.socketModels.argsResponse.NameValuePairsX
import tkamul.ae.mdmcontrollers.data.gateways.socketModels.sendingObject.Args
import tkamul.ae.mdmcontrollers.data.gateways.socketModels.sendingObject.DeviceInfo2SocketPayload
import tkamul.ae.mdmcontrollers.domain.core.Config
import tkamul.ae.mdmcontrollers.domain.useCases.hardwareControllers.*
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
class MDMControllers  @Inject constructor(
    val bluetoothController  : BluetoothUseCase,
    val locationController  : LocationUseCase,
    val mdmInfoController  : MDMInfoUseCase,
    val mobileDataController  : MobileDataUseCase,
    val nfcController  : NFCUseCase,
    val rebootController  : RebootUseCase,
    val shutdownController  : ShutdownUseCase,
    val wifiController : WifiUseCase,
    var mdmSocketChannelController : MDMSocketChannelUseCase
){


    /**
     * event router : routing from a string event to a use case
     */
    fun invokProcess(event : String) {
        invokProcess(
            NameValuePairs(
            event = event ,
            args = tkamul.ae.mdmcontrollers.data.gateways.socketModels.argsResponse.Args(
                NameValuePairsX("internal")
            )
        ))
    }
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

            }
    }

    /**
     *  invoke location use case
      */
    private fun invokeLocation(enable: Boolean, pairs: NameValuePairs) {
        locationController.invoke(enable){
            sendMobileStatus(pairs)
        }
    }

    /**
     * invoke nfc use case
     */
    private fun invokeNFC(enable: Boolean, pairs: NameValuePairs) {
        nfcController.invoke(enable){
            sendMobileStatus(pairs)
        }
    }

    /**
     * invoke bluetooth use case
     */
    private fun invokeBluetooth(enable: Boolean, pairs: NameValuePairs) {
        bluetoothController.invoke(enable){
            sendMobileStatus(pairs)
        }
    }

    /**
     * invoke mobile data use case
     */
    private fun invokedata(enable: Boolean, pairs: NameValuePairs) {
        mobileDataController.invoke(enable){
            sendMobileStatus(pairs)
        }
    }


    /**
     * invoke wifi use case
     */
    fun invokeWifi(enable: Boolean, pairs: NameValuePairs){
        wifiController.invoke(enable) {
            sendMobileStatus(pairs)
        }
     }

    /**
     * function to send all controls status + device info
     */
    private fun sendMobileStatus( pairs: NameValuePairs) {
        thread {
            // sleep 5 sec to wait controllers status to be ready
            // ex : wait wifi until have a status of (enable/disable) not (enabling/disabling )
            //      then send mobile info
            Thread.sleep(5*1000)
            mdmInfoController.invoke {
                mdmSocketChannelController.send(DeviceInfo2SocketPayload(
                    event = pairs.event ,
                    device = it,
                    args = Args(pairs.args.nameValuePairs.ray_id)
                ))
            }
        }
    }

    /**
     * pass mdm info obj to @getMDMIVfo
     * used from service to get serial number to be a query parameter for socket channel
     */
    fun invokeMDMInfo(deliverMDMInfoModel: (MDMInfo) -> Unit) {
        mdmInfoController.invoke {
            deliverMDMInfoModel(it)
        }
    }


}