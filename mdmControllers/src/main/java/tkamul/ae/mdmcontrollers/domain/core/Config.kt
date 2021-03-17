package tkamul.ae.mdmcontrollers.domain.core

/**
 * Created by sotra@altakamul.tr on 2/19/2021.
 */
object Config {
    object NFC {
        const  val ENABLE: String = "enabled"
        const  val DISABLE: String = "disabled"
        const val NA: String = "this device not support NFC Technology"
    }

    object   Bluetooth {
        const  val OFF: String = "OFF"
        const  val TURNING_ON: String = "TURNING_ON"
        const  val ON: String = "ON"
        const  val TURNING_OFF: String = "TURNING_OFF"
        const  val BLE_TURNING_ON: String = "BLE_TURNING_ON"
        const  val BLE_ON: String = "BLE_ON"
        const  val BLE_TURNING_OFF: String = "BLE_TURNING_OFF"
        const  val BLE_TURNING_ON_VALUE = 14
        const  val BLE_ON_VALUE = 15
        const  val BLE_TURNING_OFF_VALUE = 16
        const  val NA: String = "State not available : "
    }

    object SericeNotification {
        val MDM_NOTIFICATION_ATTACHED_BODY: String="MDM Attached"
        const val MDM_NOTIFICATION_TITLE = "MDM Controller"
        const val MDM_NOTIFICATION_RUNNING_BODY = "MDM is running"
        const val MDM_NOTIFICATION_STOPED_BODY = "MDM has been stopped"
        const val MDM_NOTIFICATION_DISCONECTED_BODY = "MDM has been disconnected"
        const val MDM_NOTIFICATION_MOBIE_DISCONECTED_BODY = "media tech service  has been stopped "
        const val MDM_NOTIFICATION_CHANNNEL_ID = "MDM_ID"
        const val MDM_NOTIFICATION_CHANNNEL_NAME = "MDM"
    }

    const val MP4P_MODEL_NAME =  "MobiPrint 4+"
    const val MP4_MODEL_NAME =  "MP4"
    const val MP3P_MODEL_NAME =  "MP3_Plus"
    const val MP3_MODEL_NAME =  "MP3"
    const val MDM_SOCKET_CHANNEL: String = "message"
    const val MDM_SOCKET_URL = "https://mdm-server-io.herokuapp.com"

    object Events {
        const val REMOTE_COMMAND_RECEIVER ="REMOTE_COMMAND_RECEIVER"
        const val COMMAND_ID_KEY =  "COMMAND_ID_KEY"
        const val COMMAND_RAY_ID_KEY =  "COMMAND_RAY_ID"
        const val EXECUTE_REMOTE_COMMAND_EVENT =  "execute"
        const val NOTIFICATION_EVENT =  "notification"
        const val  UNINSTALL_EVENT= "uninstall-apk"
        const val  INSTALL_EVENT= "install-apk"
        const val SET_DEVICE_INFO_EVENT: String = "setDeviceInfo"
        const val WIFI_EVENT_ON = "wifi-on"
        const val WIFI_EVENT_OFF = "wifi-off"
        const val DATA_EVENT_ON = "data-on"
        const val DATA_EVENT_OFF = "data-off"
        const val BLUETHOOTH_EVENT_ON = "blutooth-on"
        const val BLUETHOOTH_EVENT_OFF = "bluetooth-off"
        const val NFC_EVENT_ON = "nfc-on"
        const val NFC_EVENT_OFF = "nfc-off"
        const val REBOOT_EVENT = "reboot"
        const val POWERR_OFF_EVENT = "power-off"
        const val LOCATION_EVENT_ON = "location-on"
        const val LOCATION_EVENT_OFF = "location-off"
        const val PRINT_EVENT = "print"
    }


}