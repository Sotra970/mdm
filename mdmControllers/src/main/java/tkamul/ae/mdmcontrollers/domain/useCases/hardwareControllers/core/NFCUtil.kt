package tkamul.ae.mdmcontrollers.domain.useCases.hardwareControllers.core

import android.content.Context
import android.nfc.NfcAdapter
import android.nfc.NfcManager
import tkamul.ae.mdmcontrollers.domain.core.Config
import tkamul.ae.mdmcontrollers.domain.core.Logger
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

/**
 * Created by sotra@altakamul.tr on 2/26/2021.
 */
class NFCUtil(val context: Context) {


    private val nfcAdapter: NfcAdapter? by lazy{
        val manager = context.getSystemService(Context.NFC_SERVICE) as NfcManager
        manager.defaultAdapter
    }
    internal fun getNFCAdapterState(): String {
        return  nfcAdapter?.let {
            if (it.isEnabled) return Config.NFC.ENABLE
            else Config.NFC.DISABLE
        }?: Config.NFC.NA
    }

    //   the app must be system app to enable and disable nfc
//    adb shell pm grant ae.tkamul.mdm android.permission.WRITE_SECURE_SETTINGS
    internal fun nfcSwitch(enable : Boolean) {
        nfcAdapter?.let {
            val NfcManagerClass: Class<*>
            val setNfcEnabled: Method
            try {
                NfcManagerClass = Class.forName(it::class.java.name)
                setNfcEnabled = NfcManagerClass.getDeclaredMethod(if (enable) "enable" else "disable")
                setNfcEnabled.setAccessible(true)
                setNfcEnabled.invoke(nfcAdapter)
            } catch (e: ClassNotFoundException) {
                Logger.loge(e)
            } catch (e: NoSuchMethodException) {
                Logger.loge(e)
            } catch (e: IllegalArgumentException) {
                Logger.loge(e)
            } catch (e: IllegalAccessException) {
                Logger.loge(e)
            } catch (e: InvocationTargetException) {
                Logger.loge(e)
            }
        }
    }


}