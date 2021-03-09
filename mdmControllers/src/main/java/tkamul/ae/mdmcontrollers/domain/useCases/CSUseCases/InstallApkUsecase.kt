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
class InstallApkUsecase @Inject constructor(
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
     fun invoke(url : String, packageName : String,
                downloadStatusListener : (DownloadUtils.DownloadStatus)->Unit,
                installStatusListener : (DownloadUtils.DownloadStatus, Boolean)->Unit
    ){
        DownloadUtils.enqueue(context = context , url = url, packageName = packageName , downloadListener = object : DownloadUtils.DownloadListener{
                    override fun onFinish(referenceId: Long, downloadStatus: DownloadUtils.DownloadStatus) {
                        Logger.logd("onFinish  : $downloadStatus")
                        installAPk(downloadStatus, packageName,installStatusListener)

                    }

                    override fun deliverStatus(downloadStatus: DownloadUtils.DownloadStatus) {
                        Logger.logd("deliverStatus : $downloadStatus" )
                        downloadStatusListener(downloadStatus)
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
            thread {
                Thread.sleep(15*1000)
                onFinish(downloadStatus , it.packageList.containsApp(packageName))
            }
        }
    }





@Throws(RuntimeException::class)
    private fun installONQ(downloadStatus: DownloadUtils.DownloadStatus, packageName: String, onFinish: (DownloadUtils.DownloadStatus, Boolean) -> Unit) {
        mobiMediaTechServiceUtil.getQInterface {
            it.installApp(downloadStatus.fileUri,packageName)
            thread {
                Thread.sleep(15*1000)
                onFinish(downloadStatus , it.packageList.containsApp(packageName))
            }
        }
    }
}

