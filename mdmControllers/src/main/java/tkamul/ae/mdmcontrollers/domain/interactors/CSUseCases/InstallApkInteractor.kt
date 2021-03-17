package tkamul.ae.mdmcontrollers.domain.interactors.CSUseCases

import android.content.Context
import android.os.Build
import tkamul.ae.mdmcontrollers.domain.core.DownloadUtils
import tkamul.ae.mdmcontrollers.domain.core.Logger
import tkamul.ae.mdmcontrollers.domain.core.extentionFunction.PackageNameExt.containsApp
import tkamul.ae.mdmcontrollers.service.MobiMediaTechServiceUtil
import javax.inject.Inject
import kotlin.concurrent.thread

/**
 * Created by sotra@altakamul.tr on 2/17/2021.
 */
class InstallApkInteractor @Inject constructor(
        var context: Context,
        val mobiMediaTechServiceUtil: MobiMediaTechServiceUtil
){
    /**
     * download apk
     * then install apk
     * deliver download status to consumer
     * deliver install status to consumer
     * @see DownloadUtils.enqueue() Documentation
     */
    @Throws(RuntimeException::class)
     fun invoke(url: String,
                downloadStatusListener: (DownloadUtils.DownloadStatus) -> Unit,
                installStatusListener: (DownloadUtils.DownloadStatus, Boolean) -> Unit
    ){
        DownloadUtils.enqueue(context = context, url = url, downloadListener = object : DownloadUtils.DownloadListener {
            override fun onFinish(referenceId: Long, downloadStatus: DownloadUtils.DownloadStatus) {
                Logger.logd("onFinish  : $downloadStatus")
                installAPk(downloadStatus, installStatusListener)

            }

            override fun deliverStatus(downloadStatus: DownloadUtils.DownloadStatus) {
                Logger.logd("deliverStatus : $downloadStatus")
                downloadStatusListener(downloadStatus)
            }

        })
    }

    fun installAPk(downloadStatus: DownloadUtils.DownloadStatus, deliverInstallStatus: (DownloadUtils.DownloadStatus, Boolean) -> Unit) {
        Logger.logd("Build.VERSION : " + Build.VERSION.SDK_INT)
        if (Build.VERSION.SDK_INT == 29) {
            installONQ(downloadStatus, deliverInstallStatus)
        } else {
            installPreQ(downloadStatus, deliverInstallStatus)
        }
    }


    @Throws(RuntimeException::class)
    private fun installPreQ(downloadStatus: DownloadUtils.DownloadStatus, onFinish: (DownloadUtils.DownloadStatus, Boolean) -> Unit) {
        val packageName = getPackageName(downloadStatus.fileUri)
        mobiMediaTechServiceUtil.getGoInterface( {
          it.installApp(downloadStatus.fileUri, packageName)
            thread {
                Thread.sleep(15 * 1000)
                onFinish(downloadStatus, it.packageList.containsApp(packageName))
            }
        })
    }





@Throws(RuntimeException::class)
    private fun installONQ(downloadStatus: DownloadUtils.DownloadStatus, onFinish: (DownloadUtils.DownloadStatus, Boolean) -> Unit) {
    val packageName = getPackageName(downloadStatus.fileUri)
        mobiMediaTechServiceUtil.getQInterface( {
            it.installApp(downloadStatus.fileUri, packageName)
            thread {
                Thread.sleep(15 * 1000)
                onFinish(downloadStatus, it.packageList.containsApp(packageName))
            }
        })
    }

    private fun getPackageName(fileUri: String?): String{
        fileUri?.let {
            val packageInfo = context.packageManager.getPackageArchiveInfo(it, 0)
            return  packageInfo?.packageName?:""
        }
        return ""
    }
}

