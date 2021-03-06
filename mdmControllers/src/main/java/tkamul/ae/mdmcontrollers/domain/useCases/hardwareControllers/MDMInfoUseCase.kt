package tkamul.ae.mdmcontrollers.domain.useCases.hardwareControllers

import android.content.Context
import android.os.Build
import com.mediatek.settings.service.DeviceInfo
import org.json.JSONObject
import tkamul.ae.mdmcontrollers.domain.core.Logger
import tkamul.ae.mdmcontrollers.service.MobiMediaTechServiceUtil
import tkamul.ae.mdmcontrollers.domain.useCases.hardwareControllers.core.BluetoothUtil
import tkamul.ae.mdmcontrollers.domain.useCases.hardwareControllers.core.LocationUtils
import tkamul.ae.mdmcontrollers.domain.useCases.hardwareControllers.core.NFCUtil
import java.lang.RuntimeException
import javax.inject.Inject

/**
 * Created by sotra@altakamul.tr on 2/17/2021.
 */

data class  MDMInfo(
    val deviceInfo: DeviceInfo,
    val wifiStatus: String?,
    val nfcStatus: String?,
    val locationStatus: String?,
    val dataStatus: String?,
    val bluetoothStatus: String?
)
class MDMInfoUseCase @Inject constructor(
    var mobiMediaTechServiceUtil: MobiMediaTechServiceUtil ,
    var  context: Context
){
    @Throws(RuntimeException::class)
     fun invoke(getDeviceInfo: (MDMInfo) -> Unit){
        Logger.logd("Build.VERSION : "+Build.VERSION.SDK_INT)
        if (Build.VERSION.SDK_INT == 29) {
            invokeAndroidQSenario( getDeviceInfo)
        } else {
            invokeRestAndroidApiLevelSenario( getDeviceInfo)
        }
    }


    @Throws(RuntimeException::class)
    private fun invokeRestAndroidApiLevelSenario(getMDMInfo: (MDMInfo) -> Unit) {
           mobiMediaTechServiceUtil.getGoInterface {
              getMDMInfo(
                  MDMInfo(
                       deviceInfo = it.deviceInformation ,
                       wifiStatus = it.wifiStatus ,
                        nfcStatus = NFCUtil(context).getNFCAdapterState(),
                        locationStatus = getLocationOBJ(it.curLocationMode , it.location)  ,
                        dataStatus = it.dataStatus ,
                        bluetoothStatus = BluetoothUtil.getBluetoothAdapterState(context)
                  )
              )
           }
    }

    @Throws(RuntimeException::class)
    private fun invokeAndroidQSenario(getMDMInfo: (MDMInfo) -> Unit) {
        mobiMediaTechServiceUtil.getQInterface {
            getMDMInfo(
                MDMInfo(
                    deviceInfo = it.deviceInformation ,
                    wifiStatus = it.wifiStatus ,
                    nfcStatus = NFCUtil(context).getNFCAdapterState(),
                    locationStatus = getLocationOBJ(it.curLocationMode , it.location)  ,
                    dataStatus = it.dataStatus ,
                    bluetoothStatus = BluetoothUtil.getBluetoothAdapterState(context)
                )
            )
        }
    }

    private fun getLocationOBJ(mode : Int, interfaceCoordinates :String?): String {
        return JSONObject().apply {
                put("mode" , mode)
                put("interfaceCoordinates" , interfaceCoordinates)
                put("mangerCoordinates" , LocationUtils.getLastKnownLocation(context) )
        }.toString()
    }


}