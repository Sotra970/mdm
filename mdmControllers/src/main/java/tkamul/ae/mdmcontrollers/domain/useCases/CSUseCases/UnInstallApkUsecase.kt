package tkamul.ae.mdmcontrollers.domain.useCases.CSUseCases

import android.content.Context
import android.os.Build
import tkamul.ae.mdmcontrollers.domain.core.DownloadUtils
import tkamul.ae.mdmcontrollers.domain.core.Logger
import tkamul.ae.mdmcontrollers.domain.core.extentionFunction.PackageNameExt.containsApp
import tkamul.ae.mdmcontrollers.service.MobiMediaTechServiceUtil
import java.lang.RuntimeException
import javax.inject.Inject
import kotlin.concurrent.thread

/**
 * Created by sotra@altakamul.tr on 2/17/2021.
 */
class UnInstallApkUsecase @Inject constructor(
        val mobiMediaTechServiceUtil: MobiMediaTechServiceUtil
){

    @Throws(RuntimeException::class)
    fun invoke(packageName: String, onFinish: (Boolean) -> Unit) {
        Logger.logd("Build.VERSION : "+ Build.VERSION.SDK_INT)
        if (Build.VERSION.SDK_INT == 29) {
            unInstallONQ(packageName,onFinish)
        } else {
            unInstallPreQ(packageName,onFinish)
        }
    }


    @Throws(RuntimeException::class)
    private fun unInstallPreQ(packageName: String, onFinish: ( Boolean) -> Unit) {
        mobiMediaTechServiceUtil.getGoInterface {
            it.removeApp(packageName)
            thread {
                Thread.sleep(15*1000)
                onFinish(!it.packageList.containsApp(packageName))
            }
        }
    }





@Throws(RuntimeException::class)
    private fun unInstallONQ(packageName: String, onFinish: ( Boolean) -> Unit) {
        mobiMediaTechServiceUtil.getQInterface {
            it.removeApp(packageName)
            thread {
                Thread.sleep(15*1000)
                onFinish(!it.packageList.containsApp(packageName))
            }
        }
    }
}

