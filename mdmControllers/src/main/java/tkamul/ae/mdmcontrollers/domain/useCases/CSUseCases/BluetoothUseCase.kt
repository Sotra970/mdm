package tkamul.ae.mdmcontrollers.domain.useCases.CSUseCases

import android.content.Context
import android.os.Build
import tkamul.ae.mdmcontrollers.domain.core.Config
import tkamul.ae.mdmcontrollers.domain.core.Logger
import tkamul.ae.mdmcontrollers.service.MobiMediaTechServiceUtil
import tkamul.ae.mdmcontrollers.domain.core.BluetoothUtil
import java.lang.RuntimeException
import javax.inject.Inject

/**
 * Created by sotra@altakamul.tr on 2/17/2021.
 */
class BluetoothUseCase @Inject constructor(
    val mobiMediaTechServiceUtil: MobiMediaTechServiceUtil    ,
    val context: Context
){
    @Throws(RuntimeException::class)
     fun invoke(enable: Boolean , onFinish: () -> Unit){
        Logger.logd("Build.VERSION : "+Build.VERSION.SDK_INT)
        if (Build.VERSION.SDK_INT == 29) {
            invokeAndroidQSenario(enable , onFinish)
        } else if (Build.MODEL.equals(Config.MP4_MODEL_NAME)){
            invokeNotMObieSenario(enable , onFinish)
        }else {
            invokeRestAndroidApiLevelSenario(enable , onFinish)
        }
    }

    // unfortunately  enable/disable bluetooth not working with MP4
    // so this work around for normal devices which not containing mediatech interface or mobiiot SDK
    private fun invokeNotMObieSenario(
            enable: Boolean,
            onFinish: () -> Unit
    ) {
        if (enable){
            BluetoothUtil.getAdapter(context)?.enable()
        }else {
            BluetoothUtil.getAdapter(context)?.disable()
        }
        onFinish()
    }


    @Throws(RuntimeException::class)
    private fun invokeRestAndroidApiLevelSenario(
        enable: Boolean,
        onFinish: () -> Unit
    ) {
           mobiMediaTechServiceUtil.getGoInterface {
               if (enable){
                   it.enableBluetooth()
               }else{
                   it.disableBluetooth()
               }
               onFinish()
           }
    }

    @Throws(RuntimeException::class)
    private fun invokeAndroidQSenario(enable: Boolean, onFinish: () -> Unit) {
        mobiMediaTechServiceUtil.getQInterface {
            if (enable){
                it.enableBluetooth()
            }else{
                it.disableBluetooth()
            }
            onFinish()
        }
    }




}