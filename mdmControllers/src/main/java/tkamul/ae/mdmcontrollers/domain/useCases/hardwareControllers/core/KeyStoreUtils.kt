package tkamul.ae.mdmcontrollers.domain.useCases.hardwareControllers.core

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.os.Build
import tkamul.ae.mdmcontrollers.domain.core.Logger
import tkamul.ae.mdmcontrollers.domain.useCases.hardwareControllers.MDMInfoUseCase
import tkamul.ae.mdmcontrollers.service.MobiMediaTechServiceUtil

class KeyStoreUtils(
    val mobiMediaTechServiceUtil : MobiMediaTechServiceUtil,
    val context: Context
) {

    fun addKeyStore() {

        if (Build.VERSION.SDK_INT == 29) {
            invokeAndroidQSenario()
        } else {
            invokeRestAndroidApiLevelSenario()
        }


    }

    private fun invokeRestAndroidApiLevelSenario() {
        mobiMediaTechServiceUtil.getGoInterface { goInterface->
            val pm  = context.getPackageManager()
            getSignatures(pm , context.packageName)?.let {
                for (sig in it ){
                    Logger.logd(sig)
                    goInterface.addKeystoreToList(sig)
                }
            }
        }
    }

    private fun invokeAndroidQSenario() {
        mobiMediaTechServiceUtil.getQInterface { qInterface->
            val pm  = context.getPackageManager()
            getSignatures(pm , context.packageName)?.let {
                for (sig in it ){
                    Logger.logd(sig)
                    qInterface.addKeystoreToList(sig)
                }
            }
        }
    }

    private fun getSignatures(pm: PackageManager, packageName: String): List<ByteArray>? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
                if (packageInfo?.signingInfo == null) {
                    return null
                }
                if (packageInfo.signingInfo.hasMultipleSigners()) {
                    signatureDigest(packageInfo.signingInfo.apkContentsSigners)
                } else {
                    signatureDigest(packageInfo.signingInfo.signingCertificateHistory)
                }
            } else {
                @SuppressLint("PackageManagerGetSignatures") val packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
                if (packageInfo?.signatures == null || packageInfo.signatures.isEmpty()) {
                    null
                } else signatureDigest(packageInfo.signatures)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
    }

    private fun signatureDigest(sig: Signature): ByteArray {
        return  sig.toByteArray()
    }

    private fun signatureDigest(sigList: Array<Signature>): List<ByteArray> {
        val signaturesList: MutableList<ByteArray> = ArrayList()
        for (signature in sigList) {
            signaturesList.add(signatureDigest(signature))
        }
        return signaturesList
    }

}
