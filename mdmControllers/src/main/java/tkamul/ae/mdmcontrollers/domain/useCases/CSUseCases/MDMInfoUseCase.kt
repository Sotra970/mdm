package tkamul.ae.mdmcontrollers.domain.useCases.CSUseCases

import android.content.Context
import android.os.Build
import org.json.JSONObject
import tkamul.ae.mdmcontrollers.domain.core.Logger
import tkamul.ae.mdmcontrollers.service.MobiMediaTechServiceUtil
import tkamul.ae.mdmcontrollers.domain.core.BluetoothUtil
import tkamul.ae.mdmcontrollers.domain.core.LocationUtils
import tkamul.ae.mdmcontrollers.domain.core.NFCUtil
import tkamul.ae.mdmcontrollers.domain.core.extentionFunction.toPackageInfoList
import tkamul.ae.mdmcontrollers.domain.entities.MDMInfo
import java.lang.RuntimeException
import javax.inject.Inject

/**
 * Created by sotra@altakamul.tr on 2/17/2021.
 */


class MDMInfoUseCase @Inject constructor(
    var mobiMediaTechServiceUtil: MobiMediaTechServiceUtil ,
    var printerController : PrintUseCase,
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
                        bluetoothStatus = BluetoothUtil.getBluetoothAdapterState(context),
                        printerStatus = printerController.getPrinterStats(),
                        installedPackages = it.packageList.toPackageInfoList()
                  )
              )
           }
    }

    @Throws(RuntimeException::class)
    private fun invokeAndroidQSenario(getMDMInfo: (MDMInfo) -> Unit) {
        mobiMediaTechServiceUtil.getQInterface {
            getMDMInfo(
                MDMInfo(
                    deviceInfo = it.deviceInformation,
                    wifiStatus = it.wifiStatus,
                    nfcStatus = NFCUtil(context).getNFCAdapterState(),
                    locationStatus = getLocationOBJ(it.curLocationMode , it.location),
                    dataStatus = it.dataStatus,
                    bluetoothStatus = BluetoothUtil.getBluetoothAdapterState(context),
                    printerStatus = printerController.getPrinterStats(),
                    installedPackages = it.packageList.toPackageInfoList()
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

