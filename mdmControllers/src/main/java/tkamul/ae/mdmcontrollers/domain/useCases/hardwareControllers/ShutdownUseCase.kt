package tkamul.ae.mdmcontrollers.domain.useCases.hardwareControllers

import android.os.Build
import tkamul.ae.mdmcontrollers.domain.core.Logger
import tkamul.ae.mdmcontrollers.service.MobiMediaTechServiceUtil
import java.lang.RuntimeException
import javax.inject.Inject

/**
 * Created by sotra@altakamul.tr on 2/17/2021.
 */
class ShutdownUseCase @Inject constructor(
    var mobiMediaTechServiceUtil: MobiMediaTechServiceUtil
){
    @Throws(RuntimeException::class)
     fun invoke(){
        Logger.logd("Build.VERSION : "+Build.VERSION.SDK_INT)
        if (Build.VERSION.SDK_INT == 29) {
            mobiMediaTechServiceUtil.getQInterface {
                it.shutDown()
            }
        } else {
            mobiMediaTechServiceUtil.getGoInterface {
                it.shutDown()
            }
        }
    }
}