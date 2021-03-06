package tkamul.ae.mdmcontrollers.domain.useCases.CSUseCases

import android.content.Context
import tkamul.ae.mdmcontrollers.service.MobiMediaTechServiceUtil
import tkamul.ae.mdmcontrollers.domain.core.NFCUtil
import javax.inject.Inject


/**
 * Created by sotra@altakamul.tr on 2/17/2021.
 */
class NFCUseCase @Inject constructor(
    val mobiMediaTechServiceUtil: MobiMediaTechServiceUtil,
    val context: Context
){
    @Throws(RuntimeException::class)
     fun invoke(enable: Boolean, onFinish: () -> Unit){
        NFCUtil(context).nfcSwitch(enable)
        onFinish()
    }

}