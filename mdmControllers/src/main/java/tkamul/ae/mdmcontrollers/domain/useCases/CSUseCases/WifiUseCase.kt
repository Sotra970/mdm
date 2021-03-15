package tkamul.ae.mdmcontrollers.domain.useCases.CSUseCases

import android.os.Build
import tkamul.ae.mdmcontrollers.domain.core.Logger
import tkamul.ae.mdmcontrollers.service.MobiMediaTechServiceUtil
import java.lang.RuntimeException
import javax.inject.Inject

/**
 * Created by sotra@altakamul.tr on 2/17/2021.
 */
class WifiUseCase @Inject constructor(
    var mobiMediaTechServiceUtil: MobiMediaTechServiceUtil
){
    @Throws(RuntimeException::class)
     fun invoke(enable: Boolean, onFinish: () -> Unit){
        Logger.logd("Build.VERSION : "+Build.VERSION.SDK_INT)
        if (Build.VERSION.SDK_INT == 29) {
            invokeAndroidQSenario(enable , onFinish)
        } else {
            invokeRestAndroidApiLevelSenario(enable , onFinish)
        }
    }


    @Throws(RuntimeException::class)
    private fun invokeRestAndroidApiLevelSenario(
        enable: Boolean,
        onFinish: () -> Unit
    ) {
           mobiMediaTechServiceUtil.getGoInterface ({
               if (enable){
                   it.enableWifi()
               }else{
                   it.disableWifi()
               }
               onFinish()
           })
    }

    @Throws(RuntimeException::class)
    private fun invokeAndroidQSenario(enable: Boolean, onFinish: () -> Unit) {
        mobiMediaTechServiceUtil.getQInterface({
            if (enable){
                it.enableWifi()
            }else{
                it.disableWifi()
            }
            onFinish()
        })
    }
}