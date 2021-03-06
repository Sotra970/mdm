package tkamul.ae.mdmcontrollers.domain.useCases

import android.content.Context
import android.os.Build
import com.google.gson.Gson
import tkamul.ae.mdmcontrollers.domain.core.DownloadUtils
import tkamul.ae.mdmcontrollers.domain.core.Logger
import tkamul.ae.mdmcontrollers.service.MobiMediaTechServiceUtil
import java.lang.RuntimeException
import javax.inject.Inject

/**
 * Created by sotra@altakamul.tr on 2/17/2021.
 */
class InstallApkUsecase @Inject constructor(
        var context: Context,
        val mobiMediaTechServiceUtil: MobiMediaTechServiceUtil
        ){
    /**
     * print incoming text
     * return printing status
     */
    @Throws(RuntimeException::class)
     fun invoke(url : String , appName:String,packageName : String ,
                deliverDownloadStatus : (DownloadUtils.DownloadStatus)->Unit ,
                deliverInstallStatus : (DownloadUtils.DownloadStatus, Boolean)->Unit
    ){
        DownloadUtils.enqueue(
                context ,
                url,
                appName,
                false ,
                object : DownloadUtils.DownloadListener{
                    override fun onFinish(referenceId: Long, downloadStatus: DownloadUtils.DownloadStatus) {
                        Logger.logd("onFinish  : $downloadStatus")
                        installAPk(downloadStatus, packageName,deliverInstallStatus)

                    }

                    override fun deliverStatus(downloadStatus: DownloadUtils.DownloadStatus) {
                        Logger.logd("deliverStatus : $downloadStatus" )
                        deliverDownloadStatus(downloadStatus)
                    }

                }
        )
    }

    fun installAPk(downloadStatus: DownloadUtils.DownloadStatus, packageName: String, deliverInstallStatus: (DownloadUtils.DownloadStatus, Boolean) -> Unit) {
        Logger.logd("Build.VERSION : "+ Build.VERSION.SDK_INT)
        if (Build.VERSION.SDK_INT == 29) {
            installONQ(downloadStatus , packageName,deliverInstallStatus)
        } else {
            installPreQ(downloadStatus , packageName,deliverInstallStatus)
        }
    }


    @Throws(RuntimeException::class)
    private fun installPreQ(downloadStatus: DownloadUtils.DownloadStatus, packageName: String, onFinish: (DownloadUtils.DownloadStatus, Boolean) -> Unit) {
        mobiMediaTechServiceUtil.getGoInterface {
          it.installApp(downloadStatus.fileUri,packageName)
            val x = it.packageList
            onFinish(downloadStatus , it.packageList.containsApp(packageName))

        }
    }



   /* AppType:ThirdApp

    PackageName:numan.altkamul

    AppName:AlTkamul

    Version:1.2.1.8
*/

    private fun <E> MutableList<E>.containsApp(packageName: String): Boolean {
    for (child in this){
        child as String
        val childPackageName  = child.substring(child.indexOf("PackageName")+"PackageName:".length , child.indexOf("AppName")).trim()
        if (childPackageName.equals(packageName,true))
            return true

    }
    return false
}

@Throws(RuntimeException::class)
    private fun installONQ(downloadStatus: DownloadUtils.DownloadStatus, packageName: String, onFinish: (DownloadUtils.DownloadStatus, Boolean) -> Unit) {
        mobiMediaTechServiceUtil.getQInterface {
            it.installApp(downloadStatus.fileUri,packageName)
            onFinish(downloadStatus , it.packageList.containsApp(packageName))
        }
    }
}

