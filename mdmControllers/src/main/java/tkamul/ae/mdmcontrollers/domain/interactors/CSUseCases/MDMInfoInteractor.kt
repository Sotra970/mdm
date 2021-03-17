package tkamul.ae.mdmcontrollers.domain.interactors.CSUseCases

import android.content.Context
import android.os.Build
import android.telephony.TelephonyManager
import org.json.JSONObject
import tkamul.ae.mdmcontrollers.domain.core.Logger
import tkamul.ae.mdmcontrollers.service.MobiMediaTechServiceUtil
import tkamul.ae.mdmcontrollers.domain.core.BluetoothUtil
import tkamul.ae.mdmcontrollers.domain.core.LocationUtils
import tkamul.ae.mdmcontrollers.domain.core.NFCUtil
import tkamul.ae.mdmcontrollers.domain.core.extentionFunction.toPackageInfoList
import tkamul.ae.mdmcontrollers.domain.entities.MDMInfo
import tkamul.ae.mdmcontrollers.domain.interactors.printer.PrintInteractor
import java.lang.RuntimeException
import javax.inject.Inject

/**
 * Created by sotra@altakamul.tr on 2/17/2021.
 */


class MDMInfoInteractor @Inject constructor(
        var mobiMediaTechServiceUtil: MobiMediaTechServiceUtil,
        var printerController : PrintInteractor,
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
           mobiMediaTechServiceUtil.getGoInterface ({
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
                  ).apply {
                      if (deviceInfo.serial_number.trim().isEmpty()){
                          val telephonyMgr = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                          if (!telephonyMgr.deviceId.isEmpty())
                          deviceInfo.serial_number = telephonyMgr.deviceId
                          else if (!it.deviceInformation.sim1_imei.isEmpty())
                              deviceInfo.serial_number =  it.deviceInformation.sim1_imei
                          else if (!it.deviceInformation.sim2_imei.isEmpty())
                              deviceInfo.serial_number =  it.deviceInformation.sim2_imei

                      }
                  }
              )
           })
    }

    @Throws(RuntimeException::class)
    private fun invokeAndroidQSenario(getMDMInfo: (MDMInfo) -> Unit) {
        mobiMediaTechServiceUtil.getQInterface ({
            getMDMInfo(
                MDMInfo(
                    deviceInfo = it.deviceInformation,
                    wifiStatus = it.wifiStatus,
                    nfcStatus = NFCUtil(context).getNFCAdapterState(),
                    locationStatus = getLocationOBJ(it.curLocationMode , it.location),
                    dataStatus = it.dataStatus,
                    bluetoothStatus = BluetoothUtil.getBluetoothAdapterState(context),
                    printerStatus = printerController.getPrinterStats(),
                    installedPackages = listOf()//it.packageList.toPackageInfoList()
                )
            )
        })
    }

    private fun getLocationOBJ(mode : Int, interfaceCoordinates :String?): String {
        return JSONObject().apply {
                put("mode" , mode)
                put("interfaceCoordinates" , interfaceCoordinates)
                put("mangerCoordinates" , LocationUtils.getLastKnownLocation(context) )
        }.toString()
    }


}

